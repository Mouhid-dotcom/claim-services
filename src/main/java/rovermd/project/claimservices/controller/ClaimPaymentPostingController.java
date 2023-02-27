package rovermd.project.claimservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rovermd.project.claimservices.dto.insurancePaymentPosting.Request.ChargesWRTClaimDto;
import rovermd.project.claimservices.service.ClaimPaymentPostingService;

@RestController
@RequestMapping("/api/payments/")
public class ClaimPaymentPostingController {

    @Autowired
    ClaimPaymentPostingService claimPaymentPostingService;

    @PostMapping("post/{flag}")
    public ResponseEntity<Object> paymentPost(@RequestBody ChargesWRTClaimDto charges, @PathVariable Integer flag) {
        Object o = claimPaymentPostingService.setCharges_WRT_Claim(flag, charges);
        return new ResponseEntity<>(o, HttpStatus.OK);
    }
}
