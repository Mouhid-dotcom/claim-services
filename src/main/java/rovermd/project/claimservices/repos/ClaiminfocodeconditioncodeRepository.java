package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.claimMaster.Claiminfocodeconditioncode;

import java.util.List;

public interface ClaiminfocodeconditioncodeRepository extends JpaRepository<Claiminfocodeconditioncode, Integer> {
    List<Claiminfocodeconditioncode> findByClaiminfomasterId(Integer claimInfoMasterId);

}