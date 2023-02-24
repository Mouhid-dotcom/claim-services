package rovermd.project.claimservices.dto.cms1500;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.Claimadditionalinfo;

import java.io.Serializable;

/**
 * A DTO for the {@link Claimadditionalinfo} entity
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