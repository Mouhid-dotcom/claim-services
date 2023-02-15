package rovermd.project.claimservices.service;

import rovermd.project.claimservices.entity.Claiminfomaster;

import java.io.IOException;
import java.text.ParseException;

public interface ClaimServiceEDI {

    Object createEDI_Prof(Integer claimId) throws ParseException, IOException;
    Object createEDI_Inst(Integer claimId) throws ParseException, IOException;
}
