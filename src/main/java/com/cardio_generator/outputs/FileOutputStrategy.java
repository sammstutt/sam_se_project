package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

//Fixed the class name so that it follows UpperCamelCase
public class FileOutputStrategy implements OutputStrategy {

    /** This is where the outputted files are store.*/
    //Changed all instances of BaseDirectory to lowerCamelCase
    private String baseDirectory;

    /** Maps data to their corresponding file.*/
    //Removed the underscore in file_map to match lowerCamelCase
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Creates a FileOutputStrategy with a given baseDirectory.
     *
     * @param baseDirectory the directory where files are stored
     */
    //This is the constructor so it must match the class name and stay UpperCamelCase
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes the specified patient data to a file.
     *
     * @param patientId the number that identifies a specific patient
     * @param timestamp the time it took for the data to be generated
     * @param label the label/title to describe the data
     * @param data the actual data
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        // Set the filePath variable
        //Changed all instances of FilePath to lowerCamelCase
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (IOException e) {
            //Changed Exception to IOException for better error handling
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}