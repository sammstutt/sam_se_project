package com.alerts;

public class HypotensiveHypoxemiaAlertFactory extends AlertFactory{
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, "Hypotensive Hypoxemia Alert: " + condition, timestamp);
    }
}
