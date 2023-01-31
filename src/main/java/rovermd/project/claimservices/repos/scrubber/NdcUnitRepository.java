package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.scrubber.NdcUnit;

public interface NdcUnitRepository extends JpaRepository<NdcUnit, Integer> {
}