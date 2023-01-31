package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Agewiseicd;

public interface AgewiseicdRepository extends JpaRepository<Agewiseicd, Integer> {

    @Query("SELECT Count(*) FROM Agewiseicd WHERE ICD=:code")
    Integer validateAgeICDs(@Param("code") String code);

    @Query("SELECT Count(*) FROM Agewiseicd WHERE ICD=:code AND upperLimit>=:age AND lowerLimit<=:age ")
    Integer validateAgeICDswithUpperAndLowerLimit(@Param("code") String code,@Param("age") int age);
}