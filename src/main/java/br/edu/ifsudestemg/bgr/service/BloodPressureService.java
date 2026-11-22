package br.edu.ifsudestemg.bgr.service;

import br.edu.ifsudestemg.bgr.exception.BusinessRuleException;
import br.edu.ifsudestemg.bgr.model.entity.BloodPressure;
import br.edu.ifsudestemg.bgr.model.entity.Patient;
import br.edu.ifsudestemg.bgr.model.repository.BloodPressureRepository;
import br.edu.ifsudestemg.bgr.model.repository.PatientRepository;
import br.edu.ifsudestemg.bgr.service.interfaces.IPatient;
import br.edu.ifsudestemg.bgr.service.validator.BloodPressureValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BloodPressureService {

    private final BloodPressureRepository repository;
    private final BloodPressureValidator validator;
    private final PatientRepository patientRepository;
    private final IPatient patientService;

    public void createBloodPressureRecord(BloodPressure bloodPressure) {
        validator.validate(bloodPressure);
        repository.save(bloodPressure);
    }

    public List<BloodPressure> searchForAllBloodPressureRecords() {
        return repository.findAll();
    }

    public Optional<BloodPressure> searchForBloodPressureRecordById(UUID id) {
        return repository.findById(id);
    }

    public void updateBloodPressureRecord(BloodPressure bloodPressure) {
        validator.validate(bloodPressure);
        repository.save(bloodPressure);
    }

    public void deleteBloodPressureRecord(UUID idBloodPressure) {
        repository.deleteById(idBloodPressure);
    }

    public String checkForHypertension(UUID idBloodPressure) {
        Optional<BloodPressure> bloodPressure = repository.findById(idBloodPressure);
        if (bloodPressure.isEmpty()) {
            throw new BusinessRuleException("Pressão sanguínea inexistente.");
        }
        Optional<Patient> patient = patientRepository.findById(bloodPressure.get().getPatient().getId());
        if (patient.isEmpty()) {
            throw new BusinessRuleException("Paciente inexistente");
        }

        Integer agePatient = patientService.calculateAge(patient.get().getId());
        Integer systolic = bloodPressure.get().getSystolicBloodPressureLevel();
        Integer diastolic = bloodPressure.get().getDiastolicBloodPressureLevel();

        String result;

        if (agePatient < 15) {
            if (systolic > 110 || diastolic > 70) {
                result = "Criança com pressão alta";
            } else {
                result = "A pressão da criança não está alta";
            }
        } else {
            if (systolic > 120  || diastolic > 80) {
                result = "Adulto com pressão alta";
            } else {
                result = "A pressão do adulto não está alta";
            }
        }

        return result;
    }
}