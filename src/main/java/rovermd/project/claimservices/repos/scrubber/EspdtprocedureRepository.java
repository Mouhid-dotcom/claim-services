package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Espdtprocedure;

public interface EspdtprocedureRepository extends JpaRepository<Espdtprocedure, Integer> {

    @Query("SELECT Count(*) from Espdtprocedure where espdt=:code")
    Integer validateEPSDT_ProceduresCodes(@Param("code") String code);

}