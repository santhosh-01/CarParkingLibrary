package database;

import model.CarEntryExitMaster;

import java.util.ArrayList;

public class CarEntryExitTable {

    private final ArrayList<CarEntryExitMaster> cars = new ArrayList<>();

    public void addCar(CarEntryExitMaster car) {
        cars.add(car);
    }

    public CarEntryExitMaster getCarByCarNumber(String carNo) {
        for (CarEntryExitMaster car:cars) {
            if(car.getCarNumber().equals(carNo)) return car;
        }
        return null;
    }

}
