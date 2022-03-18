package service;

import database.CarInParking;
import model.Car;
import model.CarParkingPlace;
import model.ParkingCell;
import model.ParkingLot;

public class CarExit {

    private final CarInParking carInParking;

    public CarExit(CarInParking carInParking) {
        this.carInParking = carInParking;
    }

    public ParkingCell exitACarFromPosition(ParkingLot parkingLot, CarParkingPlace pos, Car car) {
        ParkingCell parkingCell = parkingLot.exitACar(pos);
        carInParking.removeCar(car.getCarNumber());
        return parkingCell;
    }

}
