package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.ClaimStatus;

import java.util.List;

public interface ClaimStatusRepository extends JpaRepository<ClaimStatus,Long> {
    List<ClaimStatus> findClaimStatusByStatusEquals(String arg);
}
