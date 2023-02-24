package rovermd.project.claimservices.service;

import rovermd.project.claimservices.entity.claimMaster.Claiminfomaster;

import java.util.List;

public interface ClaimServiceSrubber {
    List<?> scrubberProf(Claiminfomaster claim, int... changeDetected);

    List<?> scrubberInst(Claiminfomaster claim, int... changeDetected);
}
