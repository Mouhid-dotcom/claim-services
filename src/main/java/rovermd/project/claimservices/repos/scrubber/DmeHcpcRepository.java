package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.DmeHcpc;

public interface DmeHcpcRepository extends JpaRepository<DmeHcpc, Long> {

    @Query("SELECT Count(*) from DmeHcpc where hcpcs=:code")
    Integer validateDMEProceduresCodes(@Param("code") String code);
}