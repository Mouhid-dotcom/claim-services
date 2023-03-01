package rovermd.project.claimservices.dto.insurancePaymentPosting;


import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePaymentDto {
    private Long paymentBy;
    private Long paymentFrom;
    private String amount;
    private String recievedDate;
    private String checkNo;
    private String otherRefNo;
    private String paymentSource;
    private String cardType;
    private String appliedAmount;
    private String unAppliedAmount;
    private Integer insuranceId;

    private String insuranceName;


}
