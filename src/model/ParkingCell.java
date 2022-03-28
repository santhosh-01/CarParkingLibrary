package model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ParkingCell extends Cell{

    private Car car;
    private LocalTime parkedTime;
    private LocalTime carExitTime;
    private String isParked;

    public ParkingCell(ParkingCell parkingCell) {
        this.car = parkingCell.getCar();
        this.parkedTime = parkingCell.getParkedTime();
        this.carExitTime = parkingCell.getCarExitTime();
        this.isParked = parkingCell.getIsParked();
    }

    public ParkingCell() {
        super.setCellValue("-");
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
        if(car != null) {
            this.setCellValue(car.getCarNumber());
        }
        else {
            this.setCellValue("-");
        }
    }

    public LocalTime getParkedTime() {
        return parkedTime;
    }

    public void setParkedTime(LocalTime parkedTime) {
        this.parkedTime = parkedTime;
    }

    public void setCarExitTime(LocalTime carExitTime) {
        this.carExitTime = carExitTime;
    }

    public String getIsParked() {
        return isParked;
    }

    public void setIsParked(String isParked) {
        this.isParked = isParked;
    }

    public LocalTime getCarExitTime() {
        return carExitTime;
    }

}
