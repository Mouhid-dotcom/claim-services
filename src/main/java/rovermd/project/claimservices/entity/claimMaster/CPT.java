package rovermd.project.claimservices.entity.claimMaster;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cptmaster")
@NoArgsConstructor
@Setter
@Getter

//Current Procedural Terminology
public class CPT {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "CPTCode")
    private String CPTCode;

    @Column(name = "CPTDescription")
    private String CPTDescription;

    @Column(name = "Price")
    private BigDecimal Price;

    @Column(name = "EffectiveDate")
    private LocalDateTime EffectiveDate;

}
