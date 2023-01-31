package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Zipcodefacility;

public interface ZipcodefacilityRepository extends JpaRepository<Zipcodefacility, Integer> {

    @Query("SELECT Count(*) FROM Zipcodefacility WHERE zip=:zip AND state=:state")
    Integer validateZipAndState(@Param("zip") String zip,@Param("state") String state);
}