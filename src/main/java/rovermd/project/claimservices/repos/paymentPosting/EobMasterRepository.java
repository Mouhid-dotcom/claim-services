package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.paymentPosting.EobMaster;

public interface EobMasterRepository extends JpaRepository<EobMaster, Integer> {
}