package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Enmprocedure;

public interface EnmprocedureRepository extends JpaRepository<Enmprocedure, Long> {

    @Query("SELECT Count(*) from Enmprocedure where CPT =:code")
    Integer validateE_N_M_ProceduresCodes(@Param("code") String code);
}