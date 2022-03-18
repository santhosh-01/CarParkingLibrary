package service;

import model.*;

import java.util.ArrayList;

public interface CarParking {

    Car acceptCarDetailsToPark();

    boolean confirmCarDetails(Car car);

    CarLocation checkAndSuggestLastCarParkingPlace(Car car);

    ParkingLot suggestAndGetCarParkingLot();

    CarParkingPlace suggestAndGetParkingPlace(ParkingLot parkingLot);

    void parkACar(ParkingLot parkingLot, CarParkingPlace position, Car car);

    Car acceptCarDetailsToExit();

    boolean confirmCarDetailsForExit(Car car);

    BillingSystem generateBill(ParkingCell parkingCell, Car car);

    void generatePathToParkACar(ParkingLot parkingLot, CarParkingPlace position);

    void generatePathToExitACar(ParkingLot parkingLot, CarParkingPlace pos);

    Car getValidCarInParkingHistory();

    String getValidCarNumberInParkingHistory();

    boolean showCarParkingHistory(Car car);

    boolean checkDuplicateCarNoInParking(String carNo);

    ArrayList<BillingSystem> getBillingsByCarNo(String carNo);

}
