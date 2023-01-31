package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.TOS;

import java.util.List;

public interface TOSRepository extends JpaRepository<TOS,Long> {
    List<TOS> findTOSByStatusEquals(String arg);
    TOS findTOSByCode(String code);

}
