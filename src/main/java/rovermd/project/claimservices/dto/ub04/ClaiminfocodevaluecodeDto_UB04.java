package rovermd.project.claimservices.dto.ub04;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodevaluecode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link Claiminfocodevaluecode} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodevaluecodeDto_UB04 implements Serializable {
    @Size(max = 255)
    private String code;
    private BigDecimal amount;
}