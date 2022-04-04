package core;

import java.util.ArrayList;

// DB Class
public class CarInParking {

    private final ArrayList<Car> cars = new ArrayList<>();

    protected void addCars(Car car) {
        cars.add(car);
    }

    protected boolean isCarNumberExist(String carNo) {
        for (Car car:cars) {
            if(car.getCarNumber().equals(carNo)) {
                return true;
            }
        }
        return false;
    }

    protected void removeCar(String carNo) {
        for (Car car:cars) {
            if(car.getCarNumber().equals(carNo)) {
                cars.remove(car);
                return;
            }
        }
    }
}
