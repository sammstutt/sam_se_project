package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class HypotensiveHypoxemiaStrategy implements AlertStrategy{
    public boolean checkAlert(PatientRecord record, List<PatientRecord> pastRecords){
        List<Double> systolicReadings = new ArrayList<>(); //For blood pressure
        List<PatientRecord> saturationRecords = new ArrayList<>(); // For oxygen saturation
        for (PatientRecord records : pastRecords) {
            if ("SystolicPressure".equals(record.getRecordType())) {
                systolicReadings.add(record.getMeasurementValue());
            } else if ("BloodSaturation".equals(record.getRecordType())) {
                saturationRecords.add(record);
            }
        }

        if (!systolicReadings.isEmpty() && !saturationRecords.isEmpty()) {
            for (PatientRecord saturationRecord : saturationRecords) {
                double saturationValue = saturationRecord.getMeasurementValue();

                if (saturationValue < 0.92) { // Check if saturation falls below 92%
                    for (double systolicValue : systolicReadings) {
                        if (systolicValue < 90) return true;
                    }
                }
            }
        }
        return false;
    }
}
