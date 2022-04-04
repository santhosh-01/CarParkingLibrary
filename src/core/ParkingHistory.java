package core;

import java.time.LocalTime;
import java.util.ArrayList;

public class ParkingHistory {

    private final CarTable carTable;
    private final DataProvider dataProvider;
    private final DataPrinter dataPrinter;
    private final CarEntryExitTable carEntryExitTable;

    public ParkingHistory(CarTable carTable, DataProvider dataProvider, DataPrinter dataPrinter, CarEntryExitTable carEntryExitTable) {
        this.carTable = carTable;
        this.dataProvider = dataProvider;
        this.dataPrinter = dataPrinter;
        this.carEntryExitTable = carEntryExitTable;
    }

    public Car getValidCarInParkingHistory() {
        return carTable.getCarByCarNo(getValidCarNumberInParkingHistory());
    }

    public String getValidCarNumberInParkingHistory() {
        while (true) {
            String carNumber = dataProvider.getCarNumberForHistory();
            if(carNumber.equalsIgnoreCase("b")) return null;
            if(carNumber.equals("")) {
                dataPrinter.givenCarNumberEmpty();
                dataPrinter.askingBackToMainMenu();
                continue;
            }
            if(!carTable.isCarNumberExist(carNumber)) {
                dataPrinter.givenCarNotInParking();
                dataPrinter.askingBackToMainMenu();
                continue;
            }
            return carNumber;
        }
    }

    public boolean showCarParkingHistory(Car car) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        if(carEntryExitMaster == null) {
            return false;
        }
        for (CarEntryExit carEntryExit:carEntryExitMaster.getCarEntryExits()) {
            LocalTime time1 = carEntryExit.getEntryTime();
            LocalTime time2 = carEntryExit.getExitTime();
            CarLocation pos = carEntryExit.getPosition();

            dataPrinter.carParkingHistory(time1,time2,pos,carEntryExit);
        }
        return true;
    }

    public CarEntryExit getLastCarEntryExitByCar(ParkingCell parkingCell, Car car) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(car.getCarNumber());
        CarEntryExit carEntryExit = carEntryExitMaster.getLastCarEntryExit();
        carEntryExit.setExitTime(parkingCell.getCarExitTime());
        return carEntryExit;
    }

    public ArrayList<BillingSystem> getBillingsByCarNo(String carNo) {
        CarEntryExitMaster carEntryExitMaster = carEntryExitTable.getCarByCarNumber(carNo);
        return carEntryExitMaster.getBillings();
    }

}
