package core;

import java.util.ArrayList;

public class DataHandler {

    private final MultiFloorCarParking obj;
    private final DataProvider dataProvider;
    private final DataPrinter dataPrinter;

    private final CarTable carTable;
    private final CarInParking carInParking;

    public DataHandler(MultiFloorCarParking obj, DataProvider dataProvider, DataPrinter dataPrinter,
                       CarTable carTable, CarInParking carInParking, CarEntryExitTable carEntryExitTable) {
        this.obj = obj;
        this.dataProvider = dataProvider;
        this.dataPrinter = dataPrinter;
        this.carTable = carTable;
        this.carInParking = carInParking;
    }

    public Car acceptCarDetailsToPark() {
        BillingSystem billingSystem = new BillingSystem();
        if (!dataProvider.billingAmountAcceptance(BillingSystem.getBillingAmountPerHour(),BillingSystem.getMoneyAbbr())) return null;
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

    public boolean confirmCarDetails(Car car) {
        while (true) {
            String carNo = car.getCarNumber();
            String carBrand = car.getCarBrand();
            String carModel = car.getCarModel();
            String carInfoUpdateMenu = "\n1. Car Number: " + carNo +
                    "\n2. Car Brand: " + carBrand +
                    "\n3. Car Model Number: " + carModel +
                    "\n4. Continue Parking" +
                    "\n5. Cancel Parking";
            dataPrinter.carDetailsConfirmation(carInfoUpdateMenu);
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

    public boolean confirmCarDetailsForExit(Car car) {
        while (true) {
            String choice = dataProvider.givenCarConfirmation(car.getCarNumber(),car.getCarBrand(),car.getCarModel());
            if(choice.equalsIgnoreCase("yes")) return true;
            else if(choice.equalsIgnoreCase("no")) {
                return false;
            }
            else {
                dataPrinter.yesOrNoInvalidMessage();
            }
        }
    }

    private String validateAndGetCarNumber() {
        String carNo;
        do {
            carNo = dataProvider.takeCarNumberInput();
        } while (carNo == null);
        return carNo;
    }

    private String validateAndGetCarBrand() {
        String carBrand;
        do {
            carBrand = dataProvider.takeCarBrandInput();
        } while (carBrand == null);
        return carBrand;
    }

    private String validateAndGetCarModel() {
        String carModel;
        do {
            carModel = dataProvider.takeCarModelInput();
        } while (carModel == null);
        return carModel;
    }

    private Car createCar(String carNo, String carBrand, String carModel) {
        Car car;
        car = new Car(carNo,carBrand,carModel);
        carTable.addCar(car);
        return car;
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
}
