package service;

import database.CarEntryExitTable;
import model.*;

import java.util.ArrayList;

public class BillingFunctionalities {

    private final CarEntryExitTable carEntryExitTable;

    public BillingFunctionalities(CarEntryExitTable carEntryExitTable) {
        this.carEntryExitTable = carEntryExitTable;
    }

    public BillingSystem generateBill(CarEntryExit carEntryExit, ParkingCell parkingCell, Car car) {
        BillingSystem billing = carEntryExit.getBilling();
        billing.setCarExitTime(parkingCell.getCarExitTime());
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>(arr1);
        return billing;
    }

    public ArrayList<BillingSystem> getBillingsByCarNo(String carNo) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(carNo);
        return carEntryExitMaster.getBillings();
    }

}
