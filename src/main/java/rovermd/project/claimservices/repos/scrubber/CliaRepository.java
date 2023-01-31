package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Clia;

public interface CliaRepository extends JpaRepository<Clia, Integer> {

    @Query("SELECT Count(*) from Clia where hcpcs=:code")
    Integer validateCLIACodes(@Param("code") String code);
}