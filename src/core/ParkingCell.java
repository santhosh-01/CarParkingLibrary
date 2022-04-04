package core;

import java.time.LocalTime;

// Model Class
public class ParkingCell extends Cell{

    private final int parkingSpotNumber;
    private Car car;
    private LocalTime parkedTime;
    private LocalTime carExitTime;
    private String isParked;

    protected ParkingCell(ParkingCell parkingCell) {
        this.parkingSpotNumber = -1;
        this.car = parkingCell.getCar();
        this.parkedTime = parkingCell.getParkedTime();
        this.carExitTime = parkingCell.getCarExitTime();
        this.isParked = parkingCell.getIsParked();
    }

    protected ParkingCell(int parkingSpotNumber) {
        this.parkingSpotNumber = parkingSpotNumber;
        super.setCellValue("-");
    }

    public int getParkingSpotNumber() {
        return parkingSpotNumber;
    }

    protected Car getCar() {
        return car;
    }

    protected LocalTime getParkedTime() {
        return parkedTime;
    }

    protected String getIsParked() {
        return isParked;
    }

    protected LocalTime getCarExitTime() {
        return carExitTime;
    }

    // Setters
    protected void setCar(Car car) {
        this.car = car;
        if(car != null) {
            this.setCellValue(car.getCarNumber());
        }
        else {
            this.setCellValue("-");
        }
    }

    protected void setParkedTime(LocalTime parkedTime) {
        this.parkedTime = parkedTime;
    }

    protected void setCarExitTime(LocalTime carExitTime) {
        this.carExitTime = carExitTime;
    }

    protected void setIsParked(String isParked) {
        this.isParked = isParked;
    }

}