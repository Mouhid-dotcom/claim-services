package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Mcrvaccinerulecode;

import java.util.List;

public interface McrvaccinerulecodeRepository extends JpaRepository<Mcrvaccinerulecode, Integer> {

    @Query("SELECT Count(*) FROM Mcrvaccinerulecode where vaccineCodes=:chargeprocedure AND iCD10Code=:ICD  AND administration=:Code")
    Integer validateRelativeAdminCode(@Param("chargeprocedure") String chargeprocedure, @Param("ICD") String ICD, @Param("Code") String Code);

    @Query("SELECT vaccineCodes FROM Mcrvaccinerulecode where iCD10Code=:ICD AND administration=:Code")
    List<String> getRespectiveVaccineCode(@Param("ICD") String ICD, @Param("Code") String Code);
}