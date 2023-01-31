package rovermd.project.claimservices.entity.scrubber;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "genderspecificcpt_icd")
public class GenderspecificcptIcd {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "CPTCode")
    private String cPTCode;

    @Size(max = 255)
    @Column(name = "Gender")
    private String gender;

    @Size(max = 255)
    @Column(name = "Effective_Date")
    private String effectiveDate;

    @Size(max = 255)
    @Column(name = "Termination_Date")
    private String terminationDate;

    @Size(max = 100)
    @Column(name = "ICD", length = 100)
    private String icd;

}