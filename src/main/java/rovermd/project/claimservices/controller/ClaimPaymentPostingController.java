package rovermd.project.claimservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rovermd.project.claimservices.dto.insurancePaymentPosting.ChargesWRTClaimDto;
import rovermd.project.claimservices.dto.insurancePaymentPosting.ClaimIdAndTransactionId;
import rovermd.project.claimservices.dto.insurancePaymentPosting.ClaimsWRTCheckDto;
import rovermd.project.claimservices.service.ClaimPaymentPostingService;

@RestController
@RequestMapping("/api/payments/")
public class ClaimPaymentPostingController {

    @Autowired
    ClaimPaymentPostingService claimPaymentPostingService;

    @PostMapping("postCharges/{flag}")
    public ResponseEntity<Object> paymentPostCharges(@RequestBody ChargesWRTClaimDto charges, @PathVariable Integer flag) {
        Object o = claimPaymentPostingService.setCharges_WRT_Claim(flag, charges);
        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    @PostMapping("postClaims/{flag}")
    public ResponseEntity<Object> paymentPostClaim(@RequestBody ClaimsWRTCheckDto claims, @PathVariable Integer flag) {
        Object o = claimPaymentPostingService.setClaims_WRT_Checks(flag, claims);
        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    @PostMapping("viewCharges/{flag}")
    public ResponseEntity<Object> getCharges_WRT_Claim(@RequestBody ClaimIdAndTransactionId claimIdAndTransactionId, @PathVariable Integer flag) {
        Object o = claimPaymentPostingService.getCharges_WRT_Claim(claimIdAndTransactionId,flag);
        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    @PostMapping("viewClaims/{flag}")
    public ResponseEntity<Object> getClaims_WRT_Check(@RequestBody ClaimIdAndTransactionId claimIdAndTransactionId, @PathVariable Integer flag) {
        Object o = claimPaymentPostingService.getClaims_WRT_Check(claimIdAndTransactionId,flag);
        return new ResponseEntity<>(o, HttpStatus.OK);
    }
}
