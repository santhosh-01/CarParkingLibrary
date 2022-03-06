package model;

import java.time.LocalTime;

public class Billing {

    private int id;
    private static int nextId = 1001;
    private LocalTime carEntryTime;
    private LocalTime carExitTime;
    public static MoneyType moneyType;
    public static double billingAmountPerHour = 100.0;
    public static String moneyAbbr;
    private double bill;

    public Billing(LocalTime carEntryTime, LocalTime carExitTime, long seconds) {
        this.id = nextId;
        this.carEntryTime = carEntryTime;
        this.carExitTime = carExitTime;
        this.bill = calculateBill(seconds);
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
        double billingAmountPerSecond = this.billingAmountPerHour/(60.0 * 60.0);
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
        return bill;
    }
}
