package rovermd.project.claimservices.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rovermd.project.claimservices.dto.SuccessMsg;
import rovermd.project.claimservices.dto.copyClaim.professional.ClaiminfomasterProfDto_CopyClaim;
import rovermd.project.claimservices.dto.institutional.ClaiminfomasterInstDto;
import rovermd.project.claimservices.dto.ub04.UB04DTO;
import rovermd.project.claimservices.dto.viewSingleClaim.institutional.ClaiminfomasterInstDto_ViewSingleClaim;
import rovermd.project.claimservices.service.ClaimServiceEDI;
import rovermd.project.claimservices.service.ClaimServiceInstitutional;
import rovermd.project.claimservices.service.ClaimServiceSrubber;
import rovermd.project.claimservices.entity.claimMaster.Claiminfomaster;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/claims/institutional")
public class ClaimControllerInstitutional {
    @Autowired
    private ClaimServiceInstitutional claimService;

    @Autowired
    private ClaimServiceSrubber claimServiceSrubber;

    @Autowired
    private ClaimServiceEDI claimServiceEDI;

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

    @GetMapping("/edi/{claimId}")
    public ResponseEntity<?> edi(@PathVariable Integer claimId) throws ParseException, IOException {
        Object ediProf = claimServiceEDI.createEDI_Inst(claimId);
        if(ediProf==null ){
            SuccessMsg successMsg = new SuccessMsg();
            successMsg.setStatuscode("OK");
            successMsg.setMessage("Edi Generated Successfully");
            return new ResponseEntity<>(successMsg, HttpStatus.OK);

        }else{
            return new ResponseEntity<>(ediProf, HttpStatus.OK);
        }
    }


    @GetMapping("/ub04/{claimId}")
    public ResponseEntity<UB04DTO> ub04(@PathVariable Integer claimId) {
        UB04DTO claim = claimService.ub04(claimId);
        return new ResponseEntity<>(claim, HttpStatus.CREATED);
    }

}
