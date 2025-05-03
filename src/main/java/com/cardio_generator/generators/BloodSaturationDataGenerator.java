package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * This class generates a blood saturation level for individual patients.
 * Saturation level are updated to simulate fluctuations.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

    /** Random number generator for variation */
    private static final Random random = new Random();

    /** Stores the most recent saturation value*/
    private int[] lastSaturationValues;

    /**
     * Constructs a new BloodSaturationDataGenerator.
     *
     * @param patientCount the amount of patients to simulate
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates are new blood saturation level for the specific patient and outputs it.
     * It ensures the saturation level stays in a realistic and healthy range.
     *
     * @param patientId indicates the specific patient to gather data from
     * @param outputStrategy specifies the strategy used for to output the data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
