package rovermd.project.claimservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rovermd.project.claimservices.dto.ClaimAudittrailDto;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimServiceProfessional;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims/")
public class ClaimController {
    @Autowired
    private ClaimServiceProfessional claimService;

    @Autowired
    private ClaimAudittrailService claimAudittrailService;

    @GetMapping(value = { "/", "/{keyWord}" })
    public ResponseEntity<List<Map<Object,Object>>> getAllClaims(@PathVariable(required = false) String keyWord) {
        return ResponseEntity.ok(this.claimService.getAllClaims(keyWord));
    }

    @GetMapping(value ={"/patRegID/{patRegID}/visitID/{visitID}","visitID/{visitID}/patRegID/{patRegID}/","/patRegID/{patRegID}","visitID/{visitID}"})
    public ResponseEntity<List<Map<Object,Object>>> getAllClaims(@PathVariable(required = false) Integer patRegID,@PathVariable(required = false) Integer visitID) {
        return ResponseEntity.ok(this.claimService.getAllClaimsByPatRegIDAndVisitId(patRegID,visitID));
    }

    @GetMapping("auditTrails/{claimNumber}")
    public ResponseEntity<List<ClaimAudittrailDto>> getAuditTrails(@PathVariable String claimNumber) {
        return ResponseEntity.ok(claimAudittrailService.findAllByClaimNumber(claimNumber));
    }
}
