package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int repeatInterval; // Time interval to repeat the alert
    private long lastTriggerTime;

    public RepeatedAlertDecorator(Alert alert, int repeatInterval) {
        super(alert);
        this.repeatInterval = repeatInterval;
        this.lastTriggerTime = alert.getTimestamp();
    }

    @Override
    public long getTimestamp() { //this checks if enough time has passed to repeat the alert
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTriggerTime >= repeatInterval) {
            lastTriggerTime = currentTime;
            return currentTime;
        }

        return wrappedAlert.getTimestamp();
    }
}
