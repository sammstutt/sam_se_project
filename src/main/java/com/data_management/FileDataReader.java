package com.data_management;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * Reads patient data from files in the specified directory and stores it into the DataStorage system.
 * Each file is expected to hold patient data in the format:
 * <patientId>,<measurementValue>,<recordType>,<timestamp>
 */
public class FileDataReader implements DataReader {

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        // Get the directory containing the data files
        String directoryPath = System.getProperty("output_dir");

        // This validates the directory path and checks whether it is emtpy
        if (directoryPath == null || directoryPath.isEmpty()) {
            throw new IllegalArgumentException("The output directory is not specified. Set 'output_dir' system property.");
        }
        Path directory = Paths.get(directoryPath);
        if (!Files.isDirectory(directory)) {
            throw new IOException("The provided path isnt a valid directory: " + directoryPath);
        }

        // Traverse through all files in the directory
        try (Stream<Path> paths = Files.list(directory)) {
            paths.filter(Files::isRegularFile)
                    .forEach(file -> processFile(file, dataStorage));
        }
    }

    /**
     * Processes a single file and adds its data to the DataStorage.
     *
     * @param file        the file to process
     * @param dataStorage the DataStorage instance to store the parsed data
     */
    private void processFile(Path file, DataStorage dataStorage) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // each line here represents a patient record
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.err.println("Skipping invalid line in file " + file + ": " + line);
                    continue;
                }

                try {
                    // Parse the line into data
                    int patientId = Integer.parseInt(parts[0].trim());
                    double measurementValue = Double.parseDouble(parts[1].trim());
                    String recordType = parts[2].trim();
                    long timestamp = Long.parseLong(parts[3].trim());

                    // Add the parsed data to the DataStorage
                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid data in file " + file + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + file + ". Error: " + e.getMessage());
        }
    }
}


