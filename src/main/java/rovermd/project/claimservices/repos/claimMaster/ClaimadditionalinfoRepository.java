package rovermd.project.claimservices.repos.claimMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rovermd.project.claimservices.entity.claimMaster.Claimadditionalinfo;

@Repository

public interface ClaimadditionalinfoRepository extends JpaRepository<Claimadditionalinfo, Integer> {
    Claimadditionalinfo findByClaiminfomasterId(Integer claimId);
}