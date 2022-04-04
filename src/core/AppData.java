package core;

// Data Class
public class AppData {

    private final int floors;
    private final int rows;
    private final int columns;
    private final int pathWidth;
    private final String drivingSide;
    private final double billingAmountPerHour;

    public AppData(int floors, int rows, int columns, int pathWidth, String path, double billingAmountPerHour) {
        this.floors = floors;
        this.rows = rows;
        this.columns = columns;
        this.pathWidth = pathWidth;
        this.drivingSide = path;
        this.billingAmountPerHour = billingAmountPerHour;
    }

    protected int getFloors() {
        return floors;
    }

    protected int getRows() {
        return rows;
    }

    protected int getColumns() {
        return columns;
    }

    protected int getPathWidth() {
        return pathWidth;
    }

    protected String getDrivingSide() {
        return drivingSide;
    }

    protected double getBillingAmountPerHour() {
        return billingAmountPerHour;
    }
}
