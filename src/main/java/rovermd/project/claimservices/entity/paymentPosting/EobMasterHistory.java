package rovermd.project.claimservices.entity.paymentPosting;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "EOB_Master_History")
@NoArgsConstructor
@Setter
@Getter
public class EobMasterHistory {

    @Id
    @Column(name = "Id" , nullable = false, updatable = false)
    private Integer id;

    @Column(name="PatientIdx")
    private Integer patientIdx;

    @Column(name="PaymentAmount")
    private String paymentAmount;

    @Column(name="ReceivedDate")
    private String receivedDate;

    @Column(name="CheckNumber")
    private String checkNumber;

    @Column(name="PaymentType")
    private String paymentType;

    @Column(name="PaymentSource")
    private String paymentSource;

    @Column(name="Memo")
    private String memo;

    @Column(name="Status")
    private Integer status;

    @Column(name="CopayDOS")
    private String copayDos;

    @Column(name="CardType")
    private String cardType;

    @Column(name="CreatedDate")
    private ZonedDateTime createdDate;

    @Column(name="CreatedBy")
    private String createdBy;

    @Column(name="ModifyBy")
    private String modifyBy;

    @Column(name="ModifyDate")
    private ZonedDateTime modifyDate;

    @Column(name="InsuranceIdx")
    private Integer insuranceIdx;

    @Column(name="OtherRefrenceNo")
    private String otherRefrenceNo;

    @Column(name="PaymentFrom")
    private String paymentFrom;

    @Column(name="isPaymentOnly")
    private Integer isPaymentOnly;

    @Column(name="isCreditAccount")
    private Integer isCreditAccount;

    @Column(name="AppliedAmount")
    private String appliedAmount;

    @Column(name="UnappliedAmount")
    private String unappliedAmount;

    @Column(name="UpdatedAt")
    private ZonedDateTime updatedAt;

    @Column(name="UpdatedBy")
    private String updatedBy;

    @Column(name="ViewDate")
    private ZonedDateTime viewDate;

    @Column(name="Claim_ERA_Id")
    private Integer claimEraId;

    @Column(name="Claim_ERA_Checks_Id")
    private Integer claimEraChecksId;

    @PrePersist
    public void created() {
        createdDate = ZonedDateTime.now();
        createdBy = "MOUHID_CREATED";
    }

    @PreUpdate
    public void Updated() {
        updatedAt = ZonedDateTime.now();
        viewDate = ZonedDateTime.now();
        updatedBy = "MOUHID_UPDATED";
    }
}
