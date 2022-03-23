package service;

import database.CarEntryExitTable;
import model.*;

import java.util.ArrayList;

public class BillingFunctionalities {

    private final CarEntryExitTable carEntryExitTable;

    public BillingFunctionalities(CarEntryExitTable carEntryExitTable) {
        this.carEntryExitTable = carEntryExitTable;
    }

    public BillingSystem generateBill(ParkingCell parkingCell, Car car) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        CarEntryExit carEntryExit = carEntryExitMaster.getLastCarEntryExit();
        carEntryExit.setExitTime(parkingCell.getCarExitTime());
        BillingSystem billing = carEntryExit.getBilling();
        billing.setCarExitTime(parkingCell.getCarExitTime());
        return billing;
    }

    public ArrayList<BillingSystem> getBillingsByCarNo(String carNo) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(carNo);
        return carEntryExitMaster.getBillings();
    }

}
