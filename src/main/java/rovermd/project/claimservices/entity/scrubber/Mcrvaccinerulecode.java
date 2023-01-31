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
@Table(name = "mcrvaccinerulecodes")
public class Mcrvaccinerulecode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "VaccineCodes")
    private Integer vaccineCodes;

    @Size(max = 10)
    @Column(name = "ICD10Code", length = 10)
    private String iCD10Code;

    @Size(max = 50)
    @Column(name = "Administration", length = 50)
    private String administration;

    @Column(name = "VaccineType")
    private Integer vaccineType;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

}