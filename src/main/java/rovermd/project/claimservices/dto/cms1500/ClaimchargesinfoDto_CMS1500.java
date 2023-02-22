package rovermd.project.claimservices.dto.cms1500;

import jakarta.validation.constraints.Size;
import lombok.Data;
import rovermd.project.claimservices.dto.professional.ClaimchargesotherinfoDto;

import java.math.BigDecimal;

/**
 * A DTO for the {@link rovermd.project.claimservices.entity.Claimchargesinfo} entity
 */
@Data
public class ClaimchargesinfoDto_CMS1500 {
    @Size(max = 255)
    private String mod1;
    @Size(max = 255)
    private String mod2;
    @Size(max = 255)
    private String mod3;
    @Size(max = 255)
    private String mod4;
    private BigDecimal unitPrice;
    private BigDecimal units;
    private BigDecimal amount;
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
    private String hCPCSProcedure;
    @Size(max = 255)
    private String pos;
    @Size(max = 255)
    private String tos;
    @Size(max = 255)
    private String dXPointer;

}