package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimSummaryDTO {
    Integer patientName;
    String formVersion;
    BigDecimal totalAmount;
    BigDecimal insurancePayment;
    BigDecimal patientPayment;
    BigDecimal adjustments;
    BigDecimal balance;
    String dos;
    String claimCreatedDate;
}