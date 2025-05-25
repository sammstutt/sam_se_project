package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy {

    @Override
    public boolean checkAlert(PatientRecord record, List<PatientRecord> pastRecords) {
        List<Double> ecgReadings = new ArrayList<>(); // For ECG readings
        for (PatientRecord records : pastRecords) {
            if ("ECG".equals(record.getRecordType())) ecgReadings.add(record.getMeasurementValue());
        }

        if (!ecgReadings.isEmpty()) {
            int windowSize = Math.min(5, ecgReadings.size()); // sliding window size is 5 or less. Can be changed.
            for (int i = windowSize; i < ecgReadings.size(); i++) {
                double sum = 0;
                for (int j = i - windowSize; j < i; j++) {
                    sum += ecgReadings.get(j); // Sum values
                }
                double average = sum / windowSize; // Find average

                double currentValue = ecgReadings.get(i);
                if (currentValue > average * 1.5) return true;
            }
        }
        return false;
    }
}
