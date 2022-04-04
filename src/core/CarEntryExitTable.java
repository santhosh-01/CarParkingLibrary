package core;

import java.util.ArrayList;

// DB Class
public class CarEntryExitTable {

    private final ArrayList<CarEntryExitMaster> cars = new ArrayList<>();

    protected void addCar(CarEntryExitMaster car) {
        cars.add(car);
    }

    protected CarEntryExitMaster getCarByCarNumber(String carNo) {
        for (CarEntryExitMaster car:cars) {
            if(car.getCarNumber().equals(carNo)) return car;
        }
        return null;
    }

}
