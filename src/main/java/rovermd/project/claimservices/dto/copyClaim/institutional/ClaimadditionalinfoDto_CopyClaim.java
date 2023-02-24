package rovermd.project.claimservices.dto.copyClaim.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claimadditionalinfo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link Claimadditionalinfo} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimadditionalinfoDto_CopyClaim implements Serializable {

    @Size(max = 255)
    private String statmentCoverFromDateAddInfo;
    @Size(max = 255)
    private String statmentCoverToDateAddInfo;
    @Size(max = 255)
    private String admissionDateAddInfo;
    @Size(max = 255)
    private String admissionHourAddInfo;
    @Size(max = 255)
    private String admissionTypeAddInfo;
    @Size(max = 255)
    private String admissionSourceAddInfo;
    @Size(max = 255)
    private String dischargeHourAddInfo;
    @Size(max = 255)
    private String patientStatusAddInfo;
    @Size(max = 255)
    private String delayReasonCodeAddInfo;
    @Size(max = 255)
    private String employmentStatusAddInfo;
    @Size(max = 255)
    private String autoAccidentAddInfo;
    @Size(max = 255)
    private String otherAccidentAddInfo;
    @Size(max = 255)
    private String pPSAddInfo;
    private String remarksAddInfo;
    @Size(max = 255)
    private String autoaccidentStateaddinfo;
    @Size(max = 255)
    private String releaseInfoAddInfo;
    @Size(max = 255)
    private String assofBenifitAddInfo;
    @Size(max = 255)
    private String provAccAssigAddInfo;
    private LocalDate accidentIllnesDateAddInfo;
    private LocalDate lastMenstrualPeriodDateAddInfo;
    private LocalDate initialTreatDateAddInfo;
    private LocalDate lastSeenDateAddInfo;
    @Size(max = 255)
    private String unabletoWorkFromDateAddInfo;
    @Size(max = 255)
    private String unabletoWorkToDateAddInfo;
    @Size(max = 255)
    private String patHomeboundAddInfo;
    @Size(max = 255)
    private String claimCodesAddinfo;
    @Size(max = 255)
    private String otherClaimIDAddinfo;
    private String claimNoteAddinfo;
    private String resubmitReasonCodeAddinfo;
    @Size(max = 255)
    private String hospitalizedFromDateAddInfo;
    @Size(max = 255)
    private String hospitalizedToDateAddInfo;
    @Size(max = 255)
    private String labChargesAddInfo;
    @Size(max = 255)
    private String specialProgCodeAddInfo;
    @Size(max = 255)
    private String patientSignOnFileAddInfo;
    @Size(max = 255)
    private String insuredSignOnFileAddInfo;
    @Size(max = 255)
    private String pXCTaxQualiAddInfo;
    @Size(max = 255)
    private String documentationMethodAddInfo;
    @Size(max = 255)
    private String documentationTypeAddInfo;
    @Size(max = 255)
    private String patientHeightAddInfo;
    @Size(max = 255)
    private String patientWeightAddInfo;
    @Size(max = 255)
    private String servAuthExcepAddInfo;
    @Size(max = 255)
    private String demoProjectAddInfo;
    @Size(max = 255)
    private String memmoCertAddInfo;
    @Size(max = 255)
    private String invDevExempAddInfo;
    @Size(max = 255)
    private String ambPatGrpAddInfo;

}