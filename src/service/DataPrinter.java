package service;

import model.Car;
import model.CarEntryExit;
import model.MultiFloorCarParking;
import model.ParkingLot;

import java.time.LocalTime;

public interface DataPrinter {

    void showCarDetails(String carNo, String carBrand, String carModel);

    void CarParkingCancelledMessage();

    void LastCarParkingDetails(int[] pos, CarEntryExit carEntryExit);

    void emptyCarParkingPlace(int[] pos);

    void parkingConfirmation();

    void yesOrNoInvalidMessage();

    void parkingIsFullInFloor(int floor);

    void printParkingAvailableFloors(MultiFloorCarParking obj);

    void askCorrectFormatOfParkingPlace();

    void invalidParkingPlace();

    void generateReceipt(CarParking carParking, MultiFloorCarParking obj, ParkingLot parkingLot,
                         int[] pos, Car car);

    void noCarsAvailable();

    void carNumberNotFound();

    void givenCarNotInParking();

    void askingBackToMainMenu();

    void carParkingHistory(LocalTime time1, LocalTime time2, int[] pos, CarEntryExit carEntryExit);

    void showCarInformation(Car car);

    void duplicateCarExist();

    void givenCarNumberEmpty();

}
