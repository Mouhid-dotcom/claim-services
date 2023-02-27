package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntries;

public interface ClaimLedgerChargesEntriesRepository extends JpaRepository<ClaimLedgerChargesEntries, Integer> {
}