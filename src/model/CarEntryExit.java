package model;

import java.time.LocalTime;

public class CarEntryExit {

    private final LocalTime entryTime;
    private LocalTime exitTime;
    private final CarLocation position;
    private int carParkingSpotNumber;
    private BillingSystem billing;

    public CarEntryExit(LocalTime entryTime, CarLocation position) {
        this.entryTime = entryTime;
        this.position = position;
        billing = new BillingSystem(entryTime);
    }

    public void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    public LocalTime getEntryTime() {
        return entryTime;
    }

    public LocalTime getExitTime() {
        return exitTime;
    }

    public CarLocation getPosition() {
        return position;
    }

    public BillingSystem getBilling() {
        return billing;
    }

    public void setBilling(BillingSystem billing) {
        this.billing = billing;
    }

    public int getCarParkingSpotNumber() {
        return carParkingSpotNumber;
    }

    public void setCarParkingSpotNumber(int carParkingSpotNumber) {
        this.carParkingSpotNumber = carParkingSpotNumber;
    }
}
