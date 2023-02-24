package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodeoccspan;

import java.util.List;

public interface ClaiminfocodeoccspanRepository extends JpaRepository<Claiminfocodeoccspan, Integer> {
    List<Claiminfocodeoccspan> findByClaiminfomasterId(Integer claimInfoMasterId);

}