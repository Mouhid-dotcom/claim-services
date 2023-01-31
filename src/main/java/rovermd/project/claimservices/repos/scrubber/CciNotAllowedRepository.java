package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.CciNotAllowed;

public interface CciNotAllowedRepository extends JpaRepository<CciNotAllowed, Long> {

    @Query("SELECT Count(*) FROM CciNotAllowed WHERE cpt1=:cpt1 AND cpt2=:cpt2")
    Integer isNotAllowed_NCCI(@Param("cpt1") String cpt1, @Param("cpt2") String cpt2);
}