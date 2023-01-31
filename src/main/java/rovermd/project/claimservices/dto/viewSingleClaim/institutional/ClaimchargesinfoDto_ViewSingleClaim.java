package rovermd.project.claimservices.dto.viewSingleClaim.institutional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.constraints.Size;
import lombok.Data;
import rovermd.project.claimservices.dto.professional.ClaimchargesotherinfoDto;

import java.math.BigDecimal;

/**
 * A DTO for the {@link rovermd.project.claimservices.entity.Claimchargesinfo} entity
 */
@Data
public class ClaimchargesinfoDto_ViewSingleClaim {
    private Integer id;
    @Size(max = 255)
    private String descriptionFrom;
    @Size(max = 255)
    private String serviceDate;
    @Size(max = 255)
    private String hcpcs;
    @Size(max = 255)
    private ObjectNode mod1;
    @Size(max = 255)
    private ObjectNode mod2;
    @Size(max = 255)
    private ObjectNode mod3;
    @Size(max = 255)
    private ObjectNode mod4;
    @Size(max = 255)
    private ObjectNode revCode;
    private BigDecimal unitPrice;
    private BigDecimal units;
    private BigDecimal amount;
    private String status;
    @Size(max = 255)
    private ObjectNode chargesStatus;
    @Size(max = 255)
    private ObjectNode hCPCSProcedure;


    ClaimchargesotherinfoDto claimchargesotherinfo;

}