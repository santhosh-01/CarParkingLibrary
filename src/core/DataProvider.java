package core;

public interface DataProvider {

    boolean billingAmountAcceptance(double billingAmountPerHour, String moneyAbbr);

    String takeCarModelInput();

    String takeCarBrandInput();

    String takeCarNumberInput();

    String getCarConfirmation();

    String getLastCarParkingConfirmation();

    String getSuggestedParkingFloorConfirmation(int floorNo);

    String getFloorNumber();

    String getSuggestedParkingPlaceConfirmation(int position);

    String takeCarParkingSpotInput(int floorNo, String modifiedParkingLotMap);

    String getCarNumberToExit();

    String givenCarConfirmation(String carNumber, String carBrand, String carModel);

    String getCarNumberForHistory();

}
