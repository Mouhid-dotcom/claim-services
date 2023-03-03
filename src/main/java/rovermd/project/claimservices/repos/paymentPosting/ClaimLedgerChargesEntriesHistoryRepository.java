package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntriesHistory;

public interface ClaimLedgerChargesEntriesHistoryRepository extends JpaRepository<ClaimLedgerChargesEntriesHistory, Integer> {
}