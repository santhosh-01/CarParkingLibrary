package service;

import database.CarEntryExitTable;
import database.CarInParking;
import database.CarTable;
import model.*;
import util.OrdinalNumber;

public class CarParkingImpl implements CarParking{

    private void showPathToParkACar(String path, ParkingLot parkingLot, int row, int col) {
        if(path.equals("L")) {
            parkingLot.setDetailedLeftEntryPath(row, col);
        }
        else {
            parkingLot.setDetailedEntryPath(row, col);
        }
        parkingLot.showDetailedPath();
        parkingLot.removeDirections();
    }

    private void showPathToExitACar(String path, ParkingLot parkingLot, int row, int col) {
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
    public Car createCar(String carNo, String carBrand, String carModel) {
        Car car;
        car = new Car(carNo,carBrand,carModel);
        CarTable carTable = new CarTable();
        carTable.addCar(car);
        return car;
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
    public void parkACar(MultiFloorCarParking obj, ParkingLot parkingLot, int[] position, Car car) {
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
    public void exitACarFromPosition(MultiFloorCarParking obj, ParkingLot parkingLot, int[] pos, Car car) {
        parkingLot.exitACar(pos);

        CarInParking carInParking = new CarInParking();
        carInParking.removeCar(car.getCarNumber());
    }

    @Override
    public long calculateParkingTimeInSeconds(ParkingCell parkingCell) {
        return parkingCell.calculateCarParkedInSeconds();
    }

    @Override
    public double generateBill(ParkingCell parkingCell, Car car, long seconds) {
        CarEntryExitTable carEntryExitTable = new CarEntryExitTable();
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        CarEntryExit carEntryExit = carEntryExitMaster.getLastCarEntryExit();
        carEntryExit.setExitTime(parkingCell.getCarExitTime());
        Billing billing = carEntryExit.getBilling();
        billing.setCarExitTime(parkingCell.getCarExitTime(),seconds);
        return billing.getBill();
    }

    @Override
    public void generatePathToParkACar(MultiFloorCarParking obj, ParkingLot parkingLot, int[] position) {
        System.out.println("\nDetailed Path to park the car in the given parking place " +
                "at " + getOrdinalNumber(parkingLot.getFloorNo()) + " floor");
        showPathToParkACar(obj.path,parkingLot,position[0],position[1]);
    }

    @Override
    public void generatePathToExitACar(MultiFloorCarParking obj, ParkingLot parkingLot, int[] pos) {
        System.out.println("\nDetailed Path to exit the car from the parking place " +
                "at " + getOrdinalNumber(parkingLot.getFloorNo()) + " floor");
        showPathToExitACar(obj.path,parkingLot,pos[0],pos[1]);
    }


    private String getOrdinalNumber(int floorNo) {
        if(floorNo == 0) return "Ground";
        else return OrdinalNumber.getOrdinalNo(floorNo);
    }

}
