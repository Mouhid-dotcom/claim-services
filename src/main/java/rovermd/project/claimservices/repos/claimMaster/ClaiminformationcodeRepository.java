package rovermd.project.claimservices.repos.claimMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rovermd.project.claimservices.entity.claimMaster.Claiminformationcode;

@Repository
public interface ClaiminformationcodeRepository extends JpaRepository<Claiminformationcode, Integer> {
}