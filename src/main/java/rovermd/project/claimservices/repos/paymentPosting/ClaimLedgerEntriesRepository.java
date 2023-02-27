package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerEntries;

public interface ClaimLedgerEntriesRepository extends JpaRepository<ClaimLedgerEntries, Integer> {
}