package model;

import util.TimeFormat;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class BillingSystem {

    private final LocalTime carEntryTime;
    private LocalTime carExitTime;
    public static MoneyType moneyType;
    public static double billingAmountPerHour = 100.0;
    public static String moneyAbbr;
    private double bill;

    public BillingSystem(LocalTime carEntryTime) {
        this.carEntryTime = carEntryTime;
    }

    public static void setMoneyType(MoneyType moneyType) {
        BillingSystem.moneyType = moneyType;
        switch (moneyType) {
            case INR -> moneyAbbr = "INR";
            case USD -> moneyAbbr = "USD";
        }
    }

    private double calculateBill(long seconds) {
        double billingAmountPerSecond = billingAmountPerHour/(60.0 * 60.0);
        return billingAmountPerSecond * seconds;
    }

    public int getId() {
        return id;
    }

    public LocalTime getCarEntryTime() {
        return carEntryTime;
    }

    public LocalTime getCarExitTime() {
        return carExitTime;
    }

    public double getBill() {
        if(this.carExitTime == null) {
            this.bill = calculateBill(calculateCarParkedInSeconds());
        }
        return this.bill;
    }

    public void setCarExitTime(LocalTime carExitTime) {
        this.carExitTime = carExitTime;
        this.bill = calculateBill(calculateCarParkedInSeconds());
    }

    private long calculateCarParkedInSeconds() {
        if(carExitTime == null) return ChronoUnit.SECONDS.between(this.carEntryTime, LocalTime.now());
        else return ChronoUnit.SECONDS.between(this.carEntryTime, carExitTime);
    }

    @Override
    public String toString() {
        return "carEntryTime=" + TimeFormat.getTime(carEntryTime) +
                "\ncarExitTime=" + TimeFormat.getTime(carExitTime==null?LocalTime.of(0,0,0):carExitTime) +
                String.format("\nparkingTimeInSeconds=%d seconds",calculateCarParkedInSeconds()) +
                String.format("\nbill=%.2f %s",getBill(), BillingSystem.moneyAbbr);
    }
}
