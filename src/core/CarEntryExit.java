package core;

import java.time.LocalTime;

// Model Class
public class CarEntryExit {

    private final LocalTime entryTime;
    // Reason for Non-Final: Initially, Once Parked, exit time will be 00:00:00, after car exit, it will be updated
    private LocalTime exitTime;
    private final CarLocation position;
    private final BillingSystem billing;

    protected CarEntryExit(LocalTime entryTime, CarLocation position) {
        this.entryTime = entryTime;
        this.position = position;
        this.billing = new BillingSystem(entryTime);
    }

    public LocalTime getEntryTime() {
        return entryTime;
    }

    public LocalTime getExitTime() {
        return exitTime;
    }

    protected CarLocation getPosition() {
        return position;
    }

    public BillingSystem getBilling() {
        return billing;
    }

    protected void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    }
}
