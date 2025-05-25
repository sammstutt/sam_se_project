package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class ManualStrategy implements AlertStrategy{
    public boolean checkAlert(PatientRecord record, List<PatientRecord> pastRecords){
        List<PatientRecord> manualAlertRecords = new ArrayList<>(); // For manual alerts
        for (PatientRecord records : pastRecords) {
            if ("Manual".equals(record.getRecordType())) manualAlertRecords.add(record);
        }

        if (!manualAlertRecords.isEmpty()) {
            for (PatientRecord alertRecord : manualAlertRecords) {
                // Only trigger the alert if it has been pressed
                if (alertRecord.getMeasurementValue() > 0) return true;
            }
        }
        return false;
    }
}
