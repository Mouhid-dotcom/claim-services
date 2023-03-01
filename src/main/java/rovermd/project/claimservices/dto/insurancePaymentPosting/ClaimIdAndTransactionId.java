package rovermd.project.claimservices.dto.insurancePaymentPosting;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimIdAndTransactionId {
    Integer claimId;
    String transactionId;
}
