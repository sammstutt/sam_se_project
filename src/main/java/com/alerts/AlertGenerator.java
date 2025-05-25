package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private final Map<String, AlertStrategy> alertStrategies = new HashMap<>(); // strategies
    private final Map<String, AlertFactory> alertFactories = new HashMap<>(); // factories

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;

        // Initialize the strategies in the hashmap
        alertStrategies.put("SystolicPressure", new BloodPressureStrategy());
        alertStrategies.put("DiastolicPressure", new BloodPressureStrategy());
        alertStrategies.put("BloodSaturation", new OxygenSaturationStrategy());
        alertStrategies.put("ECG", new HeartRateStrategy());
        alertStrategies.put("Manual", new ManualStrategy());

        // Initialize the factories in the hasmap
        alertFactories.put("SystolicPressure", new BloodPressureAlertFactory());
        alertFactories.put("DiastolicPressure", new BloodPressureAlertFactory());
        alertFactories.put("BloodSaturation", new BloodOxygenAlertFactory());
        alertFactories.put("ECG", new ECGAlertFactory());
        alertFactories.put("Manual", new ManualAlertFactory());
    }



    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */

    /*
    public void evaluateData(Patient patient) {
        // Implementation goes here
        // Retrieve recent records
        List<PatientRecord> records = patient.getRecords(System.currentTimeMillis() - 86400000, System.currentTimeMillis()); // last 24h
        if (records == null || records.isEmpty()) return; // No records to evaluate

        List<Double> systolicReadings = new ArrayList<>(); //For blood pressure
        List<Double> diastolicReadings = new ArrayList<>(); //For blood pressure
        List<PatientRecord> saturationRecords = new ArrayList<>(); // For oxygen saturation
        List<Double> ecgReadings = new ArrayList<>(); // For ECG readings
        List<PatientRecord> manualAlertRecords = new ArrayList<>(); // For manual alerts

        for (PatientRecord record : records) {
            if ("SystolicPressure".equals(record.getRecordType())) {
                systolicReadings.add(record.getMeasurementValue());
            } else if ("DiastolicPressure".equals(record.getRecordType())) {
                diastolicReadings.add(record.getMeasurementValue());
            } else if ("BloodSaturation".equals(record.getRecordType())) {
                saturationRecords.add(record);
            } else if ("ECG".equals(record.getRecordType())) {
                ecgReadings.add(record.getMeasurementValue());
            } else if ("ManualAlert".equals(record.getRecordType())) {
                manualAlertRecords.add(record);
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
                    triggerAlertWithFactory(new BloodPressureAlertFactory(), patient.getPatientId(),
                            "SystolicPressure exceeded critical thresholds: ",
                            System.currentTimeMillis());
                }

                //Trend checks
                if (third >= second + 10 && second >= first + 10)
                    triggerAlertWithFactory(new BloodPressureAlertFactory(), patient.getPatientId(), "SystolicPressure shows consistent increase", System.currentTimeMillis());
                if (third <= second - 10 && second <= first - 10)
                    triggerAlertWithFactory(new BloodPressureAlertFactory(), patient.getPatientId(), "SystolicPressure shows consistent decrease", System.currentTimeMillis());
            }
        }

        // Check diastolic for trends and critical thresholds
        if (!diastolicReadings.isEmpty()) {
            for (int i = 2; i < diastolicReadings.size(); i++) {
                double first = diastolicReadings.get(i - 2);
                double second = diastolicReadings.get(i - 1);
                double third = diastolicReadings.get(i);

                // critical threshold checks
                if (third > 120 || third < 60) {
                    triggerAlertWithFactory(new BloodPressureAlertFactory(), patient.getPatientId(),
                            "DiastolicPressure exceeded critical thresholds: ",
                            System.currentTimeMillis());
                }

                //Trend checks
                if (third >= second + 10 && second >= first + 10)
                    triggerAlertWithFactory(new BloodPressureAlertFactory(), patient.getPatientId() ,"DiastolicPressure shows consistent increase", System.currentTimeMillis());
                if (third <= second - 10 && second <= first - 10)
                    triggerAlertWithFactory(new BloodPressureAlertFactory(), patient.getPatientId(), "DiastolicPressure shows consistent decrease", System.currentTimeMillis());
            }
        }
        //---------------------------------------------------------------------------------------------------------//
        //Now it is time for alert type 2
        if (!saturationRecords.isEmpty()) {
            for (int i = 1; i < saturationRecords.size(); i++) {
                PatientRecord previousRecord = saturationRecords.get(i - 1);
                PatientRecord currentRecord = saturationRecords.get(i);

                double previousValue = previousRecord.getMeasurementValue();
                double currentValue = currentRecord.getMeasurementValue();

                //Alert if oxygen saturation falls below 92%
                //is the value 92 or 0.92?
                if (currentValue < 0.92) {
                    triggerAlert(new Alert(patient.getPatientId(), "Oxygen Saturation below 92%", System.currentTimeMillis()));
                }

                //Trigger an alert if the blood oxygen saturation level drops by 5% or more within a 10-minute interval.
                //is the value 5 or 0.05
                if (previousValue - currentValue >= 0.05 &&
                        currentRecord.getTimestamp() - previousRecord.getTimestamp() <= 600000) { //600000 is 10 minutes
                    triggerAlert(new Alert(patient.getPatientId(), "Oxygen Saturation dropped by 5% or more", System.currentTimeMillis()));
                }
            }
        }
        //---------------------------------------------------------------------------------------------------------//
        //Now it is time for alert type 3
        //The alert should trigger when both:
        // Systolic blood pressure is below 90 mmHg
        // Blood oxygen saturation falls below 92%.
        if (!systolicReadings.isEmpty() && !saturationRecords.isEmpty()) {
            for (PatientRecord saturationRecord : saturationRecords) {
                double saturationValue = saturationRecord.getMeasurementValue();

                if (saturationValue < 0.92) { // Check if saturation falls below 92%
                    for (double systolicValue : systolicReadings) {
                        if (systolicValue < 90) { // Check if systolic blood pressure is below 90 mmHg
                            triggerAlert(new Alert(patient.getPatientId(), "Hypotensive Hypoxemia Alert", saturationRecord.getTimestamp()));
                            break; // Exit the loop once the alert is triggered
                        }
                    }
                }
            }
        }
        //---------------------------------------------------------------------------------------------------------//
        //Now it is time for alert type 4
        //ECG Data Alerts
        //Trigger an alert if peaks above certain values happen.
        // Measure the average data generated using a sliding window.
        // Then if any peaks occur far beyond the current average generate an alert.
        if (!ecgReadings.isEmpty()) {
            int windowSize = Math.min(5, ecgReadings.size()); // sliding window size is 5 or less. Can be changed.
            for (int i = windowSize; i < ecgReadings.size(); i++) {
                double sum = 0;
                for (int j = i - windowSize; j < i; j++) {
                    sum += ecgReadings.get(j); // Sum values
                }
                double average = sum / windowSize; // Find average

                double currentValue = ecgReadings.get(i);
                if (currentValue > average * 1.5) { // Check if peak occurs far beyond average
                    triggerAlert(new Alert(patient.getPatientId(), "ECG abnormal peak detected!", System.currentTimeMillis()));
                }
            }
        }
        //---------------------------------------------------------------------------------------------------------//
        //Now it is time for alert type 5
        //This type of alert is generated by nurses or patients triggering the alert button near their beds.
        //It is separately emitted by the HealthDataGenerator.
        //Triggered and untriggered based on the Alert output from the HealthDataGenerator
        if (!manualAlertRecords.isEmpty()) {
            for (PatientRecord alertRecord : manualAlertRecords) {
                // Only trigger the alert if it has been pressed
                if (alertRecord.getMeasurementValue() > 0) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()),
                            "Manual Alert by nurse or patient.",
                            alertRecord.getTimestamp()));
                }
            }
        }
    }*/

    public void evaluateData(Patient patient) {
        // gets records from the last 24 hours
        List<PatientRecord> records = patient.getRecords(System.currentTimeMillis() - 86400000L, System.currentTimeMillis());

        if (records == null || records.isEmpty()) return; // there is nothingto evaluate

        for (PatientRecord record : records) {
            String recordType = record.getRecordType();

            // this matches the correct strategy and factory for the record type
            AlertStrategy strategy = alertStrategies.get(recordType);
            AlertFactory factory = alertFactories.get(recordType);

            if (strategy != null && factory != null) {
                // the strategy checks if there condition has been met to trigger an alert
                boolean isAlertTriggered = strategy.checkAlert(record, records);

                if (isAlertTriggered) {
                    // the factory will then create the alert
                    Alert alert = factory.createAlert(String.valueOf(patient.getPatientId()), "Condition: " + recordType, record.getTimestamp());

                    // teh alert is then triggered
                    triggerAlert(alert);
                }
            }
        }
    }

    // Add a field to store triggered alerts
    private final List<Alert> triggeredAlerts = new ArrayList<>();

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert (Alert alert){
        triggeredAlerts.add(alert);
        System.out.println("\nALERT TRIGGERED");
        System.out.println("Patient ID: " + alert.getPatientId());
        System.out.println("Condition: " + alert.getCondition());
        System.out.println("Timestamp: " + alert.getTimestamp());
    }


    // Provide a method to access triggered alerts
    public List<Alert> getTriggeredAlerts() {

        return triggeredAlerts;
    }


}



