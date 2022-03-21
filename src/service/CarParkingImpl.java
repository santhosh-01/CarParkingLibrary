package service;

import database.CarEntryExitTable;
import database.CarInParking;
import database.CarTable;
import model.*;
import util.OrdinalNumber;
import util.Validator;

import java.time.LocalTime;
import java.util.ArrayList;

public class CarParkingImpl implements CarParking{

    private final MultiFloorCarParking obj;
    private final ArrayList<ParkingLot> arr;
    private final DataProvider dataProvider;
    private final DataPrinter dataPrinter;

    private final CarTable carTable;
    private final CarInParking carInParking;
    private final CarEntryExitTable carEntryExitTable;

    public CarParkingImpl(MultiFloorCarParking obj, DataProvider dataProvider, DataPrinter dataPrinter,
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

    private void showPathToExitACar(ParkingLot parkingLot, int row, int col) {
        String path = obj.path;
        if(path.equals("L")) {
            parkingLot.setDetailedLeftExitPath(row, col);
        }
        else {
            parkingLot.setDetailedExitPath(row, col);
        }
        parkingLot.showDetailedPath();
        parkingLot.removeDirections();
    }

    @Override
    public Car acceptCarDetailsToPark() {
        if (!dataProvider.billingAmountAcceptance()) return null;
        String carNo = validateAndGetCarNumber();
        String carBrand, carModel;
        Car car;
        if(carTable.isCarNumberExist(carNo)) {
            car = carTable.getCarByCarNo(carNo);
        }
        else {
            carBrand = validateAndGetCarBrand();
            carModel = validateAndGetCarModel();
            car = createCar(carNo,carBrand,carModel);
        }
        return car;
    }

    private String validateAndGetCarNumber() {
        String carNo;
        do {
            carNo = dataProvider.getCarNumber();
        } while (carNo == null);
        return carNo;
    }

    private String validateAndGetCarBrand() {
        String carBrand;
        do {
            carBrand = dataProvider.getCarBrand();
        } while (carBrand == null);
        return carBrand;
    }

    private String validateAndGetCarModel() {
        String carModel;
        do {
            carModel = dataProvider.getCarModel();
        } while (carModel == null);
        return carModel;
    }

    private Car createCar(String carNo, String carBrand, String carModel) {
        Car car;
        car = new Car(carNo,carBrand,carModel);
        carTable.addCar(car);
        return car;
    }

    @Override
    public boolean confirmCarDetails(Car car) {
        while (true) {
            String carNo = car.getCarNumber();
            String carBrand = car.getCarBrand();
            String carModel = car.getCarModel();
            dataPrinter.showCarDetails(carNo,carBrand,carModel);
            String ch = dataProvider.getCarConfirmation();
            int choice = Validator.validateInteger(ch,1,5);
            if(choice == -1) continue;
            if(choice == 1) {
                car.setCarNumber(validateAndGetCarNumber());
            }
            else if(choice == 2) {
                car.setCarBrand(validateAndGetCarBrand());
            }
            else if(choice == 3) {
                car.setCarModel(validateAndGetCarModel());
            }
            else if(choice == 4) break;
            else if(choice == 5) {
                dataPrinter.CarParkingCancelledMessage();
                return false;
            }
        }
        return true;
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

    @Override
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

    @Override
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

    @Override
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
                    int num = dataProvider.getCarParkingPlace(parkingLot);
                    CarLocation location = obj.getCarLocation(parkingLot,num);
                    if(location == null) {
                        System.out.println("\nGiven Car Position is Invalid");
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

    @Override
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

    @Override
    public Car acceptCarDetailsToExit() {
        if(!obj.isCarAvailable()) {
            dataPrinter.noCarsAvailable();
            return null;
        }
        return getValidCarFromParking();
    }

    private Car getValidCarFromParking() {
        while (true) {
            String carNo = dataProvider.getCarNumberToExit();
            if(carNo.equalsIgnoreCase("b")) return null;
            Car car = carTable.getCarByCarNo(carNo);
            if(car == null) {
                dataPrinter.carNumberNotFound();
            }
            else {
                if(carInParking.isCarNumberExist(carNo)) {
                    return car;
                }
                else {
                    dataPrinter.givenCarNotInParking();
                }
            }
            dataPrinter.askingBackToMainMenu();
        }
    }

    @Override
    public boolean confirmCarDetailsForExit(Car car) {
        while (true) {
            String choice = dataProvider.givenCarConfirmation(car);
            if(choice.equalsIgnoreCase("yes")) return true;
            else if(choice.equalsIgnoreCase("no")) {
                return false;
            }
            else {
                dataPrinter.yesOrNoInvalidMessage();
            }
        }
    }

    public ParkingCell exitACarFromPosition(ParkingLot parkingLot, CarParkingPlace pos, Car car) {
        ParkingCell parkingCell = parkingLot.exitACar(pos);
        carInParking.removeCar(car.getCarNumber());
        return parkingCell;
    }

    @Override
    public BillingSystem generateBill(ParkingCell parkingCell, Car car) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        CarEntryExit carEntryExit = carEntryExitMaster.getLastCarEntryExit();
        carEntryExit.setExitTime(parkingCell.getCarExitTime());
        BillingSystem billing = carEntryExit.getBilling();
        billing.setCarExitTime(parkingCell.getCarExitTime());
        return billing;
    }

    @Override
    public void generatePathToParkACar(ParkingLot parkingLot, CarParkingPlace position) {
        System.out.println("\nDetailed Path to park the car in the given parking place " +
                "at " + OrdinalNumber.getOrdinalNo(parkingLot.getFloorNo()) + " floor");
        showPathToParkACar(parkingLot,position.getRow(),position.getCol());
    }

    @Override
    public void generatePathToExitACar(ParkingLot parkingLot, CarParkingPlace pos) {
        System.out.println("\nDetailed Path to exit the car from the parking place " +
                "at " + OrdinalNumber.getOrdinalNo(parkingLot.getFloorNo()) + " floor");
        showPathToExitACar(parkingLot,pos.getRow(),pos.getCol());
    }

    @Override
    public Car getValidCarInParkingHistory() {
        return carTable.getCarByCarNo(getValidCarNumberInParkingHistory());
    }

    @Override
    public String getValidCarNumberInParkingHistory() {
        while (true) {
            String carNumber = dataProvider.getCarNumberForHistory();
            if(carNumber.equalsIgnoreCase("b")) return null;
            if(carNumber.equals("")) {
                dataPrinter.givenCarNumberEmpty();
                dataPrinter.askingBackToMainMenu();
                continue;
            }
            if(!carTable.isCarNumberExist(carNumber)) {
                dataPrinter.givenCarNotInParking();
                dataPrinter.askingBackToMainMenu();
                continue;
            }
            return carNumber;
        }
    }

    @Override
    public boolean showCarParkingHistory(Car car) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        if(carEntryExitMaster == null) {
            return false;
        }
        for (CarEntryExit carEntryExit:carEntryExitMaster.getCarEntryExits()) {
            LocalTime time1 = carEntryExit.getEntryTime();
            LocalTime time2 = carEntryExit.getExitTime();
            CarLocation pos = carEntryExit.getPosition();

            dataPrinter.carParkingHistory(time1,time2,pos,carEntryExit);
        }
        return true;
    }

    @Override
    public boolean checkDuplicateCarNoInParking(String carNo) {
        if(carInParking.isCarNumberExist(carNo)) {
            dataPrinter.duplicateCarExist();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public ArrayList<BillingSystem> getBillingsByCarNo(String carNo) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(carNo);
        return carEntryExitMaster.getBillings();
    }

}
