package rovermd.project.claimservices.dto.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodereasvisit;

import java.io.Serializable;

/**
 * A DTO for the {@link Claiminfocodereasvisit} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodereasvisitDto implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String code;
    private Integer status;
}