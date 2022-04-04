package core;

// Implementation Class
public class CarExit {

    public ParkingCell exitACarFromPosition(CarInParking carInParking, ParkingLot parkingLot, CarParkingSpot pos, Car car) {
        ParkingCell parkingCell = parkingLot.exitACar(pos);
        carInParking.removeCar(car.getCarNumber());
        return parkingCell;
    }

    public void generatePathToExitACar(MultiFloorCarParking obj, ParkingLot parkingLot, CarParkingSpot pos) {
        System.out.println("\nDetailed Path to exit the car from the parking place " +
                "at " + OrdinalNumber.getOrdinalNo(parkingLot.getFloorNo()) + " floor\n");
        showPathToExitACar(obj, parkingLot,pos.getRow(),pos.getCol());
    }

    private void showPathToExitACar(MultiFloorCarParking obj, ParkingLot parkingLot, int row, int col) {
        String path = obj.getDriverSide();
        if(path.equals("L")) {
            parkingLot.setDetailedLeftExitPath(row, col);
        }
        else {
            parkingLot.setDetailedExitPath(row, col);
        }
        System.out.println(parkingLot.getDetailedPath());
        parkingLot.removeDirections();
    }

    public BillingSystem generateBill(CarEntryExit carEntryExit, ParkingCell parkingCell, Car car) {
        BillingSystem billing = carEntryExit.getBilling();
        billing.setCarExitTime(parkingCell.getCarExitTime());
        return billing;
    }

}
