package model;

public class CarParkingPlace {

    private final int row;
    private final int col;
    private int carParkingSpotNumber;

    public CarParkingPlace(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public CarParkingPlace(int row, int col, int carParkingSpotNumber) {
        this.row = row;
        this.col = col;
        this.carParkingSpotNumber = carParkingSpotNumber;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getCarParkingSpotNumber() {
        return carParkingSpotNumber;
    }

    public void setCarParkingSpotNumber(int carParkingSpotNumber) {
        this.carParkingSpotNumber = carParkingSpotNumber;
    }
}
