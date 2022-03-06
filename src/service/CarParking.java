package service;

import model.ParkingLot;

public interface CarParking {

    void showPathToParkACar(String path, ParkingLot parkingLot, int row, int col);

    void showPathToExitACar(String path, ParkingLot parkingLot, int row, int col);

}
