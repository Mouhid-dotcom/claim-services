package rovermd.project.claimservices.dto.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodeconditioncode;

import java.io.Serializable;

/**
 * A DTO for the {@link Claiminfocodeconditioncode} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminfocodeconditioncodeDto implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String code;
    private Integer status;
}