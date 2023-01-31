package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Consultantprocedure;

public interface ConsultantprocedureRepository extends JpaRepository<Consultantprocedure, Integer> {

    @Query("SELECT Count(*) FROM Consultantprocedure WHERE cPTCode=:code")
    Integer validateConsultantProceduresCodes(@Param("code") String code);
}