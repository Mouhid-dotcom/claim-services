package rovermd.project.claimservices.util;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import rovermd.project.claimservices.entity.ClaimAudittrail;
import rovermd.project.claimservices.entity.Claiminfomaster;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.serviceImpl.ClaimAudittrailServiceImpl;

@Component
public class AuditTrail {

    private static final Log log = LogFactory.getLog(AuditTrail.class);

//    @Autowired
//    private ClaimAudittrailService claimAudittrailService;

    @PostPersist
    private void afterCreation(Claiminfomaster claim) {
        ClaimAudittrailService claimAudittrailService = new ClaimAudittrailServiceImpl();
        ClaimAudittrail claimAudittrail = new ClaimAudittrail();
        claimAudittrail.setClaimNo(claim.getClaimNumber());
        claimAudittrail.setClaimType(String.valueOf(claim.getClaimType()));
        claimAudittrail.setUserID(claim.getCreatedBy());
        claimAudittrail.setClientID(String.valueOf(claim.getClientId()));
        claimAudittrail.setCreatedAt(claim.getCreatedDate());
        claimAudittrail.setUserIP(claim.getCreatedIP());
        claimAudittrail.setAction("CREATED");
        claimAudittrail.setRuleText("CLAIM SAVED");
        claimAudittrailService.createAuditTrail(claimAudittrail);

        log.info("[CLAIM AUDIT] CLAIM IS CREATED ID-> "+claim.getId());
    }

    @PostUpdate
    private void afterUpdation(Claiminfomaster claim) {
//        ClaimAudittrailService claimAudittrailService = new ClaimAudittrailServiceImpl();

//        ClaimAudittrail claimAudittrail = new ClaimAudittrail();
//        claimAudittrail.setClaimNo(claim.getClaimNumber());
//        claimAudittrail.setClaimType(String.valueOf(claim.getClaimType()));
//        claimAudittrail.setUserID(claim.getUpdatedBy());
//        claimAudittrail.setClientID(String.valueOf(claim.getClientId()));
//        claimAudittrail.setCreatedAt(claim.getUpdatedAt());
//        claimAudittrail.setUserIP(claim.getCreatedIP());
//        claimAudittrail.setAction("UPDATED");
//        claimAudittrail.setRuleText("CLAIM SAVED");
//        claimAudittrailService.createAuditTrail(claimAudittrail);
//        claimAudittrailService.savePerformanceCard(PerformanceCard.builder().performanceCardRequest(request).establishment(request.getEstablishment()).issuedOn(Instant.now()).performanceCardRequestStatus(UNASSIGNED).performanceCardGrade(PerformanceCardGrade.NON_COMPLIANT).performanceCardZone(PerformanceCardZone.RED).expirationDate(time).build());
//        claimAudittrailService.createAuditTrail(ClaimAudittrail.builder()
//                .claimNo(claim.getClaimNumber())
//                .claimType(String.valueOf(claim.getClaimType()))
//                .userID(claim.getCreatedBy())
//                .userIP(claim.getCreatedIP())
//                .clientID(String.valueOf(claim.getClientId()))
//                .userIP(claim.getCreatedIP())
//                .action("UPDATED")
//                .ruleText("CLAIM UPDATED").build());
        log.info("[CLAIM AUDIT] CLAIM IS UPDATED ID-> "+claim.getId());
    }

    @PostLoad
    private void afterLoad(Claiminfomaster claim) {
//        ClaimAudittrail claimAudittrail = new ClaimAudittrail();
//        claimAudittrail.setClaimNo(claim.getClaimNumber());
//        claimAudittrail.setClaimType(String.valueOf(claim.getClaimType()));
//        claimAudittrail.setUserID(claim.getCreatedBy());
//        claimAudittrail.setClientID(String.valueOf(claim.getClientId()));
//        claimAudittrail.setCreatedAt(claim.getCreatedDate());
//        claimAudittrail.setUserIP(claim.getCreatedIP());
//        claimAudittrail.setAction("OPENED");
//        claimAudittrail.setRuleText("CLAIM OPENED");
//        claimAudittrailService.createAuditTrail(claimAudittrail);

        log.info("[CLAIM AUDIT] CLAIM IS LOADED ID-> "+claim.getId());
    }


}