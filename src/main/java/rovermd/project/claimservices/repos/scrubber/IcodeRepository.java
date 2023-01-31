package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Icode;

public interface IcodeRepository extends JpaRepository<Icode, Integer> {

    @Query("SELECT Count(*) from Icode where hcpcs=:code")
    Integer validateI_Codes(@Param("code") String code);
}