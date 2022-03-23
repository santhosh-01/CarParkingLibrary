package service;

import database.CarEntryExitTable;
import database.CarTable;
import model.Car;
import model.CarEntryExit;
import model.CarEntryExitMaster;
import model.CarLocation;

import java.time.LocalTime;

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

}
