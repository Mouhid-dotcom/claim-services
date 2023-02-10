package rovermd.project.claimservices.service;

import rovermd.project.claimservices.entity.Claiminfomaster;

import java.util.List;

public interface ClaimServiceSrubber {
    List<?> scrubberProf(Claiminfomaster claim);

    List<?> scrubberInst(Claiminfomaster claim);
}
