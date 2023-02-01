package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rovermd.project.claimservices.entity.ClaimAudittrail;

import java.util.List;

@Repository
public interface ClaimAudittrailRepository extends JpaRepository<ClaimAudittrail, Integer> {
    List<ClaimAudittrail> findByClaimNo(String claimNumber);
    @Query("SELECT IFNULL(REPLACE(ruleHTML,'&amp;','&'),'') FROM ClaimAudittrail WHERE claimNo=:claimNumber AND action='REMOVED'")
    List<String> findByClaimNoAAndAction(@Param("claimNumber") String claimNumber);

}