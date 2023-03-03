package rovermd.project.claimservices.entity.paymentPosting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "claim_ledger_charges_entries_history")
@NoArgsConstructor
@Setter
@Getter
public class ClaimLedgerChargesEntriesHistory {
    @Id
    @Column(name = "Id" , nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="ClaimNumber")
    private String claimNumber;
    @Column(name="TransactionType")
    private String transactionType;
    @Column(name="ClaimIdx")
    private String claimIdx;
    @Column(name="ChargeIdx")
    private String chargeIdx;
    @Column(name="Charges")
    private String charges;
    @Column(name="Amount")
    private String amount;
    @Column(name="StartBalance")
    private String startBalance;
    @Column(name="Allowed")
    private String allowed;
    @Column(name="Paid")
    private String paid;
    @Column(name="Remarks")
    private String remarks;
    @Column(name="AdjReasons")
    private String adjReasons;
    @Column(name="Adjusted")
    private String adjusted;
    @Column(name="SequestrationAmt")
    private String sequestrationAmt;
    @Column(name="UnpaidReasons")
    private String unpaidReasons;
    @Column(name="Unpaid")
    private String unpaid;
    @Column(name="Deductible")
    private String deductible;
    @Column(name="Status")
    private String status;
    @Column(name="OtherCredits")
    private String otherCredits;
    @Column(name="EndBalance")
    private String endBalance;
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
    private String deleted;

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