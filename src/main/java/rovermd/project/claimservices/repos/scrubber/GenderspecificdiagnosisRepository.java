package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Genderspecificdiagnosis;

public interface GenderspecificdiagnosisRepository extends JpaRepository<Genderspecificdiagnosis, Integer> {

    @Query("SELECT Count(*) FROM Genderspecificdiagnosis WHERE icd=:icd AND sex=:sex")
    Integer validateGenderICDs(@Param("icd") String icd, @Param("sex") String sex);
}