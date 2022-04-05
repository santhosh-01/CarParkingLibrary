package core;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

// Model Class
public class BillingSystem {

    private LocalTime carEntryTime;
    private LocalTime carExitTime;
    private static MoneyType moneyType;
    private static double billingAmountPerHour;
    private static String moneyAbbr;
    private double bill;

    protected BillingSystem() {}

    protected BillingSystem(LocalTime carEntryTime) {
        this.carEntryTime = carEntryTime;
    }

    protected LocalTime getCarEntryTime() {
        return carEntryTime;
    }

    protected LocalTime getCarExitTime() {
        return carExitTime;
    }

    public static MoneyType getMoneyType() {
        return moneyType;
    }

    public static double getBillingAmountPerHour() {
        return billingAmountPerHour;
    }

    public static String getMoneyAbbr() {
        return moneyAbbr;
    }

    protected double getBill() {
        if(this.carExitTime == null) {
            this.bill = calculateBill(calculateCarParkedInSeconds());
        }
        return this.bill;
    }

    protected void setCarExitTime(LocalTime carExitTime) {
        this.carExitTime = carExitTime;
        this.bill = calculateBill(calculateCarParkedInSeconds());
    }

    protected void setBillingAmountPerHour(double billingAmountPerHour) {
        BillingSystem.billingAmountPerHour = billingAmountPerHour;
    }

    protected void setMoneyType(MoneyType moneyType) {
        BillingSystem.moneyType = moneyType;
        switch (BillingSystem.moneyType) {
            case INR -> moneyAbbr = "INR";
            case USD -> moneyAbbr = "USD";
        }
    }

    private double calculateBill(long seconds) {
        int SECONDS_IN_HOUR = 60 * 60;
        double billingAmountPerSecond = billingAmountPerHour / SECONDS_IN_HOUR;
        return billingAmountPerSecond * seconds;
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

    public String getBillingSummary() {
        return String.format("parkingTimeInSeconds=%d seconds - ",calculateCarParkedInSeconds()) +
                String.format("bill=%.2f %s",getBill(), BillingSystem.getMoneyAbbr());
    }
}