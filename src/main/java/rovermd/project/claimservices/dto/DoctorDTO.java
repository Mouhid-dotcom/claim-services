package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorDTO implements Serializable {
    private Long id;
    private String doctorsFirstName;
    private String doctorsLastName;
    private String status;
    private String npi;
    private String taxonomySpecialty;
    private String ssn;
    private String etin;
    private String phoneNumber;
    private Date createdOn;
    private Date updatedOn;
    private String createdBy;
    private String updatedBy;
    private Boolean isDeleted;
}
