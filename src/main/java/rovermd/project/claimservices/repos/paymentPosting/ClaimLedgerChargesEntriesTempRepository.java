package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntriesTemp;

import java.util.List;
import java.util.Map;

public interface ClaimLedgerChargesEntriesTempRepository extends JpaRepository<ClaimLedgerChargesEntriesTemp, Integer> {

    @Query(value = "SELECT IFNULL(DATE_FORMAT(a.DOS,'%m/%d/%Y') ,'') , IFNULL(b.Charges,''), REPLACE(FORMAT(IFNULL(b.Amount,'0.00'),2),',','') " +
            " , REPLACE(FORMAT(IFNULL(b.StartBalance,IFNULL(b.Amount,'0.00')),2),',',''), IFNULL(b.Allowed,'0.00'), REPLACE(FORMAT(IFNULL(b.Paid,'0.00'),2),',',''), IFNULL(b.Remarks,''), IFNULL(b.AdjReasons,''), REPLACE(FORMAT(IFNULL(b.Adjusted,'0.00'),2),',','') " +
            " , IFNULL(b.UnpaidReasons,''), REPLACE(FORMAT(IFNULL(b.Unpaid,'0.00'),2),',',''), REPLACE(FORMAT(IFNULL(b.Deductible,'0.00'),2),',',''), IFNULL(e.descname,''), REPLACE(FORMAT(IFNULL(b.OtherCredits,'0.00'),2),',',''), REPLACE(FORMAT(IFNULL(b.EndBalance,IFNULL(b.Amount,'0.00')),2),',','')" +
            " , IFNULL(LTRIM(rtrim(REPLACE(f.PayerName,'Servicing States','') )),'') , CONCAT(IFNULL(g.DoctorsLastName,''),', ', IFNULL(g.DoctorsFirstName,'')), IFNULL(e.Id,'4'), IFNULL(b.ChargeIdx,''), IFNULL(b.ClaimIdx,''),IFNULL(ClaimType,''),REPLACE(FORMAT(IFNULL(b.SequestrationAmt,'0.00'),2),',','') " +
            " from ClaimInfoMaster a " +
            " INNER JOIN Claim_Ledger_Charges_entries b ON a.ClaimNumber=b.ClaimNumber " +
            " LEFT JOIN claim_Status_list e on b.Status = e.Id " +
            " LEFT JOIN ProfessionalPayers f on a.PriInsuranceNameId = f.Id " +
            " LEFT JOIN DoctorsList g on a.BillingProviders = g.Id " +
            " WHERE a.Id=:claimId and b.TransactionType='Cr' and b.Deleted is NULL "
            , nativeQuery = true)
    List<Object[]> findAllByClaimId(@Param("claimId")  Integer claimId);

    @Query(value = "SELECT ClaimNumber,ClaimIdx,ChargeIdx,Charges,Amount,StartBalance " +
            ",Allowed ,Paid ,Remarks ,AdjReasons ," +
            "Adjusted ,SequestrationAmt ,UnpaidReasons ,Unpaid ,Deductible ,Status ,OtherCredits ,EndBalance, CreatedAt,CreatedBy,UserIP,TransactionIdx,TransactionType, Payment ,Adjustment ,Balance " +
            " FROM Claim_Ledger_Charges_entries_TEMP WHERE ClaimNumber=:claimNumber AND TransactionIdx=:transactionId"
            , nativeQuery = true)
    List<Map<Object,Object>> findAllByClaimIdAndTransactionIdx(@Param("claimNumber")  String claimNumber, @Param("transactionId")  String transactionId);


    void deleteByClaimNumberAndTransactionIdx(String claimNumber,String TransactionIdx);

}