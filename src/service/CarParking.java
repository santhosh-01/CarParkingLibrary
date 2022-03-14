package service;

import model.*;

import java.util.ArrayList;

public interface CarParking {

    Car acceptCarDetailsToPark();

    boolean confirmCarDetails(Car car);

    boolean checkAndSuggestLastCarParkingPlace(Car car);

    ParkingLot suggestAndGetCarParkingLot();

    int[] suggestAndGetParkingPlace(ParkingLot parkingLot);

    void parkACar(ParkingLot parkingLot, int[] position, Car car);

    Car acceptCarDetailsToExit();

    boolean confirmCarDetailsForExit(Car car);

    ParkingCell exitACarFromPosition(ParkingLot parkingLot, int[] pos, Car car);

    Billing generateBill(ParkingCell parkingCell, Car car);

    void generatePathToParkACar(ParkingLot parkingLot, int[] position);

    void generatePathToExitACar(ParkingLot parkingLot, int[] pos);

    Car getValidCarInParkingHistory();

    String getValidCarNumberInParkingHistory();

    boolean showCarParkingHistory(Car car);

    boolean checkDuplicateCarNoInParking(String carNo);

    ArrayList<Billing> getBillingsByCarNo(String carNo);

}
