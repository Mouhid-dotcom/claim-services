package rovermd.project.claimservices.dto.ub04;

import jakarta.validation.constraints.Size;
import lombok.Data;
import rovermd.project.claimservices.dto.DoctorDTO;
import rovermd.project.claimservices.dto.institutional.ClaimadditionalinfoDto;
import rovermd.project.claimservices.entity.claimMaster.Claiminfomaster;

import java.util.List;

/**
 * A DTO for the {@link Claiminfomaster} entity
 */
@Data
public class ClaiminfomasterInstDto_UB04 {
    @Size(max = 255)
    private String claimNumber;
    @Size(max = 255)
    private String refNumber;
    @Size(max = 255)
    private String typeBillText;

    private DoctorDTO operatingProvider;

    private DoctorDTO attendingProvider;

    private List<ClaimchargesinfoDto_UB04> claimchargesinfo;
    private ClaimadditionalinfoDto claimadditionalinfo;
    private ClaiminformationcodeDto_UB04 claiminformationcode;
    private List<ClaiminfocodeextcauseinjDto_UB04> claiminfocodeextcauseinj;
    private List<ClaiminfocodereasvisitDto_UB04> claiminfocodereasvisit;
    private List<ClaiminfocodeothdiagDto_UB04> claiminfocodeothdiag;
    private List<ClaiminfocodeothprocedureDto_UB04> claiminfocodeothprocedure;
    private List<ClaiminfocodeoccspanDto_UBO4> claiminfocodeoccspan;
    private List<ClaiminfooccuranceDto_UB04> claiminfooccurance;
    private List<ClaiminfocodevaluecodeDto_UB04> claiminfocodevaluecode;
    private List<ClaiminfocodeconditioncodeDto_UB04> claiminfocodeconditioncode;

}