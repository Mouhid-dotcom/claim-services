package rovermd.project.claimservices.repos.claimMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.claimMaster.ICD;

import java.util.List;

public interface ICDRepository extends JpaRepository<ICD, Long> {

    @Query("SELECT c FROM ICD c " +
            " WHERE CONCAT(IFNULL(c.ICD,''),' ',IFNULL(c.Description,''))" +
            " LIKE %:txt% AND c.EffectiveDate < NOW() AND c.Status=0")
    List<ICD> searchByLike(@Param("txt") String txt);

    List<ICD> findICDByICD(String code);

    @Query("SELECT Count(*) FROM ICD WHERE ICD LIKE 'Z68%' AND ICD=:code")
    Integer validateBMIICD(@Param("code") String code);

    @Query("SELECT Count(*) FROM ICD WHERE SUBSTR(ICD,8)='S' AND ICD=:code")
    Integer validateSequelaCode(@Param("code") String code);



    @Query("SELECT Count(*) FROM ICD where ICD BETWEEN :r1 AND :r2  AND ICD=:icd")
    Integer find_ICD_between_Ranges(@Param("r1") String r1,@Param("r2") String r2,@Param("icd") String icd);


    @Query("SELECT Count(*) FROM ICD where  ICD=:icd")
    Integer validateICD(@Param("icd") String icd);
}
