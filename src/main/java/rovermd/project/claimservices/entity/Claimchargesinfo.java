package rovermd.project.claimservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import rovermd.project.claimservices.service.ClaimAudittrailService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "claimchargesinfo")
@NoArgsConstructor
@Setter
@Getter
public class Claimchargesinfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

//    @Column(name = "ClaimInfoMasterId")
//    private Integer claimInfoMasterId;

//    @Size(max = 255)
//    @Column(name = "ClaimNumber")
//    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="ClaimInfoMasterId", referencedColumnName="Id"),
            @JoinColumn(name="ClaimNumber", referencedColumnName="ClaimNumber")
    })
    Claiminfomaster claiminfomaster;

    @Size(max = 255)
    @Column(name = "DescriptionFrom")
    private String descriptionFrom;

    @Size(max = 255)
    @Column(name = "ServiceDate")
    private String serviceDate;

    @Size(max = 255)
    @Column(name = "HCPCS")
    private String hcpcs;

    @Size(max = 255)
    @Column(name = "Mod1")
    private String mod1;

    @Size(max = 255)
    @Column(name = "Mod2")
    private String mod2;

    @Size(max = 255)
    @Column(name = "Mod3")
    private String mod3;

    @Size(max = 255)
    @Column(name = "Mod4")
    private String mod4;

    @Size(max = 255)
    @Column(name = "RevCode")
    private String revCode;

    @Column(name = "UnitPrice", precision = 20, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "Units", precision = 20, scale = 2)
    private BigDecimal units;

    @Column(name = "Amount", precision = 20, scale = 2)
    private BigDecimal amount;

    @Size(max = 255)
    @Column(name = "ChargesStatus")
    private String chargesStatus;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Size(max = 255)
    @Column(name = "CreatedBy")
    private String createdBy;

    @Size(max = 255)
    @Column(name = "CreatedIP")
    private String createdIP;

    @Size(max = 255)
    @Column(name = "ICDA")
    private String icda;

    @Size(max = 255)
    @Column(name = "ICDB")
    private String icdb;

    @Size(max = 255)
    @Column(name = "ICDC")
    private String icdc;

    @Size(max = 255)
    @Column(name = "ICDD")
    private String icdd;

    @Size(max = 255)
    @Column(name = "ICDE")
    private String icde;

    @Size(max = 255)
    @Column(name = "ICDF")
    private String icdf;

    @Size(max = 255)
    @Column(name = "ICDG")
    private String icdg;

    @Size(max = 255)
    @Column(name = "ICDH")
    private String icdh;

    @Size(max = 255)
    @Column(name = "ICDI")
    private String icdi;

    @Size(max = 255)
    @Column(name = "ICDJ")
    private String icdj;

    @Size(max = 255)
    @Column(name = "ICDK")
    private String icdk;

    @Size(max = 255)
    @Column(name = "ICDL")
    private String icdl;

    @Size(max = 255)
    @Column(name = "ServiceFromDate")
    private String serviceFromDate;

    @Size(max = 255)
    @Column(name = "ServiceToDate")
    private String serviceToDate;

    @Size(max = 255)
    @Column(name = "HCPCSProcedure")
    private String hCPCSProcedure;

    @Size(max = 255)
    @Column(name = "POS")
    private String pos;

    @Size(max = 255)
    @Column(name = "TOS")
    private String tos;

    @Size(max = 255)
    @Column(name = "DXPointer")
    private String dXPointer;

    @Size(max = 255)
    @Column(name = "ChargeOption")
    private String chargeOption;

    @Size(max = 255)
    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "UpdatedAt")
    private Instant updatedAt;

    @OneToOne(mappedBy = "claimchargesinfo",cascade = CascadeType.ALL)
    Claimchargesotherinfo claimchargesotherinfo;


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