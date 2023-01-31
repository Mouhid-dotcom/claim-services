package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PatientDto {

    private String FirstName;

    private String LastName;

    private String Address;

    private String City;

    private String State;

    private String ZipCode;

    private ZonedDateTime DOB;

    private String Gender;

    private String ReasonVisit;

    private String SSN;

    private ZonedDateTime DOS;

    private String PriInsurerFirstName;

    private String PriInsurerLastName;

    private String PatientRelationtoPrimary;

    private String SecInsurerFirstName;

    private String SecInsurerLastName;

    private String PatientRelationshiptoSecondry;

}
