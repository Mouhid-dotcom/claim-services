package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.*;

public interface ExternalService {
    InsuranceDTO getInsuranceDetailsById(String insId);
    DoctorDTO getDoctorDetailsById(long insId);

    PatientDto getPatientDetailsById(PatientReqDto patientReqDto);
    ClientDTO getClientDetailsById(long id);
    CompanyDTO getCompanyDetailsById(long id);
}
