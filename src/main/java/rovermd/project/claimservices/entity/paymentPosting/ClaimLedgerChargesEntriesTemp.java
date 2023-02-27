package rovermd.project.claimservices.entity.paymentPosting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "claim_ledger_charges_entries_temp")
@NoArgsConstructor
@Setter
@Getter
public class ClaimLedgerChargesEntriesTemp {
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
    private String startBalance;
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
    @Column(name="UnpaidReasons")
    private String unpaidReasons;
    @Column(name="Unpaid")
    private BigDecimal unpaid;
    @Column(name="Deductible")
    private BigDecimal deductible;
    @Column(name="Status")
    private String status;
    @Column(name="OtherCredits")
    private BigDecimal otherCredits;
    @Column(name="EndBalance")
    private BigDecimal endBalance;
    @Column(name="CreatedAt")
    private OffsetDateTime createdAt;
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
    @Column(name="SequestrationAmt")
    private String sequestrationAmt;
    @Column(name="UpdatedAt")
    private OffsetDateTime updatedAt;
    @Column(name="UpdatedBy")
    private String updatedBy;
    @Column(name="Deleted")
    private String deleted;
    @Column(name="PaymentAmount")
    private String paymentAmount;
    @Column(name="ReceivedDate")
    private LocalDate receivedDate;
    @Column(name="InsuranceIdx")
    private String insuranceIdx;
    @Column(name="AppliedAmount")
    private String appliedAmount;
    @Column(name="Claim_ERA_Id")
    private Integer claimEraId;
    @Column(name="Claim_ERA_Checks_Id")
    private Integer claimEraChecksId;
}
