package rovermd.project.claimservices.dto.cms1500;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PatientDto_CMS1500 {
    private String FirstName;
    private String LastName;
    private String Address;
    private String City;
    private String State;
    private String ZipCode;
    private String Dob;
    private String Gender;
    private String Ssn;
    private String Dos;
    private String PriInsurerFirstName;
    private String PriInsurerLastName;
    private String PatientRelationtoPrimary;
    private String PriMemberId;
    private String PriGroupNumber;
    private String PriAddress;
    private String PriCity;
    private String PriState;
    private String PriZipCode;
    private String PriDOB;
    private String PriGender;
    private String PriContact;
    private String secInsurerFirstName;
    private String secInsurerLastName;
    private String PatientRelationshiptoSecondry;
    private String SecMemberId;
    private String SecGroupNumber;
    private String SecAddress;
    private String SecCity;
    private String SecState;
    private String SecZipCode;
    private String SecDOB;
    private String SecGender;
    private String SecContact;
}
