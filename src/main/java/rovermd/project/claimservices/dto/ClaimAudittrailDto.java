package rovermd.project.claimservices.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.ClaimAudittrail;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link ClaimAudittrail} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimAudittrailDto implements Serializable {
    @Size(max = 255)
    private String claimNo;
    @Size(max = 255)
    private String userID;
    private Instant createdAt;
    @Size(max = 255)
    private String action;
    private String ruleText;
}