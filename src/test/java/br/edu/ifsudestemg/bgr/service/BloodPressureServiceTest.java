package br.edu.ifsudestemg.bgr.service;

import br.edu.ifsudestemg.bgr.model.entity.BloodPressure;
import br.edu.ifsudestemg.bgr.model.entity.Patient;
import br.edu.ifsudestemg.bgr.model.repository.BloodPressureRepository;
import br.edu.ifsudestemg.bgr.model.repository.PatientRepository;
import br.edu.ifsudestemg.bgr.service.interfaces.IPatient;
import br.edu.ifsudestemg.bgr.exception.BusinessRuleException;

import br.edu.ifsudestemg.bgr.service.validator.BloodPressureValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

class BloodPressureServiceTest {

    BloodPressure bloodPressure;
    Patient patient;
    BloodPressureService service;
    BloodPressureValidator validator;

    BloodPressureRepository repositoryMock;
    PatientRepository patientRepositoryMock;
    IPatient patientMock;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(UUID.randomUUID());

        bloodPressure = new BloodPressure();
        bloodPressure.setId(UUID.randomUUID());
        bloodPressure.setPatient(patient);
        bloodPressure.setSystolicBloodPressureLevel(110);
        bloodPressure.setDiastolicBloodPressureLevel(70);

        repositoryMock = createMock(BloodPressureRepository.class);
        patientRepositoryMock = createMock(PatientRepository.class);
        patientMock = createMock(IPatient.class);

        service = new BloodPressureService(
                repositoryMock,
                validator,
                patientRepositoryMock,
                patientMock
        );
    }

    private void prepareMocksForExistingBloodPressure(Integer age) {
        expect(repositoryMock.findById(bloodPressure.getId())).andReturn(Optional.of(bloodPressure));
        expect(patientRepositoryMock.findById(patient.getId())).andReturn(Optional.of(patient));
        expect(patientMock.calculateAge(patient.getId())).andReturn(age);
        replay(repositoryMock, patientRepositoryMock, patientMock);
    }

    @Test
    void shouldThrowAnExceptionIfBloodPressureDoesNotExist() {
        expect(repositoryMock.findById(bloodPressure.getId())).andReturn(Optional.empty());
        replay(repositoryMock, patientRepositoryMock, patientMock);
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> service.checkForHypertension(bloodPressure.getId())
        );
        assertEquals("Pressão sanguínea inexistente.", exception.getMessage());
        verify(repositoryMock, patientRepositoryMock, patientMock);
    }

    @Test
    void shouldThrowAnExceptionIfPatientDoesNotExist() {
        expect(repositoryMock.findById(bloodPressure.getId())).andReturn(Optional.of(bloodPressure));
        expect(patientRepositoryMock.findById(patient.getId())).andReturn(Optional.empty());
        replay(repositoryMock, patientRepositoryMock, patientMock);
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> service.checkForHypertension(bloodPressure.getId()));
        assertEquals("Paciente inexistente", exception.getMessage());
        verify(repositoryMock, patientRepositoryMock, patientMock);
    }

    @Test
    void childWithHighSystolicBloodPressure() {
        bloodPressure.setSystolicBloodPressureLevel(111);
        bloodPressure.setDiastolicBloodPressureLevel(70);
        prepareMocksForExistingBloodPressure(14);
        String result = service.checkForHypertension(bloodPressure.getId());
        assertEquals("Criança com pressão alta", result);
        verify(repositoryMock, patientRepositoryMock, patientMock);
    }

    @Test
    void childWithHighDiastolicBloodPressure() {
        bloodPressure.setSystolicBloodPressureLevel(110);
        bloodPressure.setDiastolicBloodPressureLevel(71);
        prepareMocksForExistingBloodPressure(14);
        String result = service.checkForHypertension(bloodPressure.getId());
        assertEquals("Criança com pressão alta", result);
        verify(repositoryMock, patientRepositoryMock, patientMock);
    }

    @Test
    void childWithNormalBloodPressure() {
        bloodPressure.setSystolicBloodPressureLevel(110);
        bloodPressure.setDiastolicBloodPressureLevel(70);
        prepareMocksForExistingBloodPressure(14);
        String result = service.checkForHypertension(bloodPressure.getId());
        assertEquals("A pressão da criança não está alta", result);
        verify(repositoryMock, patientRepositoryMock, patientMock);
    }

    @Test
    void adultWithHighSystolicBloodPressure() {
        bloodPressure.setSystolicBloodPressureLevel(121);
        bloodPressure.setDiastolicBloodPressureLevel(80);
        prepareMocksForExistingBloodPressure(15);
        String result = service.checkForHypertension(bloodPressure.getId());
        assertEquals("Adulto com pressão alta", result);
        verify(repositoryMock, patientRepositoryMock, patientMock);
    }

    @Test
    void adultWithHighDiastolicBloodPressure() {
        bloodPressure.setSystolicBloodPressureLevel(120);
        bloodPressure.setDiastolicBloodPressureLevel(81);
        prepareMocksForExistingBloodPressure(15);
        String result = service.checkForHypertension(bloodPressure.getId());
        assertEquals("Adulto com pressão alta", result);
        verify(repositoryMock, patientRepositoryMock, patientMock);
    }

    @Test
    void adultWithNormalBloodPressure() {
        bloodPressure.setSystolicBloodPressureLevel(120);
        bloodPressure.setDiastolicBloodPressureLevel(80);
        prepareMocksForExistingBloodPressure(15);
        String result = service.checkForHypertension(bloodPressure.getId());
        assertEquals("A pressão do adulto não está alta", result);
        verify(repositoryMock, patientRepositoryMock, patientMock);
    }
}
