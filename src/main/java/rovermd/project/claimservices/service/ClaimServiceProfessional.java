package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.ClaimInfoMaster_List;
import rovermd.project.claimservices.dto.SuccessMsg;
import rovermd.project.claimservices.dto.copyClaim.institutional.ClaiminfomasterInstDto_CopyClaim;
import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;
import rovermd.project.claimservices.dto.viewSingleClaim.professional.ClaiminfomasterProfDto_ViewSingleClaim;

import java.util.List;

public interface ClaimServiceProfessional {
    List<ClaimInfoMaster_List> getAllClaims(String key);
    List<ClaimInfoMaster_List> getAllClaimsByPatRegIDAndVisitId(Integer patRegID,Integer visitID);

    SuccessMsg createClaim(ClaiminfomasterProfDto claimDto, String remoteAddr);

    ClaiminfomasterProfDto updateClaim(ClaiminfomasterProfDto claimDto, String remoteAddr);

    ClaiminfomasterProfDto_ViewSingleClaim getClaimById(Integer claimId);

    ClaiminfomasterInstDto_CopyClaim copyClaim(Integer claimID);




}
