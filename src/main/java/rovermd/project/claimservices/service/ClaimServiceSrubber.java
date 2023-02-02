package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;

import java.text.ParseException;
import java.util.List;

public interface ClaimServiceSrubber {
    List<String> scrubber(ClaiminfomasterProfDto claimDto);
}
