package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {

    @Override
    public boolean checkAlert(PatientRecord record, List<PatientRecord> pastRecords) {
        List<PatientRecord> saturationRecords = new ArrayList<>(); // For oxygen saturation
        for (PatientRecord records : pastRecords) {
            if ("BloodSaturation".equals(record.getRecordType())) saturationRecords.add(record);
        }

        if (!saturationRecords.isEmpty()) {
            for (int i = 1; i < saturationRecords.size(); i++) {
                PatientRecord previousRecord = saturationRecords.get(i - 1);
                PatientRecord currentRecord = saturationRecords.get(i);

                double previousValue = previousRecord.getMeasurementValue();
                double currentValue = currentRecord.getMeasurementValue();

                //Alert if oxygen saturation falls below 92%
                if (currentValue < 0.92) return true;

                //Trigger an alert if the blood oxygen saturation level drops by 5% or more within a 10-minute interval.
                if (previousValue - currentValue >= 0.05 &&
                        currentRecord.getTimestamp() - previousRecord.getTimestamp() <= 600000) return true;
            }
        }
        return false;
    }
}
