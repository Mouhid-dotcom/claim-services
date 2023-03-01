package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.insurancePaymentPosting.ChargesWRTClaimDto;
import rovermd.project.claimservices.dto.insurancePaymentPosting.ClaimIdAndTransactionId;
import rovermd.project.claimservices.dto.insurancePaymentPosting.ClaimsWRTCheckDto;

public interface ClaimPaymentPostingService {
    Object setCharges_WRT_Claim(Integer flag, ChargesWRTClaimDto chargeWRTClaims);
    Object setClaims_WRT_Checks(Integer flag, ClaimsWRTCheckDto claimsWRTCheckDto);
    Object getCharges_WRT_Claim( ClaimIdAndTransactionId claimIdAndTransactionId,Integer flag);
    Object getClaims_WRT_Check( ClaimIdAndTransactionId claimIdAndTransactionId,Integer flag);
}
