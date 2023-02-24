package rovermd.project.claimservices.dto.institutional;

import jakarta.validation.constraints.Size;
import lombok.Data;
import rovermd.project.claimservices.entity.Claiminfomaster;

import java.util.List;

/**
 * A DTO for the {@link Claiminfomaster} entity
 */
@Data
public class ClaiminfomasterInstDto {
    private Integer id;
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



    private List<ClaimchargesinfoDto> claimchargesinfo;
    private ClaimadditionalinfoDto claimadditionalinfo;
    private ClaiminformationcodeDto claiminformationcode;
    private List<ClaiminfocodeextcauseinjDto> claiminfocodeextcauseinj;
    private List<ClaiminfocodereasvisitDto> claiminfocodereasvisit;
    private List<ClaiminfocodeothdiagDto> claiminfocodeothdiag;
    private List<ClaiminfocodeothprocedureDto> claiminfocodeothprocedure;
    private List<ClaiminfocodeoccspanDto> claiminfocodeoccspan;
    private List<ClaiminfooccuranceDto> claiminfooccurance;
    private List<ClaiminfocodevaluecodeDto> claiminfocodevaluecode;
    private List<ClaiminfocodeconditioncodeDto> claiminfocodeconditioncode;

}