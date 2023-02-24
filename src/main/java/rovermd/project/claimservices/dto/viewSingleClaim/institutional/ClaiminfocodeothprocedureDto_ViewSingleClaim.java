package rovermd.project.claimservices.dto.viewSingleClaim.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodeothprocedure;

import java.io.Serializable;

/**
 * A DTO for the {@link Claiminfocodeothprocedure} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodeothprocedureDto_ViewSingleClaim implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String code;
    private String description;
    @Size(max = 200)
    private String date;
    private Integer status;
}