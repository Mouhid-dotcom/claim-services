package rovermd.project.claimservices.dto.cms1500;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.dto.ClientDTO;
import rovermd.project.claimservices.dto.CompanyDTO;
import rovermd.project.claimservices.dto.DoctorDTO;
import rovermd.project.claimservices.dto.PatientDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMS1500DTO {
    InsuranceDTO_CMS1500 insuranceDetails;

    PatientDto_CMS1500 patientDetails;

    ClientDTO_CMS1500 clientDetails;
    DoctorDTO doctorDetail;

    CompanyDTO companyDetails;

    List<ClaimchargesinfoDto_CMS1500> claimChargesDetails;

    ClaimadditionalinfoDto_CMS1500 claimAdditionalinfoDetails;
}
