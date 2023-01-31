package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.NdcCptCrosswalk;

public interface NdcCptCrosswalkRepository extends JpaRepository<NdcCptCrosswalk, Integer> {

    @Query("SELECT Count(*) from NdcCptCrosswalk where hcpcsCode=:code")
    Integer validateNDC_Code(@Param("code") String code);

    @Query("SELECT Count(*) from NdcCptCrosswalk where hcpcsCode=:code AND ndc=:ndc")
    Integer validateNDC_Code_Format(@Param("code") String code,@Param("ndc") String ndc);
}