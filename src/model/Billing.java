package model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Billing {

    private int id;
    private static int nextId = 1001;
    private LocalTime carEntryTime;
    private LocalTime carExitTime;
    public static MoneyType moneyType;
    public static double billingAmountPerHour = 100.0;
    public static String moneyAbbr;
    private double bill;

    public Billing(LocalTime carEntryTime) {
        this.id = nextId;
        this.carEntryTime = carEntryTime;
        nextId ++;
    }

    public static void setMoneyType(MoneyType moneyType) {
        Billing.moneyType = moneyType;
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

    public void setCarExitTime(LocalTime carExitTime, long seconds) {
        this.carExitTime = carExitTime;
        this.bill = calculateBill(seconds);
    }

    private long calculateCarParkedInSeconds() {
        return ChronoUnit.SECONDS.between(this.carEntryTime, LocalTime.now());
    }
}