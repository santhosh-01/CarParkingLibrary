package core;

import java.time.LocalTime;
import java.util.ArrayList;

public interface DataPrinter {

    void carDetailsConfirmation(String carInfoUpdateMenu);

    void CarParkingCancelledMessage();

    void LastCarParkingDetails(int carParkingSpotNumber, int floorNo, CarEntryExit carEntryExit);

    void emptyCarParkingPlace(int carParkingSpotNumber, int floorNo);

    void parkingConfirmation();

    void yesOrNoInvalidMessage();

    void parkingIsFullInFloor(int floor);

    void printParkingAvailableFloors(ArrayList<Integer> indexArray);

    void invalidParkingPlace();

    void noCarsAvailable();

    void carNumberNotFound();

    void givenCarNotInParking();

    void askingBackToMainMenu();

    void carParkingHistory(LocalTime time1, LocalTime time2, CarLocation pos, CarEntryExit carEntryExit);

    void duplicateCarExist();

    void givenCarNumberEmpty();

}
