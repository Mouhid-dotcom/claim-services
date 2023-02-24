package rovermd.project.claimservices.dto.institutional;

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
public class ClaiminfocodevaluecodeDto implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String code;
    private BigDecimal amount;
    private Integer status;
}