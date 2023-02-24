package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rovermd.project.claimservices.entity.claimMaster.ClaimStatus;

import java.io.Serializable;

/**
 * A DTO for the {@link ClaimStatus} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimStatusDto implements Serializable {
    private Long id;
    private String Descname;
}