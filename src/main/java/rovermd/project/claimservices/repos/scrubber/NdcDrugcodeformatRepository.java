package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.scrubber.NdcDrugcodeformat;

public interface NdcDrugcodeformatRepository extends JpaRepository<NdcDrugcodeformat, Integer> {
}