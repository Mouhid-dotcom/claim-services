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
//    @Query("SELECT IFNULL(REPLACE(a.ruleHTML,'&amp;','&'),'') FROM ClaimAudittrail a WHERE a.claimNo=:claimNumber AND a.action='REMOVED'")
    @Query("SELECT a.ruleHTML FROM ClaimAudittrail a WHERE a.claimNo=:claimNumber AND a.action=:action")
    List<String> findByClaimNoAndAction_NQ(@Param("claimNumber") String claimNumber, @Param("action") String action);

    List<ClaimAudittrail> findByClaimNoAndAction(String claimNumber, String action);


    Integer deleteClaimAudittrailByClaimNoAndAction(String claimNo, String action);

}