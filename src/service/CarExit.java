package service;

import database.CarInParking;
import model.*;
import util.OrdinalNumber;

public class CarExit {

    public ParkingCell exitACarFromPosition(CarInParking carInParking, ParkingLot parkingLot, CarParkingPlace pos, Car car) {
        ParkingCell parkingCell = parkingLot.exitACar(pos);
        carInParking.removeCar(car.getCarNumber());
        return parkingCell;
    }

    public void generatePathToExitACar(MultiFloorCarParking obj, ParkingLot parkingLot, CarParkingPlace pos) {
        System.out.println("\nDetailed Path to exit the car from the parking place " +
                "at " + OrdinalNumber.getOrdinalNo(parkingLot.getFloorNo()) + " floor");
        showPathToExitACar(obj, parkingLot,pos.getRow(),pos.getCol());
    }

    private void showPathToExitACar(MultiFloorCarParking obj, ParkingLot parkingLot, int row, int col) {
        String path = obj.path;
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
