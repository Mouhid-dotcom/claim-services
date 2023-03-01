package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntriesTemp;

import java.util.List;
import java.util.Map;

public interface ClaimLedgerChargesEntriesTempRepository extends JpaRepository<ClaimLedgerChargesEntriesTemp, Integer> {

    @Query(value = "SELECT IFNULL(DATE_FORMAT(a.DOS,'%m/%d/%Y') ,'') as dos, " +
            "  IFNULL(b.Charges,'') as 'procedure', REPLACE(FORMAT(IFNULL(b.Amount,'0.00'),2),',','') as amount " +
            " , REPLACE(FORMAT(IFNULL(b.StartBalance,IFNULL(b.Amount,'0.00')),2),',','') as startBalance," +
            " IFNULL(b.Allowed,'0.00') as allowed, REPLACE(FORMAT(IFNULL(b.Paid,'0.00'),2),',','') as paid," +
            " IFNULL(b.Remarks,'') as remarks, IFNULL(b.AdjReasons,'') as adjustedReason," +
            " REPLACE(FORMAT(IFNULL(b.Adjusted,'0.00'),2),',','') as adjusted " +
            " , IFNULL(b.UnpaidReasons,'') as unpaidReason, REPLACE(FORMAT(IFNULL(b.Unpaid,'0.00'),2),',','') as unpaid," +
            " REPLACE(FORMAT(IFNULL(b.Deductible,'0.00'),2),',','') as deductible, IFNULL(e.descname,'') as status," +
            " REPLACE(FORMAT(IFNULL(b.OtherCredits,'0.00'),2),',','') as otherCredits," +
            " REPLACE(FORMAT(IFNULL(b.EndBalance,IFNULL(b.Amount,'0.00')),2),',','') as endBalance" +
            " , IFNULL(LTRIM(rtrim(REPLACE(f.PayerName,'Servicing States','') )),'') as insuranceName ," +
            " CONCAT(IFNULL(g.DoctorsLastName,''),', ', IFNULL(g.DoctorsFirstName,'')) as renderingProvider," +
            " IFNULL(e.Id,'4') as statusId, IFNULL(b.ChargeIdx,'') as chargeId, IFNULL(b.ClaimIdx,'') as claimId," +
            " IFNULL(ClaimType,'') as claimType ,REPLACE(FORMAT(IFNULL(b.SequestrationAmt,'0.00'),2),',','') as sequestrationAmount,b.Id as tempTableIdx " +
            " from ClaimInfoMaster a " +
            " INNER JOIN claim_ledger_charges_entries_temp b ON a.ClaimNumber=b.ClaimNumber " +
            " LEFT JOIN claim_Status_list e on b.Status = e.Id " +
            " LEFT JOIN ProfessionalPayers f on a.PriInsuranceNameId = f.Id " +
            " LEFT JOIN DoctorsList g on a.BillingProviders = g.Id " +
            " WHERE a.Id=:claimId and b.TransactionType='D' and b.Deleted is NULL AND TransactionIdx=:transactionId "
            , nativeQuery = true)
    List<Map<Object,Object>> findAllByClaimIdxAndTransactionIdxTransactionTypeD(@Param("claimId")  Integer claimId, @Param("transactionId")  Integer transactionId);

    @Query(value = "SELECT ClaimNumber,ClaimIdx,ChargeIdx,Charges,Amount,StartBalance " +
            ",Allowed ,Paid ,Remarks ,AdjReasons ," +
            "Adjusted ,SequestrationAmt ,UnpaidReasons ,Unpaid ,Deductible ,Status ,OtherCredits ,EndBalance, CreatedAt,CreatedBy,UserIP,TransactionIdx,TransactionType, Payment ,Adjustment ,Balance " +
            " FROM Claim_Ledger_Charges_entries_TEMP WHERE ClaimNumber=:claimNumber AND TransactionIdx=:transactionId"
            , nativeQuery = true)
    List<Map<Object,Object>> findAllByClaimIdAndTransactionIdx(@Param("claimNumber")  String claimNumber, @Param("transactionId")  String transactionId);


    void deleteByClaimNumberAndTransactionIdx(String claimNumber,String TransactionIdx);

}