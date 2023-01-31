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
@Table(name = "clia")
public class Clia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "Status")
    private Integer status;

    @Size(max = 255)
    @Column(name = "Year")
    private String year;

    @Size(max = 255)
    @Column(name = "HCPCS")
    private String hcpcs;

    @Size(max = 255)
    @Column(name = "`MOD`")
    private String mod;

    @Size(max = 255)
    @Column(name = "EffectiveDate")
    private String effectiveDate;

}