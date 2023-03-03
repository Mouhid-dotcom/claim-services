package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.paymentPosting.EobMasterHistory;

public interface EobMasterHistoryRepository extends JpaRepository<EobMasterHistory, Integer> {
}