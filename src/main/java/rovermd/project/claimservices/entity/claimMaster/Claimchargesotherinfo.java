package rovermd.project.claimservices.entity.claimMaster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "claimchargesotherinfo")
@NoArgsConstructor
@Getter
@Setter
public class Claimchargesotherinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "ClaimIdx")
    private String claimIdx;

    @Column(name = "ClaimType")
    private String ClaimType;

//    @JsonIgnore
//    @OneToOne
//    @JoinColumns({
//            @JoinColumn(name="ClaimIdx", referencedColumnName="Id"),
//            @JoinColumn(name="ClaimType", referencedColumnName="ClaimType")
//    })
//    Claiminfomaster claiminfomaster;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "ChargeIdx" , referencedColumnName = "Id")
    Claimchargesinfo claimchargesinfo;

//    @Size(max = 255)
//    @Column(name = "ClaimType")
//    private String claimType;

    @Size(max = 255)
    @Column(name = "NonCoveredAmount")
    private String nonCoveredAmount;

    @Size(max = 255)
    @Column(name = "DrugCode")
    private String drugCode;

    @Size(max = 255)
    @Column(name = "DrugUnit")
    private String drugUnit;

    @Size(max = 255)
    @Column(name = "Unit")
    private String unit;

    @Size(max = 255)
    @Column(name = "DrugDays")
    private String drugDays;

    @Size(max = 255)
    @Column(name = "DrugCodeFormat")
    private String drugCodeFormat;

    @Size(max = 255)
    @Column(name = "DrugPrice")
    private String drugPrice;

    @Size(max = 255)
    @Column(name = "PrescriptionNum")
    private String prescriptionNum;

    @Size(max = 255)
    @Column(name = "PrescriptionDate")
    private String prescriptionDate;

    @Size(max = 255)
    @Column(name = "PrescriptionMonths")
    private String prescriptionMonths;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Size(max = 255)
    @Column(name = "CreatedBy")
    private String createdBy;

    @Size(max = 255)
    @Column(name = "CreatedIP")
    private String createdIP;

    @Column(name = "UpdatedAt")
    private Instant updatedAt;

    @Size(max = 255)
    @Column(name = "UpdatedBy")
    private String updatedBy;

    @PrePersist
    public void added() {
        createdDate = new Date().toInstant();
        createdBy = "MOUHID_CREATED";




        System.out.println("Attempting to add new charge with Claimchargesinfo: " + id);
    }

    @PreUpdate
    public void Updated() {
        updatedAt = new Date().toInstant();
        updatedBy = "MOUHID_UPDATED";

        System.out.println("Attempting to Update new charge with Claimchargesinfo: " + id);
    }

}