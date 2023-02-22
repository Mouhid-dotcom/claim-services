package rovermd.project.claimservices.dto.professional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A DTO for the {@link rovermd.project.claimservices.entity.Claimadditionalinfo} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimadditionalinfoDto implements Serializable {
    private Integer id;

    @Size(max = 255)
    private String employmentStatusAddInfo;
    @Size(max = 255)
    private String autoAccidentAddInfo;
    @Size(max = 255)
    private String otherAccidentAddInfo;
    @Size(max = 255)
    private String autoaccidentStateaddinfo;
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