package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimInfoMaster_List implements Serializable {
    Object claimNo;
    Object patientName;
    Object dateOfService;
    Object totalCharges;
    Object balance;
    Object status;
    Object type;
}
