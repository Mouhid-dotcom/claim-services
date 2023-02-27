package rovermd.project.claimservices.dto.insurancePaymentPosting.Request;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimWRTCheckDto {
    private String patientName;
    private String acctNo;
    private String claimNo;
    private String pcn;
    private String dos;
    private BigDecimal billed;
    private BigDecimal allowed;
    private BigDecimal paid;
    private BigDecimal adjusted;
    private BigDecimal unpaid;
    private BigDecimal additionalActions;
    private BigDecimal balance;
}
