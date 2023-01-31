package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.ClaimAudittrailDto;
import rovermd.project.claimservices.entity.ClaimAudittrail;

import java.util.List;

public interface ClaimAudittrailService {
    ClaimAudittrail createAuditTrail(ClaimAudittrail claimAudittrail);

    List<ClaimAudittrailDto> findAllByClaimNumber(String ClaimNumber);


}

