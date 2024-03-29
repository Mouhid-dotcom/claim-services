package rovermd.project.claimservices.entity.paymentPosting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "claim_ledger_charges_entries")
@NoArgsConstructor
@Setter
@Getter
public class ClaimLedgerChargesEntries {
    @Id
    @Column(name = "Id" , nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="ClaimNumber")
    private String claimNumber;
    @Column(name="TransactionType")
    private String transactionType;
    @Column(name="ClaimIdx")
    private Integer claimIdx;
    @Column(name="ChargeIdx")
    private Integer chargeIdx;
    @Column(name="Charges")
    private String charges;
    @Column(name="Amount")
    private BigDecimal amount;
    @Column(name="StartBalance")
    private BigDecimal startBalance;
    @Column(name="Allowed")
    private BigDecimal allowed;
    @Column(name="Paid")
    private BigDecimal paid;
    @Column(name="Remarks")
    private String remarks;
    @Column(name="AdjReasons")
    private String adjReasons;
    @Column(name="Adjusted")
    private BigDecimal adjusted;
    @Column(name="SequestrationAmt")
    private BigDecimal sequestrationAmt;
    @Column(name="UnpaidReasons")
    private String unpaidReasons;
    @Column(name="Unpaid")
    private BigDecimal unpaid;
    @Column(name="Deductible")
    private BigDecimal deductible;
    @Column(name="Status")
    private Integer status;
    @Column(name="OtherCredits")
    private BigDecimal otherCredits;
    @Column(name="EndBalance")
    private BigDecimal endBalance;
    @Column(name="CreatedAt")
    private ZonedDateTime createdAt;
    @Column(name="CreatedBy")
    private String createdBy;
    @Column(name="UserIP")
    private String userIp;
    @Column(name="Payment")
    private String payment;
    @Column(name="Adjustment")
    private String adjustment;
    @Column(name="Balance")
    private String balance;
    @Column(name="TransactionIdx")
    private String transactionIdx;
    @Column(name="UpdatedAt")
    private ZonedDateTime updatedAt;
    @Column(name="UpdatedBy")
    private String updatedBy;
    @Column(name="Deleted")
    private Integer deleted;

    private String action;
    private String tcn;
    private String claimControlNo;

    @PrePersist
    public void created() {
        createdAt = ZonedDateTime.now();
        createdBy = "MOUHID_CREATED";
    }

    @PreUpdate
    public void Updated() {
        updatedAt = ZonedDateTime.now();
        updatedBy = "MOUHID_UPDATED";
    }

}