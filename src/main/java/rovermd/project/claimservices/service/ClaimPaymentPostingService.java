package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.APIResponse;
import rovermd.project.claimservices.dto.insurancePaymentPosting.Request.ChargesWRTClaimDto;
import rovermd.project.claimservices.dto.insurancePaymentPosting.Request.ClaimIdAndTransactionId;
import rovermd.project.claimservices.dto.insurancePaymentPosting.Request.ClaimsWRTCheckDto;

public interface ClaimPaymentPostingService {
    Object setCharges_WRT_Claim(Integer flag, ChargesWRTClaimDto chargeWRTClaims);
    Object setClaims_WRT_Checks(Integer flag, ClaimsWRTCheckDto claimsWRTCheckDto);
    Object getCharges_WRT_Claim( ClaimIdAndTransactionId claimIdAndTransactionId,Integer flag);
    Object getClaims_WRT_Check( ClaimIdAndTransactionId claimIdAndTransactionId,Integer flag);
}
