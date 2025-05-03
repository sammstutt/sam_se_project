package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * This class represents an alert generator that keeps track of
 * the patients current alert states and each active alert has a
 * 90% chance to be resolved.
 */
public class AlertGenerator implements PatientDataGenerator {

    /** Random number generator */
    //Changed all instances of randomGenerator to UPPER_SNAKE_CASE because it is a constant
    public static final Random RANDOM_GENERATOR = new Random();

    /** Keeps track of the patients current alertStates.*/
    //Changed all instances AlterStates to lowerCamelCase
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Creates a AlertGenerator to keep track of specified number of patient alertStates.
     * @param patientCount number of patients to keep track off.
     */
    public AlertGenerator(int patientCount) {
        //Indent here to make it easier to read
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates the alerts for a specific patient.
     * If there is an alert active then there is a 90% chance for it to be resolved.
     *
     * @param patientId indicates the specific patient to gather data from
     * @param outputStrategy specifies the strategy used for to output the data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                //Changed all instances of Lambda to lowerCamelCase
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (RuntimeException e) {
            //Changed Exception to RuntimeException
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
