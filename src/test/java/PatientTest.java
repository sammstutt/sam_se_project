import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {
    private Patient patient;

    @BeforeEach
    void setUp() {
        // Initialize the Patient and records before each test
        patient = new Patient(1);
        patient.addRecord(98.6, "BodyTemperature", 1);
        patient.addRecord(120.0, "BloodPressure", 2);
        patient.addRecord(75.0, "HeartRate", 3);
    }

    @Test
    void testGetRecords_withinRange() {
        // time range that includes the first two records
        long startTime = 1;
        long endTime = 2;

        // Retrieve records within the range
        List<PatientRecord> records = patient.getRecords(startTime, endTime);

        // Verify the records
        assertEquals(2, records.size(), "The number of records should match the expected range");
    }

    @Test
    void testGetRecords_outOfRange() {
        // time range with no records
        long startTime = 4;
        long endTime = 5;

        // Retrieve records within the range
        List<PatientRecord> records = patient.getRecords(startTime, endTime);

        // Verifyrecords
        assertTrue(records.isEmpty(), "No records should be retrieved");
    }

    @Test
    void testGetRecords_singleRecord() {
        //time range that matches a single record's timestamp
        long startTime = 3;
        long endTime = 3;

        // Retrieve records within the range
        List<PatientRecord> records = patient.getRecords(startTime, endTime);

        // Verify record
        assertEquals(1, records.size(), "Only one record should be retrieved");

    }
}
