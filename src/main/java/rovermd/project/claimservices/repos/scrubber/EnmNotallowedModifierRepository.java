package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.EnmNotallowedModifier;

public interface EnmNotallowedModifierRepository extends JpaRepository<EnmNotallowedModifier, Integer> {

    @Query("SELECT Count(*) from EnmNotallowedModifier where mod=:mod")
    Integer validateE_N_M_Modifiers(@Param("mod") String mod);
}