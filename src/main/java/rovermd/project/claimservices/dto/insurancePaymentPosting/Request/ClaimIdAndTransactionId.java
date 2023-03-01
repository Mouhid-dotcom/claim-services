package rovermd.project.claimservices.dto.insurancePaymentPosting.Request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimIdAndTransactionId {
    Integer claimId;
    Integer transactionId;
}
