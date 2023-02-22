package rovermd.project.claimservices.dto.ub04;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.dto.ClientDTO;
import rovermd.project.claimservices.dto.CompanyDTO;
import rovermd.project.claimservices.dto.InsuranceDTO;
import rovermd.project.claimservices.dto.PatientDto;
import rovermd.project.claimservices.dto.institutional.ClaimadditionalinfoDto;
import rovermd.project.claimservices.dto.institutional.ClaimchargesinfoDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UB04DTO {
    InsuranceDTO insuranceDTO;

    PatientDto patientDto;

    ClientDTO clientDTO;

    CompanyDTO companyDTO;

    List<ClaimchargesinfoDto> claimchargesinfoDto;

    ClaimadditionalinfoDto claimadditionalinfoDto;

}
