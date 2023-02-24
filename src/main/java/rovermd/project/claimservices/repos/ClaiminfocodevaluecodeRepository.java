package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodevaluecode;

import java.util.List;

public interface ClaiminfocodevaluecodeRepository extends JpaRepository<Claiminfocodevaluecode, Integer> {
    List<Claiminfocodevaluecode> findByClaiminfomasterId(Integer claimInfoMasterId);

}