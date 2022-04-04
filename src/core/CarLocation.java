package core;

// Model Class
public class CarLocation {

    private final CarParkingSpot carParkingSpot;
    private int floorNo;

    protected CarLocation(CarParkingSpot carParkingPlace, int floorNo) {
        this.carParkingSpot = carParkingPlace;
        this.floorNo = floorNo;
    }

    protected CarLocation(int row, int col, int carParkingSpotNumber, int floorNo) {
        this.carParkingSpot = new CarParkingSpot(row,col,carParkingSpotNumber);
        this.floorNo = floorNo;
    }

    public CarParkingSpot getCarParkingSpot() {
        return carParkingSpot;
    }

    public int getFloorNo() {
        return floorNo;
    }

    /*void setCarParkingSpot(CarParkingSpot carParkingSpot) {
        this.carParkingSpot = carParkingSpot;
    }*/

    protected void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }

    /*void setCarParkingSpotNumber(int position) {
        this.carParkingSpot.setCarParkingSpotNumber(position);
    }*/
}
