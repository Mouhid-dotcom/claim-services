package rovermd.project.claimservices.dto.insurancePaymentPosting.Request;


import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePaymentDto {
    private Long paymentBy;
    private Long paymentFrom;
    private BigDecimal amount;
    private String recievedDate;
    private String checkNo;
    private String otherRefNo;
    private String paymentSource;
    private String cardType;
}
