package rovermd.project.claimservices.dto.copyClaim.professional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claimchargesotherinfo;

import java.io.Serializable;

/**
 * A DTO for the {@link Claimchargesotherinfo} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimchargesotherinfoDto_CopyClaim implements Serializable {

    @Size(max = 255)
    private String nonCoveredAmount;
    @Size(max = 255)
    private String drugCode;
    @Size(max = 255)
    private String drugUnit;
    @Size(max = 255)
    private String unit;
    @Size(max = 255)
    private String drugDays;
    @Size(max = 255)
    private String drugCodeFormat;
    @Size(max = 255)
    private String drugPrice;
    @Size(max = 255)
    private String prescriptionNum;
    @Size(max = 255)
    private String prescriptionDate;
    @Size(max = 255)
    private String prescriptionMonths;

}