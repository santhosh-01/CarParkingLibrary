package service;

import model.*;

import java.time.LocalTime;

public interface DataPrinter {

    void showCarDetails(String carNo, String carBrand, String carModel);

    void CarParkingCancelledMessage();

    void LastCarParkingDetails(CarLocation pos, CarEntryExit carEntryExit);

    void emptyCarParkingPlace(CarLocation pos);

    void parkingConfirmation();

    void yesOrNoInvalidMessage();

    void parkingIsFullInFloor(int floor);

    void printParkingAvailableFloors(MultiFloorCarParking obj);

    void askCorrectFormatOfParkingPlace();

    void invalidParkingPlace();

    void noCarsAvailable();

    void carNumberNotFound();

    void givenCarNotInParking();

    void askingBackToMainMenu();

    void carParkingHistory(LocalTime time1, LocalTime time2, CarLocation pos, CarEntryExit carEntryExit);

    void showCarInformation(Car car);

    void duplicateCarExist();

    void givenCarNumberEmpty();

}
