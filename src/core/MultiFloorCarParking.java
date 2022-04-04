package core;

import java.util.ArrayList;

// Model Class
public class MultiFloorCarParking {

    private final int floors, rows, columns, pathWidth;
    private final String driverSide;
    private final ArrayList<ParkingLot> parkingLots;

    public MultiFloorCarParking(AppData appData) {
        floors = appData.getFloors();
        rows = appData.getRows();
        columns = appData.getColumns();
        pathWidth = appData.getPathWidth();
        driverSide = appData.getDrivingSide();
        BillingSystem billingSystem = new BillingSystem();
        billingSystem.setMoneyType(MoneyType.INR);
        billingSystem.setBillingAmountPerHour(appData.getBillingAmountPerHour());
        parkingLots = new ArrayList<>();
        setParkingLots();
    }

    public int getFloors() {
        return floors;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public String getDriverSide() {
        return driverSide;
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    protected ArrayList<Integer> getParkingAvailableFloors() {
        ArrayList<Integer> arr = new ArrayList<>();
        for (int j = 0; j < parkingLots.size(); ++j) {
            if(!parkingLots.get(j).isParkingFull()) {
                arr.add(j);
            }
        }
        return arr;
    }

    public ParkingLot getParkingLotWithCarNumber(String carNo) {
        for (ParkingLot parkingLot:parkingLots) {
            CarParkingSpot pos = parkingLot.getCarNumberParkingSpot(carNo);
            if(pos != null) {
                return parkingLot;
            }
        }
        return null;
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

    public boolean isCarAvailable() {
        int maxVacancy = rows * columns;
        for (int i = 0; i < floors; ++i) {
            if (parkingLots.get(i).getVacancy() != maxVacancy) {
                return true;
            }
        }
        return false;
    }

    private void setParkingLots() {
        for(int i = 0; i < floors; ++i) {
            ParkingLot parkingLot = new ParkingLot(i, rows, columns, pathWidth);
            parkingLots.add(parkingLot);
        }
    }
}
