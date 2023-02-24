package rovermd.project.claimservices.entity.claimMaster;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "claimadditionalinfo")
@NoArgsConstructor
@Getter
@Setter
public class Claimadditionalinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "ClaimNumber")
    private String claimNumber;

//    @JsonBackReference
//    @JsonIgnore
//    @OneToOne
//    @JoinColumn(name="ClaimInfoMasterId", referencedColumnName="Id",updatable = false)
//    Claiminfomaster claiminfomaster;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id")
    Claiminfomaster claiminfomaster;

    @Size(max = 255)
    @Column(name = "StatmentCoverFromDateAddInfo")
    private String statmentCoverFromDateAddInfo;

    @Size(max = 255)
    @Column(name = "StatmentCoverToDateAddInfo")
    private String statmentCoverToDateAddInfo;

    @Size(max = 255)
    @Column(name = "AdmissionDateAddInfo")
    private String admissionDateAddInfo;

    @Size(max = 255)
    @Column(name = "AdmissionHourAddInfo")
    private String admissionHourAddInfo;

    @Size(max = 255)
    @Column(name = "AdmissionTypeAddInfo")
    private String admissionTypeAddInfo;

    @Size(max = 255)
    @Column(name = "AdmissionSourceAddInfo")
    private String admissionSourceAddInfo;

    @Size(max = 255)
    @Column(name = "DischargeHourAddInfo")
    private String dischargeHourAddInfo;

    @Size(max = 255)
    @Column(name = "PatientStatusAddInfo")
    private String patientStatusAddInfo;

    @Size(max = 255)
    @Column(name = "DelayReasonCodeAddInfo")
    private String delayReasonCodeAddInfo;

    @Size(max = 255)
    @Column(name = "EmploymentStatusAddInfo")
    private String employmentStatusAddInfo;

    @Size(max = 255)
    @Column(name = "AutoAccidentAddInfo")
    private String autoAccidentAddInfo;

    @Size(max = 255)
    @Column(name = "OtherAccidentAddInfo")
    private String otherAccidentAddInfo;

    @Column(name = "Status")
    private Integer status;


    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Size(max = 255)
    @Column(name = "CreatedBy")
    private String createdBy;

    @Size(max = 255)
    @Column(name = "CreatedIP")
    private String createdIP;

    @Size(max = 255)
    @Column(name = "PPSAddInfo")
    private String pPSAddInfo;

    @Lob
    @Column(name = "RemarksAddInfo")
    private String remarksAddInfo;

    @Size(max = 255)
    @Column(name = "AutoAccident_StateAddInfo")
    private String autoaccidentStateaddinfo;

    @Size(max = 255)
    @Column(name = "ReleaseInfoAddInfo")
    private String releaseInfoAddInfo;

    @Size(max = 255)
    @Column(name = "AssofBenifitAddInfo")
    private String assofBenifitAddInfo;

    @Size(max = 255)
    @Column(name = "ProvAccAssigAddInfo")
    private String provAccAssigAddInfo;

    @Column(name = "AccidentIllnesDateAddInfo")
    private LocalDate accidentIllnesDateAddInfo;

    @Column(name = "LastMenstrualPeriodDateAddInfo")
    private LocalDate lastMenstrualPeriodDateAddInfo;

    @Column(name = "InitialTreatDateAddInfo")
    private LocalDate initialTreatDateAddInfo;

    @Column(name = "LastSeenDateAddInfo")
    private LocalDate lastSeenDateAddInfo;

    @Size(max = 255)
    @Column(name = "UnabletoWorkFromDateAddInfo")
    private String unabletoWorkFromDateAddInfo;

    @Size(max = 255)
    @Column(name = "UnabletoWorkToDateAddInfo")
    private String unabletoWorkToDateAddInfo;

    @Size(max = 255)
    @Column(name = "PatHomeboundAddInfo")
    private String patHomeboundAddInfo;

    @Size(max = 255)
    @Column(name = "ClaimCodesAddinfo")
    private String claimCodesAddinfo;

    @Size(max = 255)
    @Column(name = "OtherClaimIDAddinfo")
    private String otherClaimIDAddinfo;

    @Lob
    @Column(name = "ClaimNoteAddinfo")
    private String claimNoteAddinfo;

    @Lob
    @Column(name = "ResubmitReasonCodeAddinfo")
    private String resubmitReasonCodeAddinfo;

    @Size(max = 255)
    @Column(name = "HospitalizedFromDateAddInfo")
    private String hospitalizedFromDateAddInfo;

    @Size(max = 255)
    @Column(name = "HospitalizedToDateAddInfo")
    private String hospitalizedToDateAddInfo;

    @Size(max = 255)
    @Column(name = "LabChargesAddInfo")
    private String labChargesAddInfo;

    @Size(max = 255)
    @Column(name = "SpecialProgCodeAddInfo")
    private String specialProgCodeAddInfo;

    @Size(max = 255)
    @Column(name = "PatientSignOnFileAddInfo")
    private String patientSignOnFileAddInfo;

    @Size(max = 255)
    @Column(name = "InsuredSignOnFileAddInfo")
    private String insuredSignOnFileAddInfo;

    @Size(max = 255)
    @Column(name = "PXCTaxQualiAddInfo")
    private String pXCTaxQualiAddInfo;

    @Size(max = 255)
    @Column(name = "DocumentationMethodAddInfo")
    private String documentationMethodAddInfo;

    @Size(max = 255)
    @Column(name = "DocumentationTypeAddInfo")
    private String documentationTypeAddInfo;

    @Size(max = 255)
    @Column(name = "PatientHeightAddInfo")
    private String patientHeightAddInfo;

    @Size(max = 255)
    @Column(name = "PatientWeightAddInfo")
    private String patientWeightAddInfo;

    @Size(max = 255)
    @Column(name = "ServAuthExcepAddInfo")
    private String servAuthExcepAddInfo;

    @Size(max = 255)
    @Column(name = "DemoProjectAddInfo")
    private String demoProjectAddInfo;

    @Size(max = 255)
    @Column(name = "MemmoCertAddInfo")
    private String memmoCertAddInfo;

    @Size(max = 255)
    @Column(name = "InvDevExempAddInfo")
    private String invDevExempAddInfo;

    @Size(max = 255)
    @Column(name = "AmbPatGrpAddInfo")
    private String ambPatGrpAddInfo;

    @Size(max = 255)
    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "UpdatedAt")
    private Instant updatedAt;

    @PrePersist
    public void added() {
        createdDate = new Date().toInstant();
        createdBy = "MOUHID_CREATED";

    }

    @PreUpdate
    public void Updated() {
        updatedAt = new Date().toInstant();
        updatedBy = "MOUHID_UPDATED";

    }

}