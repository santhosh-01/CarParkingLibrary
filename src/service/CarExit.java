package service;

import database.CarInParking;
import model.Car;
import model.CarParkingPlace;
import model.ParkingCell;
import model.ParkingLot;

public class CarExit {

    public CarExit() {
    }

    public ParkingCell exitACarFromPosition(CarInParking carInParking, ParkingLot parkingLot, CarParkingPlace pos, Car car) {
        ParkingCell parkingCell = parkingLot.exitACar(pos);
        carInParking.removeCar(car.getCarNumber());
        return parkingCell;
    }

}
