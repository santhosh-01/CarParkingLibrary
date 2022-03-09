package service;

import model.*;

public interface CarParking {

    Car createCar(String carNo, String carBrand, String carModel);

    CarEntryExit checkAndGetLastCarParkingPlace(Car car);

    void parkACar(MultiFloorCarParking obj, ParkingLot parkingLot, int[] position, Car car);

    void exitACarFromPosition(MultiFloorCarParking obj, ParkingLot parkingLot, int[] pos, Car car);

    long calculateParkingTimeInSeconds(ParkingCell parkingCell);

    double generateBill(ParkingCell parkingCell, Car car, long seconds);

    void generatePathToParkACar(MultiFloorCarParking obj, ParkingLot parkingLot, int[] position);

    void generatePathToExitACar(MultiFloorCarParking obj, ParkingLot parkingLot, int[] pos);

}
