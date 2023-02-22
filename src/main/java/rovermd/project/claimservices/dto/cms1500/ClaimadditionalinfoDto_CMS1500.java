package rovermd.project.claimservices.dto.cms1500;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link rovermd.project.claimservices.entity.Claimadditionalinfo} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimadditionalinfoDto_CMS1500 implements Serializable {
    @Size(max = 255)
    private String employmentStatusAddInfo;
    @Size(max = 255)
    private String autoAccidentAddInfo;
    @Size(max = 255)
    private String otherAccidentAddInfo;
    @Size(max = 255)
    private String autoaccidentStateaddinfo;

}