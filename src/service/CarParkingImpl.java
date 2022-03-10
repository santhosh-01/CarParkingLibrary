package service;

import database.CarEntryExitTable;
import database.CarInParking;
import database.CarTable;
import model.*;
import util.OrdinalNumber;
import util.Validator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class CarParkingImpl implements CarParking{

    private final MultiFloorCarParking obj;
    private final ArrayList<ParkingLot> arr;
    private final DataProvider dataProvider;
    private final DataPrinter dataPrinter;

    private final CarInParking carInParking;
    private final CarEntryExitTable carEntryExitTable;

    public CarParkingImpl(MultiFloorCarParking obj, DataProvider dataProvider, DataPrinter dataPrinter) {
        this.obj = obj;
        this.arr = obj.getParkingLots();
        this.dataProvider = dataProvider;
        this.dataPrinter = dataPrinter;
        carInParking = new CarInParking();
        carEntryExitTable = new CarEntryExitTable();
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
        if (!dataProvider.acceptBillingAmount()) return null;
        String carNo = dataProvider.validateAndGetCarNumber();
        String carBrand, carModel;
        Car car;
        CarTable carTable = new CarTable();
        if(carTable.isCarNumberExist(carNo)) {
            car = carTable.getCarByCarNo(carNo);
        }
        else {
            carBrand = dataProvider.validateAndGetCarBrand();
            carModel = dataProvider.validateAndGetCarModel();
            car = createCar(carNo,carBrand,carModel);
        }
        return car;
    }

    @Override
    public Car createCar(String carNo, String carBrand, String carModel) {
        Car car;
        car = new Car(carNo,carBrand,carModel);
        CarTable carTable = new CarTable();
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
                car.setCarNumber(dataProvider.validateAndGetCarNumber());
            }
            else if(choice == 2) {
                car.setCarBrand(dataProvider.validateAndGetCarBrand());
            }
            else if(choice == 3) {
                car.setCarModel(dataProvider.validateAndGetCarModel());
            }
            else if(choice == 4) break;
            else if(choice == 5) {
                dataPrinter.CarParkingCancelledMessage();
                return false;
            }
        }
        return true;
    }

    @Override
    public CarEntryExit checkAndGetLastCarParkingPlace(Car car) {
        String carNumber = car.getCarNumber();
        CarEntryExitTable carEntryExitTable = new CarEntryExitTable();
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
    public boolean checkAndSuggestLastCarParkingPlace(Car car){
        CarEntryExit carEntryExit = checkAndGetLastCarParkingPlace(car);
        if(carEntryExit == null) return false;
        int[] pos = carEntryExit.getPosition();
        ParkingLot parkingLot = arr.get(pos[2]);

        dataPrinter.LastCarParkingDetails(pos,carEntryExit);

        int[] position = new int[3];
        int[] position1 = parkingLot.getNearestParkingPosition(pos[0]-1,pos[1]-1);
        position[0] = position1[0];
        position[1] = position1[1];
        position[2] = pos[2];
        if(position[0] != -1 && pos[1] != -1) {
            dataPrinter.emptyCarParkingPlace(position);
            dataPrinter.parkingConfirmation();
            while (true) {
                String choice = dataProvider.getLastCarParkingConfirmation();
                if(choice.equalsIgnoreCase("yes")) {
                    CarParking carParking = new CarParkingImpl(obj,dataProvider,dataPrinter);
                    dataPrinter.generateReceipt(carParking,obj,parkingLot,position,car);
                    return true;
                }
                else if(choice.equalsIgnoreCase("no")) {
                    return false;
                }
                else {
                    dataPrinter.yesOrNoInvalidMessage();
                }
            }
        }
        else {
            dataPrinter.parkingIsFullInFloor(pos[2]);
        }
        return false;
    }

    @Override
    public ParkingLot suggestAndGetCarParkingLot() {
        Scanner in = new Scanner(System.in);
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
    public int[] suggestAndGetParkingPlace(ParkingLot parkingLot) {
        int[] position = parkingLot.getFirstParkingPosition();
        while (true) {
            String choice = dataProvider.getSuggestedParkingPlaceConfirmation(position);
            if(choice.equalsIgnoreCase("yes")) {
                return position;
            }
            else if(choice.equalsIgnoreCase("no")){
                while (true) {
                    String str = dataProvider.getCarParkingPlace(parkingLot);
                    if(!str.contains("/")) {
                        dataPrinter.askCorrectFormatOfParkingPlace();
                        continue;
                    }
                    String[] pos = str.split("/");
                    position[0] = Validator.validateInteger(pos[0],"rows") - 1;
                    if(position[0] == -2) continue;
                    position[1] = Validator.validateInteger(pos[1],"columns") - 1;
                    if(position[1] == -2) continue;
                    if(parkingLot.isValidEmptyParkingPlace(position[0], position[1])) {
                        return position;
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
    public void parkACar(ParkingLot parkingLot, int[] position, Car car) {
        parkingLot.parkCarAtPosition(car, position[0], position[1]);

        CarEntryExitTable carEntryExitTable = new CarEntryExitTable();

        CarInParking carInParking = new CarInParking();
        carInParking.addCars(car);

        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        ParkingCell parkingCell = parkingLot.getParkingCellByPosition(position[0],position[1]);
        CarEntryExit carEntryExit = new CarEntryExit(parkingCell.getParkedTime(),
                new int[]{position[0]+1,position[1]+1,parkingLot.getFloorNo()});
        carEntryExitMaster.addEntryExit(carEntryExit);
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
            Scanner in = new Scanner(System.in);
            String carNo = dataProvider.getCarNumberToExit();
            if(carNo.equalsIgnoreCase("back")) return null;
            CarTable carTable = new CarTable();
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
            Scanner in = new Scanner(System.in);
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

    @Override
    public ParkingCell exitACarFromPosition(ParkingLot parkingLot, int[] pos, Car car) {
        ParkingCell parkingCell = parkingLot.exitACar(pos);
        CarInParking carInParking = new CarInParking();
        carInParking.removeCar(car.getCarNumber());
        return parkingCell;
    }

    @Override
    public long calculateParkingTimeInSeconds(ParkingCell parkingCell) {
        return parkingCell.calculateCarParkedInSeconds();
    }

    @Override
    public String generateBill(ParkingCell parkingCell, Car car, long seconds) {
        CarEntryExitTable carEntryExitTable = new CarEntryExitTable();
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        CarEntryExit carEntryExit = carEntryExitMaster.getLastCarEntryExit();
        carEntryExit.setExitTime(parkingCell.getCarExitTime());
        Billing billing = carEntryExit.getBilling();
        billing.setCarExitTime(parkingCell.getCarExitTime(),seconds);
        return String.format("\nBill Amount for Parking: %.2f " + Billing.moneyAbbr + "\n", billing.getBill());
    }

    @Override
    public void generatePathToParkACar(ParkingLot parkingLot, int[] position) {
        System.out.println("\nDetailed Path to park the car in the given parking place " +
                "at " + getOrdinalNumber(parkingLot.getFloorNo()) + " floor");
        showPathToParkACar(parkingLot,position[0],position[1]);
    }

    @Override
    public void generatePathToExitACar(ParkingLot parkingLot, int[] pos) {
        System.out.println("\nDetailed Path to exit the car from the parking place " +
                "at " + getOrdinalNumber(parkingLot.getFloorNo()) + " floor");
        showPathToExitACar(parkingLot,pos[0],pos[1]);
    }

    @Override
    public Car getValidCarNumberInParkingHistory() {
        while (true) {
            Scanner in = new Scanner(System.in);
            String carNumber = dataProvider.getCarNumberForCarHistory();
            if(carNumber.equalsIgnoreCase("back")) return null;
            CarTable carTable = new CarTable();
            if(carNumber.equals("")) {
                System.out.println("\nSorry! Given Car Number doesn't found! Please enter valid " +
                        "Car Number which is in parking");
                System.out.println("If you want to move back to main menu, Enter 'back'");
                continue;
            }
            if(!carTable.isCarNumberExist(carNumber)) {
                System.out.println("\nSorry! Given Car not found!! Please enter valid Car Number");
                System.out.println("If you want to move back to main menu, Enter 'back'");
                continue;
            }
            return carTable.getCarByCarNo(carNumber);
        }
    }

    @Override
    public void showCarParkingHistory(Car car) {
        System.out.println("\nParking History:");
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        if(carEntryExitMaster == null) {
            System.out.println("No Parking History Available");
            return;
        }
        for (CarEntryExit carEntryExit:carEntryExitMaster.getCarEntryExits()) {
            LocalTime time1 = carEntryExit.getEntryTime();
            LocalTime time2 = carEntryExit.getExitTime();
            int[] pos = carEntryExit.getPosition();

            dataPrinter.carParkingHistory(time1,time2,pos,carEntryExit);
        }
    }

    @Override
    public boolean checkDuplicateCarNoInParking(String carNo) {
        CarInParking carInParking = new CarInParking();
        if(carInParking.isCarNumberExist(carNo)) {
            dataPrinter.duplicateCarExist();
            return true;
        }
        else {
            return false;
        }
    }

    private String getOrdinalNumber(int floorNo) {
        if(floorNo == 0) return "Ground";
        else return OrdinalNumber.getOrdinalNo(floorNo);
    }

    private String getTime(LocalTime time) {
        return time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }

}
