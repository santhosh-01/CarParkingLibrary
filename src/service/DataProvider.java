package service;

import model.Car;
import model.ParkingLot;

public interface DataProvider {

    boolean acceptBillingAmount();

    String validateAndGetCarModel();

    String validateAndGetCarBrand();

    String validateAndGetCarNumber();

    String getCarConfirmation();

    String getLastCarParkingConfirmation();

    String getSuggestedParkingFloorConfirmation(int floorNo);

    String getFloorNumber();

    String getSuggestedParkingPlaceConfirmation(int[] position);

    String getCarParkingPlace(ParkingLot parkingLot);

    String getCarNumberToExit();

    String givenCarConfirmation(Car car);

    String getBackChoice();

    String getCarNumberForHistory();

}
