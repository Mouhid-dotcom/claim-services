package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntriesTemp;

public interface ClaimLedgerChargesEntriesTempRepository extends JpaRepository<ClaimLedgerChargesEntriesTemp, Integer> {
}