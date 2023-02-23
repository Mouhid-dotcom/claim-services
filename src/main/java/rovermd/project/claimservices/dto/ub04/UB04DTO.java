package rovermd.project.claimservices.dto.ub04;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.dto.ClientDTO;
import rovermd.project.claimservices.dto.CompanyDTO;
import rovermd.project.claimservices.dto.InsuranceDTO;
import rovermd.project.claimservices.dto.PatientDto;
import rovermd.project.claimservices.dto.institutional.ClaimadditionalinfoDto;
import rovermd.project.claimservices.dto.institutional.ClaiminfocodeconditioncodeDto;
import rovermd.project.claimservices.dto.institutional.ClaiminfooccuranceDto;
import rovermd.project.claimservices.dto.institutional.ClaiminformationcodeDto;
import rovermd.project.claimservices.entity.Claiminfocodeoccspan;
import rovermd.project.claimservices.entity.Claiminfooccurance;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UB04DTO {
    InsuranceDTO insuranceDTO;
    PatientDto patientDto;
    ClientDTO_UB04 clientDTO;
    CompanyDTO companyDTO;
    ClaiminfomasterInstDto_UB04 claiminfomasterInstDto;
}
