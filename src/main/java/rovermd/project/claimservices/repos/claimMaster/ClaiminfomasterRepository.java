package rovermd.project.claimservices.repos.claimMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.entity.claimMaster.Claiminfomaster;

import java.util.List;
import java.util.Map;


@Repository
public interface ClaiminfomasterRepository extends JpaRepository<Claiminfomaster, Integer> {
    List<Claiminfomaster> findAllByClaimType(Integer arg);

    @Query(value = "SELECT SUBSTRING(IFNULL(MAX(Convert(Substring(ClaimNumber,4,8) ,UNSIGNED INTEGER)),0)+10000001,2,7) " +
            "FROM claiminfomaster  ", nativeQuery = true)
    String getNewClaimNumber();

    @Query(value = "SELECT IFNULL(MAX(InterControlNo),'') " +
            "FROM claiminfomaster  ", nativeQuery = true)
    String getInterControlNo();

    @Query(value = "SELECT CASE WHEN  IFNULL(ClaimType,'') = 1 THEN 'Institutional' ELSE 'Professional' END as type " +
            " ,IFNULL(a.ClaimNumber,'') as claimNo,IFNULL(PatientName,'') as patientName,DATE_FORMAT(DOS,'%m/%d/%Y') as dos,FORMAT(IFNULL(TotalCharges,''),2) as totalCharges," +
            " FORMAT(IFNULL(Balance,IFNULL(TotalCharges,'')),2) as balance, " +
            " IFNULL(b.descname,'') as status ,a.Id as id " +
            " FROM claimInfoMaster a"+
            " LEFT JOIN claimChargesInfo c ON a.id=c.ClaimInfoMasterId " +
            " LEFT JOIN claim_Status_list b on c.ChargesStatus = b.id " +
            " GROUP BY c.ClaimInfoMasterId ORDER BY COALESCE(a.ViewDate,a.CreatedDate) DESC "
            , nativeQuery = true)
    List<Map<Object,Object>> getListOfCreatedClaims();


    @Query(value = "SELECT CASE WHEN  IFNULL(ClaimType,'') = 1 THEN 'Institutional' ELSE 'Professional' END as type " +
            " ,IFNULL(a.ClaimNumber,'') as claimNo,IFNULL(PatientName,'') as patientName,DATE_FORMAT(DOS,'%m/%d/%Y') as dos,FORMAT(IFNULL(TotalCharges,''),2) as totalCharges," +
            " FORMAT(IFNULL(Balance,IFNULL(TotalCharges,'')),2) as balance, " +
            " IFNULL(b.descname,'') as status ,a.Id as id " +
            " FROM claimInfoMaster a"+
            " LEFT JOIN claimChargesInfo c ON a.id=c.ClaimInfoMasterId " +
            " LEFT JOIN claim_Status_list b on c.ChargesStatus = b.id" +
            " WHERE a.PriInsuranceNameId = :insuranceIdx " +
            " GROUP BY c.ClaimInfoMasterId ORDER BY COALESCE(a.ViewDate,a.CreatedDate) DESC "
            , nativeQuery = true)
    List<Map<Object,Object>> getListOfCreatedClaimsByInsuranceIdx(@Param("insuranceIdx") Integer insuranceIdx);

    @Query(value = "SELECT CASE WHEN  IFNULL(ClaimType,'') = 1 THEN 'Institutional' ELSE 'Professional' END as type " +
            " ,IFNULL(a.ClaimNumber,'') as claimNo,IFNULL(PatientName,'') as patientName,DATE_FORMAT(DOS,'%m/%d/%Y') as dos,FORMAT(IFNULL(TotalCharges,''),2) as totalCharges," +
            " FORMAT(IFNULL(Balance,IFNULL(TotalCharges,'')),2) as balance, " +
            " IFNULL(b.descname,'') as status" +
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
    List<Map<Object,Object>> getListOfCreatedClaimsFiltered(@Param("key") String key);

    @Query(value = "SELECT CASE WHEN  IFNULL(ClaimType,'') = 1 THEN 'Institutional' ELSE 'Professional' END as type " +
            " ,IFNULL(a.ClaimNumber,'') as claimNo,IFNULL(PatientName,'') as patientName,DATE_FORMAT(DOS,'%m/%d/%Y') as dos,FORMAT(IFNULL(TotalCharges,''),2) as totalCharges," +
            " FORMAT(IFNULL(Balance,IFNULL(TotalCharges,'')),2) as balance, " +
            " IFNULL(b.descname,'') as status " +
            " FROM claimInfoMaster a"+
            " LEFT JOIN claimChargesInfo c ON a.id=c.ClaimInfoMasterId " +
            " LEFT JOIN claim_Status_list b on c.ChargesStatus = b.id " +
            " WHERE " +
            " a.PatientRegId = :patientRegId " +
            " OR a.VisitId = :visitId " +
            " GROUP BY c.ClaimInfoMasterId ORDER BY COALESCE(a.ViewDate,a.CreatedDate) DESC "
            , nativeQuery = true)
    List<Map<Object,Object>> getListOfCreatedClaimsFilteredByPatRegIDORVisitID(@Param("patientRegId") Integer patientRegId,@Param("visitId") Integer visitId);

    @Modifying
    @Transactional
    @Query(value ="UPDATE claimInfoMaster SET Status='3', timesSubmitted=:timesSubmitted ," +
            " PCN=:pcn ," +
            " EDI_FILE =:filename ," +
            " EDI_DIR='Professional'," +
            " EDI_GENERATED_BY=:userId," +
            "InterControlNo=:intercontrolno" +
            " WHERE ClaimNumber=:claimNumber " , nativeQuery = true)
    void updateTimesSubmitted(@Param("pcn") String pcn, @Param("filename") String filename ,
                           @Param("userId") String userId, @Param("claimNumber") String claimNumber,
                              @Param("timesSubmitted") String timesSubmitted,@Param("intercontrolno") String intercontrolno);


    @Query(value ="SELECT IFNULL(a.PatientName,'') as  PatientName ,IFNULL(a.AcctNo,'') as AcctNo , IFNULL(a.PCN,'') as PCN, " +
            "IFNULL(DATE_FORMAT(a.DOS,'%m/%d/%Y'),'') as DOS ,IFNULL(a.ClaimNumber,'') as ClaimNumber,FORMAT(IFNULL(a.TotalCharges,'0.00'),2) as TotalCharges" +
            " FROM  ClaimInfoMaster a" +
            " LEFT JOIN ProfessionalPayers f on a.PriInsuranceNameId = f.Id " +
            " WHERE a.ClaimNumber=?" , nativeQuery = true)
    List<Map<Object,Object>> getClaimDetails(@Param("claimNumber")  String claimNumber);

    @Query(value ="SELECT TotalCharges " +
            " FROM ClaimInfoMaster " +
            " WHERE ClaimNumber=? " , nativeQuery = true)
    Double getTotalCharges(@Param("claimNumber")  String claimNumber);


    @Modifying
    @Transactional
    @Query(value ="UPDATE ClaimInfoMaster " +
            "Set " +
            " Paid=:paid ," +
            " Adjusted=:adjusted ," +
            " Balance=:balance " +
            "WHERE ClaimNumber=:claimNumber" , nativeQuery = true)
    void updatePaidAndAdjustedAndBalance(@Param("paid") Double paid, @Param("adjusted") Double adjusted ,
                              @Param("balance") Double balance, @Param("claimNumber") String claimNumber);

}