package rovermd.project.claimservices.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rovermd.project.claimservices.dto.ClaimAudittrailDto;
import rovermd.project.claimservices.dto.ClaimInfoMaster_List;
import rovermd.project.claimservices.dto.copyClaim.institutional.ClaiminfomasterInstDto_CopyClaim;
import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;
import rovermd.project.claimservices.dto.viewSingleClaim.professional.ClaiminfomasterProfDto_ViewSingleClaim;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimServiceProfessional;

import java.util.List;

@RestController
@RequestMapping("/api/claims/")
public class ClaimController {
    @Autowired
    private ClaimServiceProfessional claimService;

    @Autowired
    private ClaimAudittrailService claimAudittrailService;

    @GetMapping("/")
    public ResponseEntity<List<ClaimInfoMaster_List>> getAllClaims() {
        return ResponseEntity.ok(this.claimService.getAllClaims());
    }

    @GetMapping("auditTrails/{claimNumber}")
    public ResponseEntity<List<ClaimAudittrailDto>> getAuditTrails(@PathVariable String claimNumber) {
        return ResponseEntity.ok(claimAudittrailService.findAllByClaimNumber(claimNumber));
    }
}
