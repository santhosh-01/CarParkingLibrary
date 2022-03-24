package model;

public class Car {

    private String carNumber;
    private String carBrand;
    private String carModel;

    public Car(String carNumber, String carBrand, String carModel) {
        this.carNumber = carNumber;
        this.carBrand = carBrand;
        this.carModel = carModel;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

}
