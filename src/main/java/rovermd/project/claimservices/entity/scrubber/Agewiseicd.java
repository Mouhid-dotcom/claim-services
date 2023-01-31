package rovermd.project.claimservices.entity.scrubber;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "agewiseicd")
public class Agewiseicd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "ICD", length = 50)
    private String icd;

    @Size(max = 255)
    @Column(name = "Description")
    private String description;

    @Column(name = "LowerLimit")
    private Integer lowerLimit;

    @Column(name = "UpperLimit")
    private Integer upperLimit;

    @Size(max = 50)
    @Column(name = "Unit", length = 50)
    private String unit;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

}