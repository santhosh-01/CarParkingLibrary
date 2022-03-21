package model;

public class CarLocation {

    private CarParkingPlace carParkingPlace;
    private int floorNo;

    public CarLocation() {
    }

    public CarLocation(CarParkingPlace carParkingPlace, int floorNo) {
        this.carParkingPlace = carParkingPlace;
        this.floorNo = floorNo;
    }

    public CarLocation(int row, int col, int carParkingSpotNumber, int floorNo) {
        this.carParkingPlace = new CarParkingPlace(row,col,carParkingSpotNumber);
        this.floorNo = floorNo;
    }

    public CarParkingPlace getCarParkingPlace() {
        return carParkingPlace;
    }

    public void setCarParkingPlace(CarParkingPlace carParkingPlace) {
        this.carParkingPlace = carParkingPlace;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }
}
