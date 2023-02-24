package rovermd.project.claimservices.dto.copyClaim.institutional;

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
public class ClaiminfooccuranceDto_CopyClaim implements Serializable {

    @Size(max = 255)
    private String code;
    @Size(max = 200)
    private String date;
    private Integer status;
}