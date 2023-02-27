package rovermd.project.claimservices.dto.insurancePaymentPosting.Request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargesWRTClaimDto {
    private String action;
    private String tcn;
    private String claimControlNo;
    List<ChargeWRTClaimDto> charges;
}
