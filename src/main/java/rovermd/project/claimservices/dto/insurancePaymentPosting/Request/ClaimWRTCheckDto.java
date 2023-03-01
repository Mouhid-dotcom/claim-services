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
    private String billed;
    private String allowed;
    private String paid;
    private String adjusted;
    private String unpaid;
    private String additionalActions;
    private String balance;
}
