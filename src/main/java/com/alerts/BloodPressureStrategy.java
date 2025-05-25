package com.alerts;

import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy{

    @Override
    public boolean checkAlert(PatientRecord record, List<PatientRecord> pastRecords){
        if (pastRecords == null || pastRecords.size() < 2) {
            return false; // Not enough records to analyse the trend
        }

        List<Double> systolicReadings = new ArrayList<>(); //For blood pressure
        List<Double> diastolicReadings = new ArrayList<>(); //For blood pressure

        //Initializes the readings we want
        for (PatientRecord records : pastRecords) {
            if ("SystolicPressure".equals(record.getRecordType())) {
                systolicReadings.add(record.getMeasurementValue());
            } else if ("DiastolicPressure".equals(record.getRecordType())) {
                diastolicReadings.add(record.getMeasurementValue());
            }
        }

        // Check systolic for trends and critical thresholds
        if (!systolicReadings.isEmpty()) {
            for (int i = 2; i < systolicReadings.size(); i++) {
                double first = systolicReadings.get(i - 2);
                double second = systolicReadings.get(i - 1);
                double third = systolicReadings.get(i);
                //This is done so we can compare values for trends and critical thresholds



                // Critical threshold checks
                // NOTE: I made a getPatientId() method in the patient class
                if (first > 180 || first < 90 || second > 180 || second < 90  || third > 180 || third < 90) {
                    return true;
                }

                //Trend checks
                if (third >= second + 10 && second >= first + 10) return true;
                if (third <= second - 10 && second <= first - 10) return true;
            }
        }


        // Check diastolic for trends and critical thresholds
        if (!diastolicReadings.isEmpty()) {
            for (int i = 2; i < diastolicReadings.size(); i++) {
                double first = diastolicReadings.get(i - 2);
                double second = diastolicReadings.get(i - 1);
                double third = diastolicReadings.get(i);

                // critical threshold checks
                if (third > 120 || third < 60) return true;

                //Trend checks
                if (third >= second + 10 && second >= first + 10) return true;
                if (third <= second - 10 && second <= first - 10) return true;
            }
        }
        return false;
    }
}
