package com.cardio_generator.outputs;

/**
 * This interface defines the structure for classes that output specific patient data.
 * The classes that implement the interface must provide the logic for the methods in this interface.
 */
public interface OutputStrategy {

    /**
     * Outputs a specific patients health data
     *
     * @param patientId the number that identifies a specific patient
     * @param timestamp the time it took for the data to be generated
     * @param label the label/title to describe the data
     * @param data the actual data
     */
    void output(int patientId, long timestamp, String label, String data);
}
