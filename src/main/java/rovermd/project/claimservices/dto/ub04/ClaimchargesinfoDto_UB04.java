package rovermd.project.claimservices.dto.ub04;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.constraints.Size;
import lombok.Data;
import rovermd.project.claimservices.entity.claimMaster.Claimchargesinfo;

import java.math.BigDecimal;

/**
 * A DTO for the {@link Claimchargesinfo} entity
 */
@Data
public class ClaimchargesinfoDto_UB04 {
    @Size(max = 255)
    private String serviceDate;

    @Size(max = 255)
    private String mod1;
    @Size(max = 255)
    private String mod2;
    @Size(max = 255)
    private String mod3;
    @Size(max = 255)
    private String mod4;
    @Size(max = 255)
    private ObjectNode revCode;
    private BigDecimal units;
    private BigDecimal amount;
    @Size(max = 255)
    private String hCPCSProcedure;
}