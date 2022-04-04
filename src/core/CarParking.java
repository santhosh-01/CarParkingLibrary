package core;

import java.util.ArrayList;

public class CarParking {

    private final MultiFloorCarParking obj;
    private final ArrayList<ParkingLot> arr;
    private final DataProvider dataProvider;
    private final DataPrinter dataPrinter;

    private final CarInParking carInParking;
    private final CarEntryExitTable carEntryExitTable;

    public CarParking(MultiFloorCarParking obj, DataProvider dataProvider, DataPrinter dataPrinter,
                      CarInParking carInParking, CarEntryExitTable carEntryExitTable) {
        this.obj = obj;
        this.arr = obj.getParkingLots();
        this.dataProvider = dataProvider;
        this.dataPrinter = dataPrinter;
        this.carInParking = carInParking;
        this.carEntryExitTable = carEntryExitTable;
    }

    private void showPathToParkACar(ParkingLot parkingLot, int row, int col) {
        String driverSide = obj.getDriverSide();
        if(driverSide.equals("L")) {
            parkingLot.setDetailedLeftEntryPath(row, col);
        }
        else {
            parkingLot.setDetailedEntryPath(row, col);
        }
        System.out.println(parkingLot.getDetailedPath());
        parkingLot.removeDirections();
    }

    public CarLocation checkAndSuggestLastCarParkingPlace(Car car){
        CarEntryExit carEntryExit = checkAndGetLastCarEntryExit(car);
        if(carEntryExit == null) return null;
        CarLocation pos = carEntryExit.getPosition();
        ParkingLot parkingLot = arr.get(pos.getFloorNo());

        dataPrinter.LastCarParkingDetails(pos.getCarParkingSpot().getCarParkingSpotNumber(),pos.getFloorNo(),carEntryExit);

        if(obj.getLowestFloorWithVacancy()<pos.getFloorNo()) {
            System.out.println("\nBut Empty Parking Place is available at lower floor");
            return null;
        }

        CarParkingSpot position1 = parkingLot.getNearestParkingSpot(pos.getCarParkingSpot().getRow()-1,
                pos.getCarParkingSpot().getCol()-1);
        ParkingCell parkingCell = parkingLot.getParkingCellByPosition(position1.getRow(),
                position1.getCol());
        CarLocation position = new CarLocation(position1,parkingCell.getParkingSpotNumber());
        CarParkingSpot carParkingSpot = position.getCarParkingSpot();
        carParkingSpot.setCarParkingSpotNumber(parkingCell.getParkingSpotNumber());
        position.setFloorNo(pos.getFloorNo());

        dataPrinter.emptyCarParkingPlace(position.getCarParkingSpot().getCarParkingSpotNumber(),position.getFloorNo());
        dataPrinter.parkingConfirmation();
        while (true) {
            String choice = dataProvider.getLastCarParkingConfirmation();
            if(choice.equalsIgnoreCase("yes")) {
                return position;
            }
            else if(choice.equalsIgnoreCase("no")) {
                return null;
            }
            else {
                dataPrinter.yesOrNoInvalidMessage();
            }
        }
    }

    private CarEntryExit checkAndGetLastCarEntryExit(Car car) {
        String carNumber = car.getCarNumber();
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(carNumber);
        if(carEntryExitMaster != null) {
            return carEntryExitMaster.getLastCarEntryExit();
        }
        else {
            carEntryExitMaster = new CarEntryExitMaster(carNumber);
            carEntryExitTable.addCar(carEntryExitMaster);
        }
        return null;
    }

    public ParkingLot suggestAndGetCarParkingLot() {
        int ind = obj.getLowestFloorWithVacancy();
        while (true) {
            String choice = dataProvider.getSuggestedParkingFloorConfirmation(ind);
            if(choice.equalsIgnoreCase("yes")) {
                return arr.get(ind);
            }
            else if(choice.equalsIgnoreCase("no")){
                while (true) {
                    dataPrinter.printParkingAvailableFloors(obj.getParkingAvailableFloors());
                    String floorNumber = dataProvider.getFloorNumber();
                    int floorNo = Validator.validateInteger(floorNumber,0,obj.getFloors()-1);
                    if(floorNo == -1) continue;
                    return arr.get(floorNo);
                }
            }
            else {
                dataPrinter.yesOrNoInvalidMessage();
            }
        }
    }

    public CarParkingSpot suggestAndGetParkingSpot(ParkingLot parkingLot) {
        int position = parkingLot.getFirstParkingSpotNumber();
        while (true) {
            String choice = dataProvider.getSuggestedParkingPlaceConfirmation(position);
            if(choice.equalsIgnoreCase("yes")) {
                return parkingLot.getCarLocation(position);
            }
            else if(choice.equalsIgnoreCase("no")){
                while (true) {
                    String num = dataProvider.takeCarParkingSpotInput(parkingLot.getFloorNo(),
                            parkingLot.getModifiedParkingLotMap(true));
                    int rows = obj.getRows();
                    int columns = obj.getColumns();
                    int floorNo = parkingLot.getFloorNo();
                    int n = Validator.validateInteger(num,((rows*columns)*floorNo)+1,
                            (rows*columns)*(floorNo+1));
                    if(n == -1) continue;
                    CarParkingSpot carParkingSpot = parkingLot.getCarLocation(n);
                    if(carParkingSpot == null) {
                        continue;
                    }
                    if(parkingLot.isValidEmptyParkingPlace(carParkingSpot.getRow(), carParkingSpot.getCol())) {
                        return carParkingSpot;
                    }
                    else {
                        dataPrinter.invalidParkingPlace();
                    }
                }
            }
            else {
                dataPrinter.yesOrNoInvalidMessage();
            }
        }
    }

    public void parkACar(ParkingLot parkingLot, CarParkingSpot carParkingSpot, Car car) {
        parkingLot.parkCarAtPosition(car, carParkingSpot.getRow(), carParkingSpot.getCol());

        carInParking.addCars(car);

        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        ParkingCell parkingCell = parkingLot.getParkingCellByPosition(carParkingSpot.getRow(),carParkingSpot.getCol());
        CarEntryExit carEntryExit = new CarEntryExit(parkingCell.getParkedTime(),
                new CarLocation(carParkingSpot.getRow()+1,carParkingSpot.getCol()+1,parkingCell.getParkingSpotNumber(),
                        parkingLot.getFloorNo()));
        carEntryExitMaster.addEntryExit(carEntryExit);
        carEntryExitMaster.addBilling(carEntryExit.getBilling());
    }

    public void generatePathToParkACar(ParkingLot parkingLot, CarParkingSpot carParkingSpot) {
        System.out.println("\nDetailed Path to park the car in the given parking place " +
                "at " + OrdinalNumber.getOrdinalNo(parkingLot.getFloorNo()) + " floor\n");
        showPathToParkACar(parkingLot,carParkingSpot.getRow(),carParkingSpot.getCol());
    }

    public boolean checkDuplicateCarNoInParking(String carNo) {
        if(carInParking.isCarNumberExist(carNo)) {
            dataPrinter.duplicateCarExist();
            return true;
        }
        else {
            return false;
        }
    }

}
