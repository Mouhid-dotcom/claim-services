package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Anesthesiamodifier;

public interface AnesthesiamodifierRepository extends JpaRepository<Anesthesiamodifier, Integer> {
    @Query("SELECT Count(*) from Anesthesiamodifier where modifier=:mod")
    Integer validateAnesthesiaModifier(@Param("mod") String mod);
}