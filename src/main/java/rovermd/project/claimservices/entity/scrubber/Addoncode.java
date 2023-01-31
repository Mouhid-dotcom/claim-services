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
@Table(name = "addoncodes")
public class Addoncode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "PrimaryCodes", length = 50)
    private String primaryCodes;

    @Size(max = 50)
    @Column(name = "AddOnCode", length = 50)
    private String addOnCode;

    @Column(name = "EffectiveDate")
    private Instant effectiveDate;

    @Column(name = "TerminationDate")
    private Instant terminationDate;

    @Size(max = 50)
    @Column(name = "PayerID", length = 50)
    private String payerID;

    @Column(name = "isDeleted")
    private Integer isDeleted;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Size(max = 100)
    @Column(name = "CreatedBy", length = 100)
    private String createdBy;

}