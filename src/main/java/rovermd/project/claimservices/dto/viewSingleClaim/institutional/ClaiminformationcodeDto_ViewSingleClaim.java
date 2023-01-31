package rovermd.project.claimservices.dto.viewSingleClaim.institutional;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link rovermd.project.claimservices.entity.Claiminformationcode} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaiminformationcodeDto_ViewSingleClaim implements Serializable {
    private Integer id;
    @Size(max = 255)
    private String principalDiagInfoCodes;
    @Size(max = 255)
    private String pOAInfoCodes;
    @Size(max = 255)
    private String admittingDiagInfoCodes;
    @Size(max = 255)
    private String principalProcedureInfoCodes;
    @Size(max = 255)
    private String principalProcedureDateInfoCodes;

}