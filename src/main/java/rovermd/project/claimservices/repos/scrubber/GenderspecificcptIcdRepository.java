package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.GenderspecificcptIcd;

public interface GenderspecificcptIcdRepository extends JpaRepository<GenderspecificcptIcd, Long> {

    @Query("SELECT Count(*) FROM GenderspecificcptIcd WHERE icd=:icd AND gender=:gender")
    Integer validateGenderCPTs(@Param("icd") String icd, @Param("gender") String gender);
}