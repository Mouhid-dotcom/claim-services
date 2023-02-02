package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rovermd.project.claimservices.entity.Claiminfomaster;

import java.util.List;


@Repository
public interface ClaiminfomasterRepository extends JpaRepository<Claiminfomaster, Integer> {
    List<Claiminfomaster> findAllByClaimType(Integer arg);

    @Query(value = "SELECT SUBSTRING(IFNULL(MAX(Convert(Substring(ClaimNumber,4,8) ,UNSIGNED INTEGER)),0)+10000001,2,7) " +
            "FROM claiminfomaster  ", nativeQuery = true)
    String getNewClaimNumber();

    @Query(value = "SELECT CASE WHEN  IFNULL(ClaimType,'') = 1 THEN 'Institutional' ELSE 'Professional' END " +
            " ,IFNULL(a.ClaimNumber,''),IFNULL(PatientName,''),DATE_FORMAT(DOS,'%m/%d/%Y'),FORMAT(IFNULL(TotalCharges,''),2)," +
            " FORMAT(IFNULL(Balance,IFNULL(TotalCharges,'')),2), " +
            " IFNULL(b.descname,'') " +
            " FROM claimInfoMaster a"+
            " LEFT JOIN claimChargesInfo c ON a.id=c.ClaimInfoMasterId " +
            " LEFT JOIN claim_Status_list b on c.ChargesStatus = b.id " +
            " GROUP BY c.ClaimInfoMasterId ORDER BY COALESCE(a.ViewDate,a.CreatedDate) DESC "
            , nativeQuery = true)
    List<Object[]> getListOfCreatedClaims();

    @Query(value = "SELECT CASE WHEN  IFNULL(ClaimType,'') = 1 THEN 'Institutional' ELSE 'Professional' END " +
            " ,IFNULL(a.ClaimNumber,''),IFNULL(PatientName,''),DATE_FORMAT(DOS,'%m/%d/%Y'),FORMAT(IFNULL(TotalCharges,''),2)," +
            " FORMAT(IFNULL(Balance,IFNULL(TotalCharges,'')),2), " +
            " IFNULL(b.descname,'') " +
            " FROM claimInfoMaster a"+
            " LEFT JOIN claimChargesInfo c ON a.id=c.ClaimInfoMasterId " +
            " LEFT JOIN claim_Status_list b on c.ChargesStatus = b.id " +
            " WHERE " +
            " IFNULL(a.PatientName,'') LIKE %:key% " +
            " OR IFNULL(a.ClaimNumber,'') LIKE %:key% " +
            " OR IFNULL(a.MemId,'') LIKE %:key% " +
            " OR IFNULL(a.PCN,'') LIKE %:key% " +
            " GROUP BY c.ClaimInfoMasterId ORDER BY COALESCE(a.ViewDate,a.CreatedDate) DESC "
            , nativeQuery = true)
    List<Object[]> getListOfCreatedClaimsFiltered(@Param("key") String key);

    @Query(value = "SELECT CASE WHEN  IFNULL(ClaimType,'') = 1 THEN 'Institutional' ELSE 'Professional' END " +
            " ,IFNULL(a.ClaimNumber,''),IFNULL(PatientName,''),DATE_FORMAT(DOS,'%m/%d/%Y'),FORMAT(IFNULL(TotalCharges,''),2)," +
            " FORMAT(IFNULL(Balance,IFNULL(TotalCharges,'')),2), " +
            " IFNULL(b.descname,'') " +
            " FROM claimInfoMaster a"+
            " LEFT JOIN claimChargesInfo c ON a.id=c.ClaimInfoMasterId " +
            " LEFT JOIN claim_Status_list b on c.ChargesStatus = b.id " +
            " WHERE " +
            " a.PatientRegId = :patientRegId " +
            " OR a.VisitId = :visitId " +
            " GROUP BY c.ClaimInfoMasterId ORDER BY COALESCE(a.ViewDate,a.CreatedDate) DESC "
            , nativeQuery = true)
    List<Object[]> getListOfCreatedClaimsFilteredByPatRegIDORVisitID(@Param("patientRegId") Integer patientRegId,@Param("visitId") Integer visitId);

}