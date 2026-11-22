package br.edu.ifsudestemg.bgr.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "blood_pressure")
@Data
@EntityListeners(AuditingEntityListener.class)
public class BloodPressure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "systolic_blood_pressure_level")
    private Integer systolicBloodPressureLevel;

    @Column(name = "diastolic_blood_pressure_level")
    private Integer diastolicBloodPressureLevel;

    @Column(name = "registration_date_time")
    @CreatedDate
    private LocalDateTime registrationDateTime;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;
}