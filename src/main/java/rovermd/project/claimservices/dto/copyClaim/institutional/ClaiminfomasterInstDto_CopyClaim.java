package rovermd.project.claimservices.dto.copyClaim.institutional;

import jakarta.validation.constraints.Size;
import lombok.Data;
import rovermd.project.claimservices.entity.Claiminfomaster;

import java.util.List;

/**
 * A DTO for the {@link Claiminfomaster} entity
 */
@Data
public class ClaiminfomasterInstDto_CopyClaim {

    private Integer patientRegId;
    private Integer visitId;
    private Integer clientId;

    @Size(max = 255)
    private String claimNumber;
    @Size(max = 255)
    private String refNumber;
    @Size(max = 255)
    private String typeBillText;
    @Size(max = 255)
    private String patientName;
    @Size(max = 255)
    private String patientMRN;
    @Size(max = 255)
    private String acctNo;
    @Size(max = 255)
    private String phNumber;
    @Size(max = 255)
    private String email;
    private String address;
    @Size(max = 255)
    private String dos;
    @Size(max = 255)
    private String uploadDate;

    @Size(max = 255)
    private String attendingProvider;
    @Size(max = 255)
    private String billingProviders;
    @Size(max = 255)
    private String operatingProvider;
    @Size(max = 255)
    private String renderingProvider;
    @Size(max = 255)
    private String referringProvider;

    @Size(max = 255)
    private String origClaim;

    @Size(max = 255)
    private String clientName;


    private String priInsuranceNameId;
    @Size(max = 255)
    private String memId;
    @Size(max = 255)
    private String policyType;
    @Size(max = 255)
    private String grpNumber;
    @Size(max = 255)
    private String PatientRelationtoPrimary;

    private String secondaryInsuranceId;
    @Size(max = 255)
    private String secondaryInsuranceMemId;
    @Size(max = 255)
    private String secondaryInsuranceGrpNumber;
    @Size(max = 255)
    private String PatientRelationshiptoSecondary;



    private List<ClaimchargesinfoDto_CopyClaim> claimchargesinfo;
    private ClaimadditionalinfoDto_CopyClaim claimadditionalinfo;
    private ClaiminformationcodeDto_CopyClaim claiminformationcode;
    private List<ClaiminfocodeextcauseinjDto_CopyClaim> claiminfocodeextcauseinj;
    private List<ClaiminfocodereasvisitDto_CopyClaim> claiminfocodereasvisit;
    private List<ClaiminfocodeothdiagDto_CopyClaim> claiminfocodeothdiag;
    private List<ClaiminfocodeothprocedureDto_CopyClaim> claiminfocodeothprocedure;
    private List<ClaiminfocodeoccspanDto_CopyClaim> claiminfocodeoccspan;
    private List<ClaiminfooccuranceDto_CopyClaim> claiminfooccurance;
    private List<ClaiminfocodevaluecodeDto_CopyClaim> claiminfocodevaluecode;
    private List<ClaiminfocodeconditioncodeDto_CopyClaim> claiminfocodeconditioncode;

}