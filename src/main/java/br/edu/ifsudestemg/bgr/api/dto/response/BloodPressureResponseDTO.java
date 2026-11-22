package br.edu.ifsudestemg.bgr.api.dto.response;

import br.edu.ifsudestemg.bgr.model.entity.BloodPressure;

import java.time.LocalDateTime;

public record BloodPressureResponseDTO(
        Integer systolicBloodPressureLevel,
        Integer diastolicBloodPressureLevel,
        LocalDateTime registrationDateTime,
        String patientName) {

    public static BloodPressureResponseDTO entityToDto(BloodPressure bloodPressure) {
        return new BloodPressureResponseDTO(
                bloodPressure.getSystolicBloodPressureLevel(),
                bloodPressure.getDiastolicBloodPressureLevel(),
                bloodPressure.getRegistrationDateTime(),
                bloodPressure.getPatient().getName()
        );
    }
}
