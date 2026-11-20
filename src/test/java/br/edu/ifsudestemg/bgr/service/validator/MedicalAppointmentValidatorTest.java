package br.edu.ifsudestemg.bgr.service.validator;

import br.edu.ifsudestemg.bgr.exception.BusinessRuleException;
import br.edu.ifsudestemg.bgr.model.entity.Doctor;
import br.edu.ifsudestemg.bgr.model.entity.MedicalAppointment;
import br.edu.ifsudestemg.bgr.model.entity.MedicalOffice;
import br.edu.ifsudestemg.bgr.model.entity.Patient;
import br.edu.ifsudestemg.bgr.model.enums.MedicalAppointmentStatus;
import br.edu.ifsudestemg.bgr.model.repository.DoctorRepository;
import br.edu.ifsudestemg.bgr.model.repository.MedicalAppointmentRepository;
import br.edu.ifsudestemg.bgr.model.repository.MedicalOfficeRepository;
import br.edu.ifsudestemg.bgr.model.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentValidatorTest {

    MedicalAppointment medicalAppointment = new MedicalAppointment();
    Patient patient = new Patient();
    Doctor doctor = new Doctor();
    MedicalOffice address = new MedicalOffice();

    @Mock
    private MedicalAppointmentRepository repository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private MedicalOfficeRepository addressRepository;

    @InjectMocks
    private MedicalAppointmentValidator validator;

    @BeforeEach
    void setUp() {
        medicalAppointment.setId(UUID.randomUUID());

        patient.setId(UUID.randomUUID());
        medicalAppointment.setPatient(patient);

        doctor.setId(UUID.randomUUID());
        medicalAppointment.setDoctor(doctor);

        address.setId(UUID.randomUUID());
        medicalAppointment.setAddress(address);
    }

    @Test
    void mustCheckForNullPatient() {
        medicalAppointment.setPatient(null);
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("A consulta médica deve estar vinculada a um paciente válido.", e.getMessage());
        }
    }

    @Test
    void mustCheckForNullPatientId() {
        patient.setId(null);
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("A consulta médica deve estar vinculada a um paciente válido.", e.getMessage());
        }
    }

    @Test
    void mustCheckForNullDoctor() {
        medicalAppointment.setDoctor(null);
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("A consulta médica deve estar vinculada a um médico válido.", e.getMessage());
        }
    }

    @Test
    void mustCheckForNullDoctorId() {
        doctor.setId(null);
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("A consulta médica deve estar vinculada a um médico válido.", e.getMessage());
        }
    }

    @Test
    void mustCheckForNullAddress() {
        medicalAppointment.setAddress(null);
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("A consulta médica deve estar vinculada a um endereço válido.", e.getMessage());
        }
    }

    @Test
    void mustCheckForNullAddressId() {
        address.setId(null);
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("A consulta médica deve estar vinculada a um endereço válido.", e.getMessage());
        }
    }

    @Test
    void mustCheckForPatientDoesNotExistInTheDatabase() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.empty());
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("Paciente inválido", e.getMessage());
        }
    }

    @Test
    void mustCheckForDoctorDoesNotExistInTheDatabase() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.empty());
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("Médico inválido", e.getMessage());
        }
    }

    @Test
    void mustCheckForAddressDoesNotExistInTheDatabase() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(addressRepository.findById(address.getId())).thenReturn(Optional.empty());
        try {
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("Endereço inválido", e.getMessage());
        }
    }

    @Test
    void mustCheckForInvalidMedicalAppointmentStatus() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        medicalAppointment.setId(null);
        try {
            medicalAppointment.setStatus(MedicalAppointmentStatus.CANCELED);
            validator.validate(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("Status não permitido!", e.getMessage());
        }
    }

    @Test
    void shouldAllowInvalidInitialStatusWhenMedicalAppointmentIdIsNotNull() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        medicalAppointment.setId(UUID.randomUUID());
        medicalAppointment.setStatus(MedicalAppointmentStatus.CANCELED);
        try {
            validator.validate(medicalAppointment);
        } catch (BusinessRuleException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void validateShouldPassWithoutThrowingExceptions() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        medicalAppointment.setId(null);
        medicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        try {
            validator.validate(medicalAppointment);
        } catch (BusinessRuleException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void shouldCheckForNonExistentMedicalAppointment() {
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.empty());
        try {
            validator.validateAppointmentStatusChange(medicalAppointment);
            fail();
        } catch (BusinessRuleException e) {
            assertEquals("Consulta médica inexistente!", e.getMessage());
        }
    }

    @Test
    void itShouldAllowChangingTheStatusToItself() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertTrue(result);
    }

    @Test
    void itShouldNotAllowChangingTheCanceledStatus() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.CANCELED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertFalse(result);
    }

    @Test
    void itShouldNotAllowChangingTheCompletedStatus() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.COMPLETED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertFalse(result);
    }

    @Test
    void itShouldNotAllowChangingTheMissedStatus() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.MISSED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertFalse(result);
    }

    @Test
    void itShouldAllowChangingTheStatusFromScheduledToCanceled() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.CANCELED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertTrue(result);
    }

    @Test
    void itShouldAllowChangingTheStatusFromScheduledToConfirmed() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.CONFIRMED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertTrue(result);
    }

    @Test
    void itShouldNotAllowChangingTheStatusFromScheduledToOtherThanCanceledOrConfirmed() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.COMPLETED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertFalse(result);
    }

    @Test
    void itShouldNotAllowChangingTheStatusToScheduled() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.SCHEDULED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.CONFIRMED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertFalse(result);
    }

    @Test
    void itShouldAllowChangingTheStatusFromConfirmedToCanceled() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.CANCELED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.CONFIRMED);
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertTrue(result);
    }

    @Test
    void itShouldNotBePossibleChangeStatusFromConfirmedToCompletedOrMissedBeforeDayOfTheAppointment() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.COMPLETED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.CONFIRMED);
        oldMedicalAppointment.setDateTime(LocalDateTime.now().plusMinutes(1));
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertFalse(result);
    }

    @Test
    void itShouldAllowChangingStatusFromConfirmedToCompletedOrMissedAfterAppointmentTime() {
        medicalAppointment.setStatus(MedicalAppointmentStatus.MISSED);
        MedicalAppointment oldMedicalAppointment = new MedicalAppointment();
        oldMedicalAppointment.setStatus(MedicalAppointmentStatus.CONFIRMED);
        oldMedicalAppointment.setDateTime(LocalDateTime.now().minusMinutes(1));
        when(repository.findById(medicalAppointment.getId())).thenReturn(Optional.of(oldMedicalAppointment));
        boolean result = validator.validateAppointmentStatusChange(medicalAppointment);
        assertTrue(result);
    }

}