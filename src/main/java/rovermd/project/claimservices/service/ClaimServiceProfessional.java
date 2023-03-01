package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.cms1500.CMS1500DTO;
import rovermd.project.claimservices.dto.copyClaim.institutional.ClaiminfomasterInstDto_CopyClaim;
import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;
import rovermd.project.claimservices.dto.viewSingleClaim.professional.ClaiminfomasterProfDto_ViewSingleClaim;

import java.util.List;
import java.util.Map;

public interface ClaimServiceProfessional {
    List<Map<Object, Object>> getAllClaims(String key);

    List<Map<Object, Object>> getAllClaimsByPatRegIDAndVisitId(Integer patRegID, Integer visitID);

    ClaiminfomasterProfDto createClaim(ClaiminfomasterProfDto claimDto, String remoteAddr);

    ClaiminfomasterProfDto updateClaim(ClaiminfomasterProfDto claimDto, String remoteAddr);

    ClaiminfomasterProfDto_ViewSingleClaim getClaimById(Integer claimId);

    ClaiminfomasterInstDto_CopyClaim copyClaim(Integer claimID);

    CMS1500DTO cms1500(Integer claimID);

}
