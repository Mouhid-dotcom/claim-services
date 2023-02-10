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
@Table(name = "ncdxcptxicd")
public class Ncdxcptxicd {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "GrpN")
    private String grpN;

    @Size(max = 255)
    @Column(name = "ICD")
    private String icd;

    @Size(max = 255)
    @Column(name = "ResCode")
    private Integer resCode;

}