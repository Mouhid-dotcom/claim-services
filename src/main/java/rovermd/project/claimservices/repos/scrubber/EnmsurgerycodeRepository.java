package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Enmsurgerycode;

public interface EnmsurgerycodeRepository extends JpaRepository<Enmsurgerycode, Integer> {

    @Query("SELECT Count(*) from Enmsurgerycode where surgeryCodes=:code")
    Integer validateE_N_M_Surgery_ProceduresCodes(@Param("code") String code);
}