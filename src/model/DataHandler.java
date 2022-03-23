package model;

import database.CarEntryExitTable;
import database.CarInParking;
import database.CarTable;
import service.DataPrinter;
import service.DataProvider;
import util.Validator;

import java.util.ArrayList;

public class DataHandler {

    private final MultiFloorCarParking obj;
    private final ArrayList<ParkingLot> arr;
    private final DataProvider dataProvider;
    private final DataPrinter dataPrinter;

    private final CarTable carTable;
    private final CarInParking carInParking;
    private final CarEntryExitTable carEntryExitTable;

    public DataHandler(MultiFloorCarParking obj, DataProvider dataProvider, DataPrinter dataPrinter,
                          CarTable carTable, CarInParking carInParking, CarEntryExitTable carEntryExitTable) {
        this.obj = obj;
        this.arr = obj.getParkingLots();
        this.dataProvider = dataProvider;
        this.dataPrinter = dataPrinter;
        this.carTable = carTable;
        this.carInParking = carInParking;
        this.carEntryExitTable = carEntryExitTable;
    }

    public Car acceptCarDetailsToPark() {
        if (!dataProvider.billingAmountAcceptance()) return null;
        String carNo = validateAndGetCarNumber();
        String carBrand, carModel;
        Car car;
        if(carTable.isCarNumberExist(carNo)) {
            car = carTable.getCarByCarNo(carNo);
        }
        else {
            carBrand = validateAndGetCarBrand();
            carModel = validateAndGetCarModel();
            car = createCar(carNo,carBrand,carModel);
        }
        return car;
    }

    private String validateAndGetCarNumber() {
        String carNo;
        do {
            carNo = dataProvider.getCarNumber();
        } while (carNo == null);
        return carNo;
    }

    private String validateAndGetCarBrand() {
        String carBrand;
        do {
            carBrand = dataProvider.getCarBrand();
        } while (carBrand == null);
        return carBrand;
    }

    private String validateAndGetCarModel() {
        String carModel;
        do {
            carModel = dataProvider.getCarModel();
        } while (carModel == null);
        return carModel;
    }

    private Car createCar(String carNo, String carBrand, String carModel) {
        Car car;
        car = new Car(carNo,carBrand,carModel);
        carTable.addCar(car);
        return car;
    }

    public boolean confirmCarDetails(Car car) {
        while (true) {
            String carNo = car.getCarNumber();
            String carBrand = car.getCarBrand();
            String carModel = car.getCarModel();
            dataPrinter.showCarDetails(carNo,carBrand,carModel);
            String ch = dataProvider.getCarConfirmation();
            int choice = Validator.validateInteger(ch,1,5);
            if(choice == -1) continue;
            if(choice == 1) {
                car.setCarNumber(validateAndGetCarNumber());
            }
            else if(choice == 2) {
                car.setCarBrand(validateAndGetCarBrand());
            }
            else if(choice == 3) {
                car.setCarModel(validateAndGetCarModel());
            }
            else if(choice == 4) break;
            else if(choice == 5) {
                dataPrinter.CarParkingCancelledMessage();
                return false;
            }
        }
        return true;
    }

    public Car acceptCarDetailsToExit() {
        if(!obj.isCarAvailable()) {
            dataPrinter.noCarsAvailable();
            return null;
        }
        return getValidCarFromParking();
    }

    private Car getValidCarFromParking() {
        while (true) {
            String carNo = dataProvider.getCarNumberToExit();
            if(carNo.equalsIgnoreCase("b")) return null;
            Car car = carTable.getCarByCarNo(carNo);
            if(car == null) {
                dataPrinter.carNumberNotFound();
            }
            else {
                if(carInParking.isCarNumberExist(carNo)) {
                    return car;
                }
                else {
                    dataPrinter.givenCarNotInParking();
                }
            }
            dataPrinter.askingBackToMainMenu();
        }
    }

    public boolean confirmCarDetailsForExit(Car car) {
        while (true) {
            String choice = dataProvider.givenCarConfirmation(car);
            if(choice.equalsIgnoreCase("yes")) return true;
            else if(choice.equalsIgnoreCase("no")) {
                return false;
            }
            else {
                dataPrinter.yesOrNoInvalidMessage();
            }
        }
    }
}
