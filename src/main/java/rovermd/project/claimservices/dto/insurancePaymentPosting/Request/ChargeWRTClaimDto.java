package rovermd.project.claimservices.dto.insurancePaymentPosting.Request;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeWRTClaimDto {
    private String claimNo;
    private String charges;
    private BigDecimal amount;
    private BigDecimal startBalance;
    private BigDecimal allowed;
    private BigDecimal paid;
    private String remarks;
    private String adjReasons;
    private BigDecimal adjusted;
    private BigDecimal sequestrationAmt;
    private String unpaidReasons;
    private BigDecimal unpaid;
    private BigDecimal deductible;
    private Integer status;
    private BigDecimal otherCredits;
    private BigDecimal endBalance;
    private Integer chargeIdx;
    private Integer claimIdx;
    private String transactionIdx;
}
