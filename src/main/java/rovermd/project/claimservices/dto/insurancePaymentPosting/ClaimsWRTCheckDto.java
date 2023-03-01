package rovermd.project.claimservices.dto.insurancePaymentPosting;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimsWRTCheckDto {
    private InsurancePaymentDto insurancePayment;
    private List<ClaimWRTCheckDto> claims;
}
