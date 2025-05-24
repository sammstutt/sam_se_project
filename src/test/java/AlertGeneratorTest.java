import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    @Test
    void testSystolicPressureAlerts() {
        Patient patient1 = new Patient(1);
        Patient patient2 = new Patient(2);
        Patient patient3 = new Patient(3);

        // Add records for critical threshold test
        patient1.addRecord(200, "SystolicPressure", System.currentTimeMillis());
        patient1.addRecord(200, "SystolicPressure", System.currentTimeMillis());
        patient1.addRecord(200, "SystolicPressure", System.currentTimeMillis());

        // Add records for trend test (consistent increase and decrease)
        patient2.addRecord(100, "SystolicPressure", System.currentTimeMillis());
        patient2.addRecord(110, "SystolicPressure", System.currentTimeMillis());
        patient2.addRecord(120, "SystolicPressure", System.currentTimeMillis());


        patient3.addRecord(115, "SystolicPressure", System.currentTimeMillis());
        patient3.addRecord(105, "SystolicPressure", System.currentTimeMillis());
        patient3.addRecord(95, "SystolicPressure", System.currentTimeMillis());

        AlertGenerator alertGenerator = new AlertGenerator(new DataStorage());
        alertGenerator.evaluateData(patient1);
        alertGenerator.evaluateData(patient2);
        alertGenerator.evaluateData(patient3);
        // Tests require 3 records to work

        System.out.println("-----------------------------------------------------------------------------------------");
    }

    @Test
    void testDiastolicPressureAlerts() {
        Patient patient1 = new Patient(1);
        Patient patient2 = new Patient(2);
        Patient patient3 = new Patient(3);
        // Add records for critical threshold test
        patient1.addRecord(125, "DiastolicPressure", System.currentTimeMillis());
        patient1.addRecord(55, "DiastolicPressure", System.currentTimeMillis());
        patient1.addRecord(55, "DiastolicPressure", System.currentTimeMillis());

        // Add records for trend test (consistent increase and decrease)
        patient2.addRecord(70, "DiastolicPressure", System.currentTimeMillis());
        patient2.addRecord(80, "DiastolicPressure", System.currentTimeMillis());
        patient2.addRecord(90, "DiastolicPressure", System.currentTimeMillis());

        patient3.addRecord(90, "DiastolicPressure", System.currentTimeMillis());
        patient3.addRecord(80, "DiastolicPressure", System.currentTimeMillis());
        patient3.addRecord(70, "DiastolicPressure", System.currentTimeMillis());

        AlertGenerator alertGenerator = new AlertGenerator(new DataStorage());
        alertGenerator.evaluateData(patient1);
        alertGenerator.evaluateData(patient2);
        alertGenerator.evaluateData(patient3);
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    @Test
    void testBloodSaturationAlerts() {
        Patient patient1 = new Patient(1);
        Patient patient2 = new Patient(2);

        // Add records for critical saturation level.
        patient1.addRecord(0.90, "BloodSaturation", System.currentTimeMillis());
        patient1.addRecord(0.90, "BloodSaturation", System.currentTimeMillis());

        // Add records for a 5% drop within 10 minutes
        long now = System.currentTimeMillis();
        patient2.addRecord(0.98, "BloodSaturation", now - 600_000);
        patient2.addRecord(0.92, "BloodSaturation", now);

        AlertGenerator alertGenerator = new AlertGenerator(new DataStorage());
        alertGenerator.evaluateData(patient1);
        alertGenerator.evaluateData(patient2);

        System.out.println("-----------------------------------------------------------------------------------------");
    }

    @Test
    void testHypotensiveHypoxemiaAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(80, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(0.91, "BloodSaturation", System.currentTimeMillis());

        AlertGenerator alertGenerator = new AlertGenerator(new DataStorage());

        alertGenerator.evaluateData(patient);
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    @Test
    void testECGPeakAlerts() {
        Patient patient = new Patient(1);

        // Noraml values
        long now = System.currentTimeMillis();
        patient.addRecord(0.8, "ECG", now - 5000);  // Normal values
        patient.addRecord(0.9, "ECG", now - 4000);
        patient.addRecord(0.85, "ECG", now - 3000);
        patient.addRecord(0.87, "ECG", now - 2000);
        patient.addRecord(0.9, "ECG", now - 1000);

        // Abnormal value
        patient.addRecord(2.0, "ECG", now );

        AlertGenerator alertGenerator = new AlertGenerator(new DataStorage());
        alertGenerator.evaluateData(patient);
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    @Test
    void testManualAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(1, "ManualAlert", System.currentTimeMillis());

        AlertGenerator alertGenerator = new AlertGenerator(new DataStorage());

        alertGenerator.evaluateData(patient);
        System.out.println("-----------------------------------------------------------------------------------------");
    }

}
