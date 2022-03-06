package service;

import model.ParkingLot;

public class CarParkingImpl implements CarParking{

    @Override
    public void showPathToParkACar(String path, ParkingLot parkingLot, int row, int col) {
        if(path.equals("L")) {
            parkingLot.setDetailedLeftEntryPath(row, col);
        }
        else {
            parkingLot.setDetailedEntryPath(row, col);
        }
        parkingLot.showDetailedPath();
        parkingLot.removeDirections();
    }

    @Override
    public void showPathToExitACar(String path, ParkingLot parkingLot, int row, int col) {
        if(path.equals("L")) {
            parkingLot.setDetailedLeftExitPath(row, col);
        }
        else {
            parkingLot.setDetailedExitPath(row, col);
        }
        parkingLot.showDetailedPath();
        parkingLot.removeDirections();
    }

}
