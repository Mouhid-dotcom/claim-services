package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.*;
import rovermd.project.claimservices.dto.cms1500.ClientDTO_CMS1500;
import rovermd.project.claimservices.dto.cms1500.InsuranceDTO_CMS1500;
import rovermd.project.claimservices.dto.cms1500.PatientDto_CMS1500;
import rovermd.project.claimservices.dto.ub04.ClientDTO_UB04;

public interface ExternalService {
    InsuranceDTO getInsuranceDetailsById(String insId);
    InsuranceDTO_CMS1500 getInsuranceDetailsById_CMS1500(String insId);
    DoctorDTO getDoctorDetailsById(long insId);
    PatientDto getPatientDetailsById(PatientReqDto patientReqDto);
    PatientDto_CMS1500 getPatientDetailsById_CMS1500(PatientReqDto patientReqDto);
    ClientDTO getClientDetailsById(long id);
    ClientDTO_CMS1500 getClientDetailsById_CMS1500(long id);
    ClientDTO_UB04 getClientDetailsById_UB04(long id);
    CompanyDTO getCompanyDetailsById(long id);
}
