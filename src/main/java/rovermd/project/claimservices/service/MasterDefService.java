package rovermd.project.claimservices.service;

import rovermd.project.claimservices.dto.DoctorDTO;
import rovermd.project.claimservices.dto.InsuranceDTO;
import rovermd.project.claimservices.dto.PatientDto;
import rovermd.project.claimservices.dto.PatientReqDto;

public interface MasterDefService {
    InsuranceDTO getInsuranceDetailsById(String insId);
    DoctorDTO getDoctorDetailsById(long insId);

    PatientDto getPatientDetailsById(PatientReqDto patientReqDto);
}
