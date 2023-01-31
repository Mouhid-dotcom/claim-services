package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link rovermd.project.claimservices.entities.POS} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class POSDto implements Serializable {
    private String Code;
    private String Description;
}