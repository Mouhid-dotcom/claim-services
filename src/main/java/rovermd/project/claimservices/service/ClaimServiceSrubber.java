package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.ScrubberRulesDto;
import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;
import rovermd.project.claimservices.entity.Claiminfomaster;

import java.text.ParseException;
import java.util.List;

public interface ClaimServiceSrubber {
    List<ScrubberRulesDto> scrubber(Claiminfomaster claim);
}
