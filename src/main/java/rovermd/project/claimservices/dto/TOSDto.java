package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link rovermd.project.claimservices.entities.TOS} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TOSDto implements Serializable {
    private String Code;
    private String Description;
}