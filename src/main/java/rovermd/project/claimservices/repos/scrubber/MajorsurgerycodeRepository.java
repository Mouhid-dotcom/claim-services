package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Majorsurgerycode;

public interface MajorsurgerycodeRepository extends JpaRepository<Majorsurgerycode, Integer> {

    @Query("SELECT Count(*) from Majorsurgerycode where majorCodes=:code")
    Integer validateMajor_Surgery_ProceduresCodes(@Param("code") String code);
}