package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rovermd.project.claimservices.entity.Claimadditionalinfo;

@Repository

public interface ClaimadditionalinfoRepository extends JpaRepository<Claimadditionalinfo, Integer> {
    Claimadditionalinfo findByClaiminfomasterId(Integer claimId);
}