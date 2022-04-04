package core;

import java.util.ArrayList;

// DB Class
public class CarTable {

    private final ArrayList<Car> cars = new ArrayList<>();

    protected boolean isCarNumberExist(String carNo) {
        for (Car car:cars) {
            if(car.getCarNumber().equals(carNo)) {
                return true;
            }
        }
        return false;
    }

    protected void addCar(Car car) {
        cars.add(car);
    }

    protected Car getCarByCarNo(String carNo) {
        Car res = null;
        for (Car car:cars) {
            if(car.getCarNumber().equals(carNo)) {
                res = car;
            }
        }
        return res;
    }

}
