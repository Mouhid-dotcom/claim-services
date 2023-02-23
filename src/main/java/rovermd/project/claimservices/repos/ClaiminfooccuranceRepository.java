package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.Claiminfocodeconditioncode;
import rovermd.project.claimservices.entity.Claiminfooccurance;

import java.util.List;

public interface ClaiminfooccuranceRepository extends JpaRepository<Claiminfooccurance, Integer> {
    List<Claiminfooccurance> findByClaiminfomasterId(Integer claimInfoMasterId);
}