package service;

import database.CarEntryExitTable;
import database.CarInParking;
import database.CarTable;
import model.*;
import util.OrdinalNumber;
import util.Validator;

import java.util.ArrayList;

public class CarParking{

    private final MultiFloorCarParking obj;
    private final ArrayList<ParkingLot> arr;
    private final DataProvider dataProvider;
    private final DataPrinter dataPrinter;

    private final CarTable carTable;
    private final CarInParking carInParking;
    private final CarEntryExitTable carEntryExitTable;

    public CarParking(MultiFloorCarParking obj, DataProvider dataProvider, DataPrinter dataPrinter,
                      CarTable carTable, CarInParking carInParking, CarEntryExitTable carEntryExitTable) {
        this.obj = obj;
        this.arr = obj.getParkingLots();
        this.dataProvider = dataProvider;
        this.dataPrinter = dataPrinter;
        this.carTable = carTable;
        this.carInParking = carInParking;
        this.carEntryExitTable = carEntryExitTable;
    }

    private void showPathToParkACar(ParkingLot parkingLot, int row, int col) {
        String path = obj.path;
        if(path.equals("L")) {
            parkingLot.setDetailedLeftEntryPath(row, col);
        }
        else {
            parkingLot.setDetailedEntryPath(row, col);
        }
        parkingLot.showDetailedPath();
        parkingLot.removeDirections();
    }

    public CarLocation checkAndSuggestLastCarParkingPlace(Car car){
        CarEntryExit carEntryExit = checkAndGetLastCarEntryExit(car);
        if(carEntryExit == null) return null;
        CarLocation pos = carEntryExit.getPosition();
        ParkingLot parkingLot = arr.get(pos.getFloorNo());

        dataPrinter.LastCarParkingDetails(pos,carEntryExit);

        if(obj.getLowestFloorWithVacancy()<pos.getFloorNo()) {
            System.out.println("\nBut Empty Parking Place is available at lower floor");
            return null;
        }

        CarLocation position = new CarLocation();
        CarParkingPlace position1 = parkingLot.getNearestParkingPosition(pos.getCarParkingPlace().getRow()-1,
                pos.getCarParkingPlace().getCol()-1);
        position.setCarParkingPlace(position1);
        ParkingCell parkingCell = parkingLot.getParkingCellByPosition(position1.getRow(),
                position1.getCol());
        position.setCarParkingSpotNumber(parkingCell.getPosition());
        position.setFloorNo(pos.getFloorNo());
        if(position1 != null) {
            dataPrinter.emptyCarParkingPlace(position);
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
        else {
            dataPrinter.parkingIsFullInFloor(pos.getFloorNo());
        }
        return null;
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
                    dataPrinter.printParkingAvailableFloors(obj);
                    String floorNumber = dataProvider.getFloorNumber();
                    int floorNo = Validator.validateInteger(floorNumber,0,obj.floors-1);
                    if(floorNo == -1) continue;
                    return arr.get(floorNo);
                }
            }
            else {
                dataPrinter.yesOrNoInvalidMessage();
            }
        }
    }

    public CarParkingPlace suggestAndGetParkingPlace(ParkingLot parkingLot) {
        int position = parkingLot.getFirstParkingPosition();
        while (true) {
            String choice = dataProvider.getSuggestedParkingPlaceConfirmation(position);
            if(choice.equalsIgnoreCase("yes")) {
                CarLocation carLocation = obj.getCarLocation(parkingLot,position);
                return carLocation.getCarParkingPlace();
            }
            else if(choice.equalsIgnoreCase("no")){
                while (true) {
                    String num = dataProvider.getCarParkingPlace(parkingLot);
                    int rows = obj.rows;
                    int columns = obj.columns;
                    int floorNo = parkingLot.getFloorNo();
                    int n = Validator.validateInteger(num,((rows*columns)*floorNo)+1,
                            (rows*columns)*(floorNo+1));
                    CarLocation location = obj.getCarLocation(parkingLot,n);
                    if(location == null) {
                        continue;
                    }
                    CarParkingPlace carParkingPlace = location.getCarParkingPlace();
                    if(parkingLot.isValidEmptyParkingPlace(carParkingPlace.getRow(), carParkingPlace.getCol())) {
                        return new CarParkingPlace(carParkingPlace.getRow(),carParkingPlace.getCol());
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

    public void parkACar(ParkingLot parkingLot, CarParkingPlace position, Car car) {
        parkingLot.parkCarAtPosition(car, position.getRow(), position.getCol());

        carInParking.addCars(car);

        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        ParkingCell parkingCell = parkingLot.getParkingCellByPosition(position.getRow(),position.getCol());
        CarEntryExit carEntryExit = new CarEntryExit(parkingCell.getParkedTime(),
                new CarLocation(position.getRow()+1,position.getCol()+1,parkingCell.getPosition(),
                        parkingLot.getFloorNo()));
        carEntryExitMaster.addEntryExit(carEntryExit);
        carEntryExitMaster.addBilling(carEntryExit.getBilling());
    }

    public void generatePathToParkACar(ParkingLot parkingLot, CarParkingPlace position) {
        System.out.println("\nDetailed Path to park the car in the given parking place " +
                "at " + OrdinalNumber.getOrdinalNo(parkingLot.getFloorNo()) + " floor");
        showPathToParkACar(parkingLot,position.getRow(),position.getCol());
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
