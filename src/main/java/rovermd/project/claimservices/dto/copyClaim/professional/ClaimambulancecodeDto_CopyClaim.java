package rovermd.project.claimservices.dto.copyClaim.professional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link Claimambulancecode} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimambulancecodeDto_CopyClaim implements Serializable {

    @Size(max = 255)
    private String ambClaimInfoCodes;
    @Size(max = 255)
    private String tranReasonInfoCodes;
    @Size(max = 255)
    private String tranMilesInfoCodes;
    @Size(max = 255)
    private String patWeightInfoCodes;
    private String roundTripReasInfoCodes;
    private String stretReasonInfoCodes;
    @Size(max = 255)
    private String pickUpAddressInfoCode;
    @Size(max = 255)
    private String pickUpCityInfoCode;
    @Size(max = 255)
    private String pickUpStateInfoCode;
    @Size(max = 255)
    private String pickUpZipCodeInfoCode;
    @Size(max = 255)
    private String dropoffAddressInfoCode;
    @Size(max = 255)
    private String dropoffCityInfoCode;
    @Size(max = 255)
    private String dropoffStateInfoCode;
    @Size(max = 255)
    private String dropoffZipCodeInfoCode;
    @Size(max = 255)
    private String patAdmitHosChk;
    @Size(max = 255)
    private String patMoveStretChk;
    @Size(max = 255)
    private String patUnconShockChk;
    @Size(max = 255)
    private String patTransEmerSituaChk;
    @Size(max = 255)
    private String patPhyRestrainChk;
    @Size(max = 255)
    private String patvisiblehemorrChk;
    @Size(max = 255)
    private String ambSerNeccChk;
    @Size(max = 255)
    private String patconfbedchairChk;
}