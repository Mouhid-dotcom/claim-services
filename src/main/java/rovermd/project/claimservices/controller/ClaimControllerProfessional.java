package rovermd.project.claimservices.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rovermd.project.claimservices.dto.ClaimInfoMaster_List;
import rovermd.project.claimservices.dto.copyClaim.institutional.ClaiminfomasterInstDto_CopyClaim;
import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;
import rovermd.project.claimservices.dto.viewSingleClaim.professional.ClaiminfomasterProfDto_ViewSingleClaim;
import rovermd.project.claimservices.service.ClaimServiceProfessional;
import rovermd.project.claimservices.service.ClaimServiceSrubber;

import java.util.List;

@RestController
@RequestMapping("/api/claims/professional")
public class ClaimControllerProfessional {
    @Autowired
    private ClaimServiceProfessional claimService;

    @Autowired
    private ClaimServiceSrubber claimServiceSrubber;
    @PostMapping
    public ResponseEntity<ClaiminfomasterProfDto> createClaim(@RequestBody ClaiminfomasterProfDto claimDTO, HttpServletRequest request) {
        ClaiminfomasterProfDto claim = claimService.createClaim(claimDTO, request.getRemoteAddr());
        return new ResponseEntity<>(claim, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ClaiminfomasterProfDto> updateClaim(@RequestBody ClaiminfomasterProfDto claimDTO, HttpServletRequest request) {
        ClaiminfomasterProfDto claim = claimService.updateClaim(claimDTO, request.getRemoteAddr());
        return new ResponseEntity<>(claim, HttpStatus.CREATED);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<ClaiminfomasterProfDto_ViewSingleClaim> getClaimById(@PathVariable Integer claimId) {
        return ResponseEntity.ok(claimService.getClaimById(claimId));
    }

    @GetMapping("copyClaim/{claimId}")
    public ResponseEntity<ClaiminfomasterInstDto_CopyClaim> copyClaim(@PathVariable Integer claimId) {
        return ResponseEntity.ok(claimService.copyClaim(claimId));
    }

    @PostMapping("/scrubber")
    public ResponseEntity<List<String>> scrubber(@RequestBody ClaiminfomasterProfDto claimDTO, HttpServletRequest request) {
        List<String> res = claimServiceSrubber.scrubber(claimDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
