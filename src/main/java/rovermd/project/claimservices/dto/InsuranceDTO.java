package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceDTO {
    private long id;

    private String payerName;

    private String payerID;

    private String status;

    private String address;

    private String city;

    private String State;

    private String zip;
    private String claimIndicator_P;
    private String claimIndicator_I;
}
