package core;

public class Car {

    private String carNumber;
    private String carBrand;
    private String carModel;

    Car(String carNumber, String carBrand, String carModel) {
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

    protected void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    protected void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    protected void setCarModel(String carModel) {
        this.carModel = carModel;
    }

}
