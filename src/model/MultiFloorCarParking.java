package model;

import java.util.ArrayList;

public class MultiFloorCarParking {

    public int floors, rows, columns, pathWidth;
    public String path;
    private final ArrayList<ParkingLot> parkingLots;

    public MultiFloorCarParking(PropertiesDataClass prop) {
        floors = prop.getFloors();
        rows = prop.getRows();
        columns = prop.getColumns();
        pathWidth = prop.getPathWidth();
        path = prop.getPath();
        BillingSystem.setMoneyType(MoneyType.INR);
        BillingSystem.billingAmountPerHour = prop.getBillingAmountPerHour();
        parkingLots = new ArrayList<>();
        setParkingLots();
    }

    private void setParkingLots() {
        for(int i = 0; i < floors; ++i) {
            ParkingLot parkingLot = new ParkingLot(i, rows, columns, pathWidth);
            parkingLots.add(parkingLot);
        }
    }

    public CarLocation getCarLocation(ParkingLot parkingLot, int carParkingNumber) {
        return parkingLot.getCarLocation(carParkingNumber);
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public boolean isParkingAvailable() {
        for (int i = 0; i < floors; ++i) {
            if (parkingLots.get(i).getVacancy() != 0) {
                return true;
            }
        }
        return false;
    }

    public int getLowestFloorWithVacancy() {
        for (int i = 0; i < floors; ++i) {
            if (parkingLots.get(i).getVacancy() != 0) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Integer> getParkingAvailableFloors() {
        ArrayList<Integer> arr = new ArrayList<>();
        for (int j = 0; j < parkingLots.size(); ++j) {
            if(!parkingLots.get(j).isParkingFull()) {
                arr.add(j);
            }
        }
        return arr;
    }

    public boolean isCarAvailable() {
        int maxVacancy = rows * columns;
        for (int i = 0; i < floors; ++i) {
            if (parkingLots.get(i).getVacancy() != maxVacancy) {
                return true;
            }
        }
        return false;
    }

    public ParkingLot getParkingLotWithCarNumber(String carNo) {
        for (ParkingLot parkingLot:parkingLots) {
            CarParkingPlace pos = parkingLot.getCarNumberPosition(carNo);
            if(pos != null) {
                return parkingLot;
            }
        }
        return null;
    }
}
