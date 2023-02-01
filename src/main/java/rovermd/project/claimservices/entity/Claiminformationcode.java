package rovermd.project.claimservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "claiminformationcode")
@NoArgsConstructor
@Getter
@Setter
public class Claiminformationcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id")
    Claiminfomaster claiminfomaster;

    @Size(max = 255)
    @Column(name = "ClaimNumber")
    private String claimNumber;

    @Size(max = 255)
    @Column(name = "PrincipalDiagInfoCodes")
    private String principalDiagInfoCodes;

    @Size(max = 255)
    @Column(name = "POAInfoCodes")
    private String pOAInfoCodes;

    @Size(max = 255)
    @Column(name = "AdmittingDiagInfoCodes")
    private String admittingDiagInfoCodes;

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
    @Column(name = "PrincipalProcedureInfoCodes")
    private String principalProcedureInfoCodes;

    @Size(max = 255)
    @Column(name = "PrincipalProcedureDateInfoCodes")
    private String principalProcedureDateInfoCodes;

    @Column(name = "UpdateAt")
    private Instant updateAt;

    @Size(max = 255)
    @Column(name = "UpdatedBy")
    private String updatedBy;

    @PrePersist
    public void added() {
        createdDate = new Date().toInstant();
        createdBy = "MOUHID_CREATED";
    }
}