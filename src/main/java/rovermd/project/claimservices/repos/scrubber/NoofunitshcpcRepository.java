package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Noofunitshcpc;

public interface NoofunitshcpcRepository extends JpaRepository<Noofunitshcpc, Integer> {

    @Query("SELECT Count(*) from Noofunitshcpc where hCPCSCode=:code")
    Integer isNotNoOfUnits(@Param("code") String code);
}