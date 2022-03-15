package service;

import model.Car;
import model.ParkingLot;

public interface DataProvider {

    boolean billingAmountAcceptance();

    String getCarModel();

    String getCarBrand();

    String getCarNumber();

    String getCarDetailsConfirmation();

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
