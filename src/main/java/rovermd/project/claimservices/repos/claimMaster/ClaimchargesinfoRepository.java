package rovermd.project.claimservices.repos.claimMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.entity.claimMaster.Claimchargesinfo;

import java.util.List;

@Repository
public interface ClaimchargesinfoRepository extends JpaRepository<Claimchargesinfo, Integer> {
    List<Claimchargesinfo> findByClaiminfomasterId(Integer claimInfoMasterId);


    @Modifying
    @Transactional
    @Query(value ="UPDATE ClaimChargesInfo " +
            " SET ChargesStatus = :status , UpdatedBy=:updatedBy , UpdatedAt=NOW() " +
            " WHERE Id =:chargeIdx  ", nativeQuery = true)
    void updateStatusWrtStatus(@Param("status") Integer status,@Param("updatedBy") String updatedBy , @Param("chargeIdx") Integer chargeIdx);
}