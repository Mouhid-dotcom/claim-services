package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.CPT;

import java.util.List;

public interface CPTRepository extends JpaRepository<CPT, Long> {
    @Query("SELECT c FROM CPT c WHERE CONCAT(IFNULL(c.CPTCode,''),' ',IFNULL(c.CPTDescription,'')) LIKE %:txt% AND c.EffectiveDate < NOW()")
    List<CPT> searchByLike(@Param("txt") String txt);

//    @Query("SELECT c FROM CPT c WHERE CONCAT(IFNULL(c.CPTCode,''),' ',IFNULL(c.CPTDescription,'')) LIKE %:txt% AND c.EffectiveDate < NOW()")
    List<CPT> findCPTByCPTCode(String code);

    @Query("SELECT Count(*) FROM CPT where (CPTCode BETWEEN '99201' AND '99357' OR  CPTCode BETWEEN '92002' AND '92014')  AND CPTCode=:code")
    Integer validateOFFICE_E_N_M(@Param("code") String code);

    @Query("SELECT Count(*) FROM CPT where CPTCode BETWEEN :r1 AND :r2  AND CPTCode=:icd")
    Integer find_CPT_between_Ranges(@Param("r1") String r1,@Param("r2") String r2,@Param("icd") String icd);

    @Query("SELECT Count(*) FROM CPT where (CPTCode BETWEEN '99201' AND '99357' OR  CPTCode BETWEEN '92002' AND '92014')  AND CPTCode=:code")
    Integer validateMammographyCPT(@Param("code") String code);

    @Query("SELECT Count(*) FROM CPT where  CPTCode=:code")
    Integer validateCPT(@Param("code") String code);

}
