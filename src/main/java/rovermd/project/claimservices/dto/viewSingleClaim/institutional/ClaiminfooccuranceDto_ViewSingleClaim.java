package rovermd.project.claimservices.dto.viewSingleClaim.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claiminfooccurance;

import java.io.Serializable;

/**
 * A DTO for the {@link Claiminfooccurance} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfooccuranceDto_ViewSingleClaim implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String code;
    private String description;
    @Size(max = 200)
    private String date;
    private Integer status;
}