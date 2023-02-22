package rovermd.project.claimservices.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rovermd.project.claimservices.dto.cms1500.CMS1500DTO;
import rovermd.project.claimservices.dto.SuccessMsg;
import rovermd.project.claimservices.dto.copyClaim.institutional.ClaiminfomasterInstDto_CopyClaim;
import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;
import rovermd.project.claimservices.dto.viewSingleClaim.professional.ClaiminfomasterProfDto_ViewSingleClaim;
import rovermd.project.claimservices.entity.Claiminfomaster;
import rovermd.project.claimservices.service.ClaimServiceEDI;
import rovermd.project.claimservices.service.ClaimServiceProfessional;
import rovermd.project.claimservices.service.ClaimServiceSrubber;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/claims/professional")
public class ClaimControllerProfessional {
    @Autowired
    private ClaimServiceProfessional claimService;

    @Autowired
    private ClaimServiceSrubber claimServiceSrubber;

    @Autowired
    private ClaimServiceEDI claimServiceEDI;

    @PostMapping
    public ResponseEntity<ClaiminfomasterProfDto> createClaim(@RequestBody ClaiminfomasterProfDto claimDTO, HttpServletRequest request) {
        ClaiminfomasterProfDto successMsg = claimService.createClaim(claimDTO, request.getRemoteAddr());
        return new ResponseEntity<>(successMsg, HttpStatus.CREATED);
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
    public ResponseEntity<List<?>> scrubber(@RequestBody Claiminfomaster claim) {
        List<?> res = claimServiceSrubber.scrubberProf(claim);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/edi/{claimId}")
    public ResponseEntity<?> edi(@PathVariable Integer claimId) throws ParseException, IOException {
        Object ediProf = claimServiceEDI.createEDI_Prof(claimId);
        if(ediProf==null){
            SuccessMsg successMsg = new SuccessMsg();
            successMsg.setStatuscode("OK");
            successMsg.setMessage("Edi Generated Successfully");
            return new ResponseEntity<>(successMsg, HttpStatus.OK);

        }else{
            return new ResponseEntity<>(ediProf, HttpStatus.OK);
        }
    }

    @GetMapping("/cms1500/{claimId}")
    public ResponseEntity<CMS1500DTO> cms1500(@PathVariable Integer claimId) throws ParseException, IOException {
        CMS1500DTO claim = claimService.cms1500(claimId);
        return new ResponseEntity<>(claim, HttpStatus.CREATED);
    }
}
