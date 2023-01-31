package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.copyClaim.professional.ClaiminfomasterProfDto_CopyClaim;
import rovermd.project.claimservices.dto.institutional.ClaiminfomasterInstDto;
import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;
import rovermd.project.claimservices.dto.viewSingleClaim.institutional.ClaiminfomasterInstDto_ViewSingleClaim;

import java.util.List;

public interface ClaimServiceInstitutional {
    List<ClaiminfomasterInstDto> getAllClaims();

    ClaiminfomasterInstDto createClaim(ClaiminfomasterInstDto claimDto, String remoteAddr);

    ClaiminfomasterInstDto updateClaim(ClaiminfomasterInstDto claimDto, String remoteAddr);

    ClaiminfomasterInstDto_ViewSingleClaim getClaimById(Integer claimId);

    ClaiminfomasterProfDto_CopyClaim copyClaim(Integer claimID);

}
