package rovermd.project.claimservices.dto.ub04;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodeextcauseinj;

import java.io.Serializable;

/**
 * A DTO for the {@link Claiminfocodeextcauseinj} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodeextcauseinjDto_UB04 implements Serializable {
    @Size(max = 255)
    private String code;
}