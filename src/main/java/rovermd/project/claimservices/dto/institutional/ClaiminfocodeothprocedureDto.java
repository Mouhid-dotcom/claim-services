package rovermd.project.claimservices.dto.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.Claiminfocodeothprocedure;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link Claiminfocodeothprocedure} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodeothprocedureDto implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String code;
    @Size(max = 200)
    private String date;
    private Integer status;
}