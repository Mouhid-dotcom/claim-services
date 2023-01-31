package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Anesthesiacode;

public interface AnesthesiacodeRepository extends JpaRepository<Anesthesiacode, Integer> {
    @Query("SELECT Count(*) from Anesthesiacode where code=:code")
    Integer validateAnesthesiaCodes(@Param("code") String code);
}