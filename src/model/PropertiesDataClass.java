package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesDataClass {

    private int floors;
    private int rows;
    private int columns;
    private int pathWidth;
    private String path;
    private double billingAmountPerHour;

    public PropertiesDataClass(int floors, int rows, int columns, int pathWidth, String path, double billingAmountPerHour) {
        this.floors = floors;
        this.rows = rows;
        this.columns = columns;
        this.pathWidth = pathWidth;
        this.path = path;
        this.billingAmountPerHour = billingAmountPerHour;
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

    public int getPathWidth() {
        return pathWidth;
    }

    public String getPath() {
        return path;
    }

    public double getBillingAmountPerHour() {
        return billingAmountPerHour;
    }
}
