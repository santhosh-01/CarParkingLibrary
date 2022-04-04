package core;

import java.util.ArrayList;

// Model Class
public class CarEntryExitMaster {

    private final String carNumber;
    private final ArrayList<CarEntryExit> carEntryExits;
    private final ArrayList<BillingSystem> billings;

    protected CarEntryExitMaster(String carNumber) {
        this.carNumber = carNumber;
        this.carEntryExits = new ArrayList<>();
        this.billings = new ArrayList<>();
    }

    protected String getCarNumber() {
        return carNumber;
    }

    protected ArrayList<CarEntryExit> getCarEntryExits() {
        return carEntryExits;
    }

    protected CarEntryExit getLastCarEntryExit() {
        return carEntryExits.get(carEntryExits.size() - 1);
    }

    protected ArrayList<BillingSystem> getBillings() {
        return billings;
    }

    protected void addEntryExit(CarEntryExit car) {
        carEntryExits.add(car);
    }

    protected void addBilling(BillingSystem billing) {
        this.billings.add(billing);
    }
}
