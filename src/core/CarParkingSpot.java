package core;

// Model Class
public class CarParkingSpot {

    private final int row;
    private final int col;
    private int carParkingSpotNumber;

    protected CarParkingSpot(int row, int col) {
        this.row = row;
        this.col = col;
    }

    protected CarParkingSpot(int row, int col, int carParkingSpotNumber) {
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

    protected void setCarParkingSpotNumber(int carParkingSpotNumber) {
        this.carParkingSpotNumber = carParkingSpotNumber;
    }
}
