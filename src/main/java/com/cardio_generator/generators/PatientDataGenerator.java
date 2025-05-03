package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * This interface defines the structure for classes that generate specific patient data.
 * The classes that implement the interface must provide the logic for the methods in this interface.
 */
public interface PatientDataGenerator {
    /**
     * Generates the data for a specific patient
     *
     * @param patientId indicates the specific patient to gather data from
     * @param outputStrategy specifies the strategy used for to output the data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
