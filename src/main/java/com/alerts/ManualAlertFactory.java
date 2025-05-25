package com.alerts;

public class ManualAlertFactory extends AlertFactory{
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, "Manual Alert: " + condition, timestamp);
    }
}
