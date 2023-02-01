package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.CciModMust;

public interface CciModMustRepository extends JpaRepository<CciModMust, Long> {

    @Query("SELECT Count(*) FROM CciModMust WHERE cpt1=:cpt1 AND cpt2=:cpt2")
    Integer Mod_Is_MUST_NCCI(@Param("cpt1") String cpt1,@Param("cpt2") String cpt2);
}