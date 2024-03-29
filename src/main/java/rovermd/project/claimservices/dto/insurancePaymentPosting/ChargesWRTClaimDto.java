package rovermd.project.claimservices.dto.insurancePaymentPosting;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargesWRTClaimDto {
    private String action;
    private String tcn;
    private String claimControlNo;
    private String checkNo;
    List<ChargeWRTClaimDto> charges;
}
