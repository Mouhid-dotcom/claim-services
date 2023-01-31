package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Zipcodelibrary;

public interface ZipcodelibraryRepository extends JpaRepository<Zipcodelibrary, Integer> {

    @Query("SELECT Count(*) FROM Zipcodelibrary WHERE zIPCode=:zip AND state=:state")
    Integer validateZipAndState(@Param("zip") String zip, @Param("state") String state);
}