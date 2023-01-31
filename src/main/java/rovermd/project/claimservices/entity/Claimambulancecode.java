package rovermd.project.claimservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "claimambulancecodes")
@NoArgsConstructor
@Getter
@Setter
public class Claimambulancecode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

//    @Column(name = "ClaimInfoMasterId")
//    private Integer claimInfoMasterId;

    @Size(max = 255)
    @Column(name = "ClaimNumber")
    private String claimNumber;

//    @JsonBackReference
//    @JsonIgnore
////    @OneToOne(cascade = CascadeType.ALL)
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="ClaimInfoMasterId", referencedColumnName="Id",updatable = false)
//    Claiminfomaster claiminfomaster;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id")
    Claiminfomaster claiminfomaster;

    @Size(max = 255)
    @Column(name = "AmbClaimInfoCodes")
    private String ambClaimInfoCodes;

    @Size(max = 255)
    @Column(name = "TranReasonInfoCodes")
    private String tranReasonInfoCodes;

    @Size(max = 255)
    @Column(name = "TranMilesInfoCodes")
    private String tranMilesInfoCodes;

    @Size(max = 255)
    @Column(name = "PatWeightInfoCodes")
    private String patWeightInfoCodes;

    @Lob
    @Column(name = "RoundTripReasInfoCodes")
    private String roundTripReasInfoCodes;

    @Lob
    @Column(name = "StretReasonInfoCodes")
    private String stretReasonInfoCodes;

    @Size(max = 255)
    @Column(name = "PickUpAddressInfoCode")
    private String pickUpAddressInfoCode;

    @Size(max = 255)
    @Column(name = "PickUpCityInfoCode")
    private String pickUpCityInfoCode;

    @Size(max = 255)
    @Column(name = "PickUpStateInfoCode")
    private String pickUpStateInfoCode;

    @Size(max = 255)
    @Column(name = "PickUpZipCodeInfoCode")
    private String pickUpZipCodeInfoCode;

    @Size(max = 255)
    @Column(name = "DropoffAddressInfoCode")
    private String dropoffAddressInfoCode;

    @Size(max = 255)
    @Column(name = "DropoffCityInfoCode")
    private String dropoffCityInfoCode;

    @Size(max = 255)
    @Column(name = "DropoffStateInfoCode")
    private String dropoffStateInfoCode;

    @Size(max = 255)
    @Column(name = "DropoffZipCodeInfoCode")
    private String dropoffZipCodeInfoCode;

    @Size(max = 255)
    @Column(name = "PatAdmitHosChk")
    private String patAdmitHosChk;

    @Size(max = 255)
    @Column(name = "PatMoveStretChk")
    private String patMoveStretChk;

    @Size(max = 255)
    @Column(name = "PatUnconShockChk")
    private String patUnconShockChk;

    @Size(max = 255)
    @Column(name = "PatTransEmerSituaChk")
    private String patTransEmerSituaChk;

    @Size(max = 255)
    @Column(name = "PatPhyRestrainChk")
    private String patPhyRestrainChk;

    @Size(max = 255)
    @Column(name = "PatvisiblehemorrChk")
    private String patvisiblehemorrChk;

    @Size(max = 255)
    @Column(name = "AmbSerNeccChk")
    private String ambSerNeccChk;

    @Size(max = 255)
    @Column(name = "PatconfbedchairChk")
    private String patconfbedchairChk;

    @Size(max = 255)
    @Column(name = "Status")
    private String status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Size(max = 255)
    @Column(name = "CreatedBy")
    private String createdBy;

    @Size(max = 255)
    @Column(name = "CreatedIP")
    private String createdIP;

    @Size(max = 255)
    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "UpdatedAt")
    private Instant updatedAt;

    @PrePersist
    public void added() {
        createdDate = new Date().toInstant();
        createdBy = "MOUHID_CREATED";

    }

    @PreUpdate
    public void Updated() {
        updatedAt = new Date().toInstant();
        updatedBy = "MOUHID_UPDATED";

    }

}