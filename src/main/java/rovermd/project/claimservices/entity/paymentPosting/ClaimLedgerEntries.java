package rovermd.project.claimservices.entity.paymentPosting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "Claim_Ledger_entries")
@NoArgsConstructor
@Setter
@Getter
public class ClaimLedgerEntries {

    @Id
    @Column(name = "Id" , nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="TransactionIdx")
    private String transactionIdx;

    @Column(name="ClaimIdx")
    private String claimIdx;

    @Column(name="TransactionType")
    private String transactionType;

    @Column(name="Amount")
    private String amount;

    @Column(name="ClaimNumber")
    private String claimNumber;

    @Column(name="CreatedAt")
    private ZonedDateTime createdAt;

    @Column(name="CreatedBy")
    private String createdBy;

    @Column(name="UserIP")
    private String userIp;

    @Column(name="UpdatedBy")
    private String updatedBy;

    @Column(name="UpdatedAt")
    private ZonedDateTime updatedAt;
}
