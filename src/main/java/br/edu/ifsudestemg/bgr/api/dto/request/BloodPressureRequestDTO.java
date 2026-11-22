package br.edu.ifsudestemg.bgr.api.dto.request;

import br.edu.ifsudestemg.bgr.model.entity.BloodPressure;
import br.edu.ifsudestemg.bgr.model.entity.Patient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record BloodPressureRequestDTO(
        @NotNull(message = "Nível de pressão sistólica não pode ser vazio!")
        @Positive(message = "Nível de pressão sistólica tem que ser maior que 0")
        Integer systolicBloodPressureLevel,

        @NotNull(message = "Nível de pressão diastólica não pode ser vazio!")
        @Positive(message = "Nível de pressão diastólica tem que ser maior que 0")
        Integer diastolicBloodPressureLevel,

        @NotNull(message = "O registro de pressão deve estar associado a um usuário!")
        String idPatient) {

    public BloodPressure dtoToEntity() {
        BloodPressure bloodPressure = new BloodPressure();
        bloodPressure.setSystolicBloodPressureLevel(this.systolicBloodPressureLevel);
        bloodPressure.setDiastolicBloodPressureLevel(this.diastolicBloodPressureLevel);
        Patient patient = new Patient();
        patient.setId(UUID.fromString(this.idPatient));
        bloodPressure.setPatient(patient);

        return bloodPressure;
    }
}
