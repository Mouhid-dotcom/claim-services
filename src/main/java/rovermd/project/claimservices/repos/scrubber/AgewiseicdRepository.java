package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Agewiseicd;

public interface AgewiseicdRepository extends JpaRepository<Agewiseicd, Integer> {

    @Query("SELECT Count(*) FROM Agewiseicd a WHERE a.icd=:code")
    Integer validateAgeICDs(@Param("code") String code);

    @Query("SELECT Count(*) FROM Agewiseicd a WHERE a.icd=:code AND a.upperLimit>=:age AND a.lowerLimit<=:age ")
    Integer validateAgeICDswithUpperAndLowerLimit(@Param("code") String code,@Param("age") int age);
}