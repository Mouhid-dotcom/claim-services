package rovermd.project.claimservices.dto.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodeoccspan;

import java.io.Serializable;

/**
 * A DTO for the {@link Claiminfocodeoccspan} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodeoccspanDto implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String code;
    @Size(max = 200)
    private String fromDate;
    @Size(max = 200)
    private String toDate;
    private Integer status;
}