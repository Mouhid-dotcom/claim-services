package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.scrubber.NcdCptGroup;

public interface NcdCptGroupRepository extends JpaRepository<NcdCptGroup, Integer> {
}