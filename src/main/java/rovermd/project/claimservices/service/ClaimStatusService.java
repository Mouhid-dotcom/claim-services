package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.ClaimStatusDto;

import java.util.List;

public interface ClaimStatusService {
    List<ClaimStatusDto> getClaimStatus();
}
