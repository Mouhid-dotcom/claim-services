package rovermd.project.claimservices.dto.viewSingleClaim.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claimadditionalinfo;

import java.io.Serializable;

/**
 * A DTO for the {@link Claimadditionalinfo} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimadditionalinfoDto_ViewSingleClaim implements Serializable {
    private Integer id;
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
}