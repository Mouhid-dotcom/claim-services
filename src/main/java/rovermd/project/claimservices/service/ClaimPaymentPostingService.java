package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.insurancePaymentPosting.Request.ChargesWRTClaimDto;

public interface ClaimPaymentPostingService {

    Object setCharges_WRT_Claim(Integer flag, ChargesWRTClaimDto chargeWRTClaims);

}
