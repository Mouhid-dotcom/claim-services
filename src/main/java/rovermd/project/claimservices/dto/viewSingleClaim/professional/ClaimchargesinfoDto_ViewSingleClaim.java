package rovermd.project.claimservices.dto.viewSingleClaim.professional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * A DTO for the {@link rovermd.project.claimservices.entity.Claimchargesinfo} entity
 */
@Data
public class ClaimchargesinfoDto_ViewSingleClaim {
    private Integer id;
    @Size(max = 255)
    private ObjectNode mod1;
    @Size(max = 255)
    private ObjectNode mod2;
    @Size(max = 255)
    private ObjectNode mod3;
    @Size(max = 255)
    private ObjectNode mod4;
    private BigDecimal unitPrice;
    private BigDecimal units;
    private BigDecimal amount;

    private Integer status;
    @Size(max = 255)
    private ObjectNode chargesStatus;
    @Size(max = 255)
    private String icda;
    @Size(max = 255)
    private String icdb;
    @Size(max = 255)
    private String icdc;
    @Size(max = 255)
    private String icdd;
    @Size(max = 255)
    private String icde;
    @Size(max = 255)
    private String icdf;
    @Size(max = 255)
    private String icdg;
    @Size(max = 255)
    private String icdh;
    @Size(max = 255)
    private String icdi;
    @Size(max = 255)
    private String icdj;
    @Size(max = 255)
    private String icdk;
    @Size(max = 255)
    private String icdl;
    @Size(max = 255)
    private String serviceFromDate;
    @Size(max = 255)
    private String serviceToDate;
    @Size(max = 255)
    private ObjectNode hCPCSProcedure;
    @Size(max = 255)
    private ObjectNode pos;
    @Size(max = 255)
    private ObjectNode tos;
    @Size(max = 255)
    private String dXPointer;


    ClaimchargesotherinfoDto_ViewSingleClaim claimchargesotherinfo;
}