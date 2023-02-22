package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rovermd.project.claimservices.entity.Claimchargesinfo;

import java.util.List;

@Repository
public interface ClaimchargesinfoRepository extends JpaRepository<Claimchargesinfo, Integer> {
    List<Claimchargesinfo> findByClaiminfomasterId(Integer claimInfoMasterId);
}