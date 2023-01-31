package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rovermd.project.claimservices.entity.ClaimAudittrail;

import java.util.List;

@Repository
public interface ClaimAudittrailRepository extends JpaRepository<ClaimAudittrail, Integer> {
    List<ClaimAudittrail> findByClaimNo(String claimNumber);
}