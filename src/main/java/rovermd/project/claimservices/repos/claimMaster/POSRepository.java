package rovermd.project.claimservices.repos.claimMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.claimMaster.POS;

import java.util.List;

public interface POSRepository extends JpaRepository<POS,Long> {
    List<POS> findPOSByStatusEquals(String arg);
    POS findPOSByCode(String code);
}
