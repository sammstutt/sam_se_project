package com.alerts;

public abstract class AlertDecorator extends Alert {

    protected Alert wrappedAlert;

    public AlertDecorator(Alert alert) {
        super(alert.getPatientId(), alert.getCondition(), alert.getTimestamp());
        this.wrappedAlert = alert; // Wrap the actual alert
    }

    @Override
    public String getCondition() {
        return wrappedAlert.getCondition(); // Delegate behavior to the wrapped alert
    }

    @Override
    public long getTimestamp() {
        return wrappedAlert.getTimestamp();
    }

    @Override
    public String getPatientId() {
        return wrappedAlert.getPatientId();
    }
}

