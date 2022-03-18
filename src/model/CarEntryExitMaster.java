package model;

import java.util.ArrayList;

public class CarEntryExitMaster {

    private final String carNumber;
    private final ArrayList<CarEntryExit> carEntryExits;
    private final ArrayList<BillingSystem> billings;

    public CarEntryExitMaster(String carNumber) {
        this.carNumber = carNumber;
        this.carEntryExits = new ArrayList<>();
        this.billings = new ArrayList<>();
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void addEntryExit(CarEntryExit car) {
        carEntryExits.add(car);
    }

    public ArrayList<CarEntryExit> getCarEntryExits() {
        return carEntryExits;
    }

    public CarEntryExit getLastCarEntryExit() {
        return carEntryExits.get(carEntryExits.size() - 1);
    }

    public void addBilling(BillingSystem billing) {
        this.billings.add(billing);
    }

    public ArrayList<BillingSystem> getBillings() {
        return billings;
    }
}
