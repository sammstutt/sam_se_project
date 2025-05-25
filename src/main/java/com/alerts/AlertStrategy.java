package com.alerts;

import com.data_management.PatientRecord;

import java.util.List;

public interface AlertStrategy {

    boolean checkAlert(PatientRecord record, List<PatientRecord> pastRecords);

}
