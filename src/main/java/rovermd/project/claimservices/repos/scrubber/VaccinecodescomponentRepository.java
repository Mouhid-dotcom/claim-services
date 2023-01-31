package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Vaccinecodescomponent;

public interface VaccinecodescomponentRepository extends JpaRepository<Vaccinecodescomponent, Integer> {

    @Query("SELECT Count(*) from Vaccinecodescomponent where vaccineCode=:code")
    Integer validateVaccineCode(@Param("code") String code);
}