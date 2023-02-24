package rovermd.project.claimservices.dto.copyClaim.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodeothdiag;

import java.io.Serializable;

/**
 * A DTO for the {@link Claiminfocodeothdiag} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodeothdiagDto_CopyClaim implements Serializable {

    @Size(max = 255)
    private String code;
    @Size(max = 255)
    private String pqa;
    private Integer status;
}