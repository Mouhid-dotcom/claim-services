package rovermd.project.claimservices.dto.ub04;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.dto.CompanyDTO;
import rovermd.project.claimservices.dto.InsuranceDTO;
import rovermd.project.claimservices.dto.PatientDto;

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
