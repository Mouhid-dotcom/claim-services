package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Pqrscode;

public interface PqrscodeRepository extends JpaRepository<Pqrscode, Integer> {

    @Query("SELECT Count(*) from Pqrscode where procedureCodes=:code")
    Integer validatePQRSCodes(@Param("code") String code);
}