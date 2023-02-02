package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessMsg {
    String statuscode;
    String message;
    Integer claimId;
    String claimNo;

}
