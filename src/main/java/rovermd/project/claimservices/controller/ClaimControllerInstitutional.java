package rovermd.project.claimservices.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rovermd.project.claimservices.dto.ClaimAudittrailDto;
import rovermd.project.claimservices.dto.copyClaim.professional.ClaiminfomasterProfDto_CopyClaim;
import rovermd.project.claimservices.dto.institutional.ClaiminfomasterInstDto;
import rovermd.project.claimservices.dto.viewSingleClaim.institutional.ClaiminfomasterInstDto_ViewSingleClaim;
import rovermd.project.claimservices.entity.Claiminfomaster;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimServiceInstitutional;
import rovermd.project.claimservices.service.ClaimServiceSrubber;

import java.util.List;

@RestController
@RequestMapping("/api/claims/institutional")
public class ClaimControllerInstitutional {
    @Autowired
    private ClaimServiceInstitutional claimService;

    @Autowired
    private ClaimServiceSrubber claimServiceSrubber;

    @PostMapping
    public ResponseEntity<ClaiminfomasterInstDto> createClaim(@RequestBody ClaiminfomasterInstDto claimDTO, HttpServletRequest request) {
        ClaiminfomasterInstDto claim = claimService.createClaim(claimDTO, request.getRemoteAddr());
        return new ResponseEntity<>(claim, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ClaiminfomasterInstDto> updateClaim(@RequestBody ClaiminfomasterInstDto claimDTO, HttpServletRequest request) {
        ClaiminfomasterInstDto claim = claimService.updateClaim(claimDTO, request.getRemoteAddr());
        return new ResponseEntity<>(claim, HttpStatus.CREATED);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<ClaiminfomasterInstDto_ViewSingleClaim> getClaimById(@PathVariable Integer claimId) {
        return ResponseEntity.ok(claimService.getClaimById(claimId));
    }

    @GetMapping("copyClaim/{claimId}")
    public ResponseEntity<ClaiminfomasterProfDto_CopyClaim> copyClaim(@PathVariable Integer claimId) {
        return ResponseEntity.ok(claimService.copyClaim(claimId));
    }

    @PostMapping("/scrubber")
    public ResponseEntity<List<?>> scrubber(@RequestBody Claiminfomaster claim) {
        List<?> res = claimServiceSrubber.scrubberInst(claim);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
