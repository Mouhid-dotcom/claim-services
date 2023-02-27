package rovermd.project.claimservices.entity.paymentPosting;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "EOB_Master")
@NoArgsConstructor
@Setter
@Getter
public class EobMaster {

    @Id
    @Column(name = "Id" , nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="PatientIdx")
    private Integer patientIdx;

    @Column(name="PaymentAmount")
    private Double paymentAmount;

    @Column(name="ReceivedDate")
    private LocalDate receivedDate;

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
    private ZonedDateTime copayDos;

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
}
