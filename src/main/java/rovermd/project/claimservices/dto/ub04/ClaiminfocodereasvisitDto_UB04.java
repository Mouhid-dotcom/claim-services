package rovermd.project.claimservices.dto.ub04;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link rovermd.project.claimservices.entity.Claiminfocodereasvisit} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodereasvisitDto_UB04 implements Serializable {
    @Size(max = 255)
    private String code;
}