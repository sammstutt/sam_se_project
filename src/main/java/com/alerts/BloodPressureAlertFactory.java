package com.alerts;

import com.data_management.*;


import java.util.List;

public class BloodPressureAlertFactory extends AlertFactory{
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, "Blood Pressure Alert: " + condition, timestamp);
    }
}
