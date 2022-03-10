package service;

import model.*;

import java.util.ArrayList;

public interface CarParking {

    Car acceptCarDetailsToPark();

    Car createCar(String carNo, String carBrand, String carModel);

    boolean confirmCarDetails(Car car);

    CarEntryExit checkAndGetLastCarParkingPlace(Car car);

    boolean checkAndSuggestLastCarParkingPlace(Car car);

    ParkingLot suggestAndGetCarParkingLot();

    int[] suggestAndGetParkingPlace(ParkingLot parkingLot);

    void parkACar(ParkingLot parkingLot, int[] position, Car car);

    Car acceptCarDetailsToExit();

    boolean confirmCarDetailsForExit(Car car);

    ParkingCell exitACarFromPosition(ParkingLot parkingLot, int[] pos, Car car);

    long calculateParkingTimeInSeconds(ParkingCell parkingCell);

    double generateBill(ParkingCell parkingCell, Car car, long seconds);

    void generatePathToParkACar(ParkingLot parkingLot, int[] position);

    void generatePathToExitACar(ParkingLot parkingLot, int[] pos);

    Car getValidCarNumberInParkingHistory();

    void showCarParkingHistory(Car car);

    boolean checkDuplicateCarNoInParking(String carNo);

}
