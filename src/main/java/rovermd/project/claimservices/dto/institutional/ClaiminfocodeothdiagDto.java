package rovermd.project.claimservices.dto.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link rovermd.project.claimservices.entity.Claiminfocodeothdiag} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodeothdiagDto implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String code;
    @Size(max = 255)
    private String pqa;
    private Integer status;
}