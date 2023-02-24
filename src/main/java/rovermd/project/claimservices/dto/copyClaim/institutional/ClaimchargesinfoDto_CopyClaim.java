package rovermd.project.claimservices.dto.copyClaim.institutional;

import jakarta.validation.constraints.Size;
import lombok.Data;
import rovermd.project.claimservices.dto.copyClaim.professional.ClaimchargesotherinfoDto_CopyClaim;
import rovermd.project.claimservices.entity.claimMaster.Claimchargesinfo;

import java.math.BigDecimal;

/**
 * A DTO for the {@link Claimchargesinfo} entity
 */
@Data
public class ClaimchargesinfoDto_CopyClaim {

    @Size(max = 255)
    private String descriptionFrom;
    @Size(max = 255)
    private String serviceDate;
    @Size(max = 255)
    private String hcpcs;
    @Size(max = 255)
    private String mod1;
    @Size(max = 255)
    private String mod2;
    @Size(max = 255)
    private String mod3;
    @Size(max = 255)
    private String mod4;
    @Size(max = 255)
    private String revCode;
    private BigDecimal unitPrice;
    private BigDecimal units;
    private BigDecimal amount;
    private Integer status;
    @Size(max = 255)
    private String chargesStatus;
    @Size(max = 255)
    private String hCPCSProcedure;


    ClaimchargesotherinfoDto_CopyClaim claimchargesotherinfo;

}