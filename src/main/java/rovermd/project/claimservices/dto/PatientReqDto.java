package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientReqDto {

    private Long patientRegId;

    private Long visitId;

    private Long primaryInsuranceId;

    private Long secondaryInsuranceId;
}
