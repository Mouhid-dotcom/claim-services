package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntries;

import java.util.List;
import java.util.Map;

public interface ClaimLedgerChargesEntriesRepository extends JpaRepository<ClaimLedgerChargesEntries, Integer> {
    @Query(value = "SELECT IFNULL(DATE_FORMAT(a.DOS,'%m/%d/%Y') ,'') as dos, " +
            "  IFNULL(b.Charges,'') as 'procedure', REPLACE(FORMAT(IFNULL(b.Amount,'0.00'),2),',','') as amount " +
            " , REPLACE(FORMAT(IFNULL(b.StartBalance,IFNULL(b.Amount,'0.00')),2),',','') as startBalance, IFNULL(b.Allowed,'0.00') as allowed, REPLACE(FORMAT(IFNULL(b.Paid,'0.00'),2),',','') as paid, IFNULL(b.Remarks,'') as remarks, IFNULL(b.AdjReasons,'') as adjustedReason, REPLACE(FORMAT(IFNULL(b.Adjusted,'0.00'),2),',','') as adjusted " +
            " , IFNULL(b.UnpaidReasons,'') as unpaidReason, REPLACE(FORMAT(IFNULL(b.Unpaid,'0.00'),2),',','') as unpaid, REPLACE(FORMAT(IFNULL(b.Deductible,'0.00'),2),',','') as deductible, IFNULL(e.descname,'') as status, REPLACE(FORMAT(IFNULL(b.OtherCredits,'0.00'),2),',','') as otherCredits, REPLACE(FORMAT(IFNULL(b.EndBalance,IFNULL(b.Amount,'0.00')),2),',','') as endBalance" +
            " , IFNULL(LTRIM(rtrim(REPLACE(f.PayerName,'Servicing States','') )),'') as insuranceName , CONCAT(IFNULL(g.DoctorsLastName,''),', ', IFNULL(g.DoctorsFirstName,'')) as renderingProvider, IFNULL(e.Id,'4') as statusId, IFNULL(b.ChargeIdx,'') as chargeId, IFNULL(b.ClaimIdx,'') as claimId,IFNULL(ClaimType,'') as claimType ,REPLACE(FORMAT(IFNULL(b.SequestrationAmt,'0.00'),2),',','') as sequestrationAmount " +
            " from ClaimInfoMaster a " +
            " INNER JOIN Claim_Ledger_Charges_entries b ON a.ClaimNumber=b.ClaimNumber " +
            " LEFT JOIN claim_Status_list e on b.Status = e.Id " +
            " LEFT JOIN ProfessionalPayers f on a.PriInsuranceNameId = f.Id " +
            " LEFT JOIN DoctorsList g on a.BillingProviders = g.Id " +
            " WHERE a.Id=:claimId and b.TransactionType='Cr' and b.Deleted is NULL "
            , nativeQuery = true)
    List<Map<Object,Object>> findAllByClaimIdxTransactionTypeCr(@Param("claimId")  Integer claimId);

    @Query(value = "SELECT IFNULL(DATE_FORMAT(a.DOS,'%m/%d/%Y') ,'') as dos, " +
            "  IFNULL(b.Charges,'') as 'procedure', REPLACE(FORMAT(IFNULL(b.Amount,'0.00'),2),',','') as amount " +
            " , REPLACE(FORMAT(IFNULL(b.StartBalance,IFNULL(b.Amount,'0.00')),2),',','') as startBalance, IFNULL(b.Allowed,'0.00') as allowed, REPLACE(FORMAT(IFNULL(b.Paid,'0.00'),2),',','') as paid, IFNULL(b.Remarks,'') as remarks, IFNULL(b.AdjReasons,'') as adjustedReason, REPLACE(FORMAT(IFNULL(b.Adjusted,'0.00'),2),',','') as adjusted " +
            " , IFNULL(b.UnpaidReasons,'') as unpaidReason, REPLACE(FORMAT(IFNULL(b.Unpaid,'0.00'),2),',','') as unpaid, REPLACE(FORMAT(IFNULL(b.Deductible,'0.00'),2),',','') as deductible, IFNULL(e.descname,'') as status, REPLACE(FORMAT(IFNULL(b.OtherCredits,'0.00'),2),',','') as otherCredits, REPLACE(FORMAT(IFNULL(b.EndBalance,IFNULL(b.Amount,'0.00')),2),',','') as endBalance" +
            " , IFNULL(LTRIM(rtrim(REPLACE(f.PayerName,'Servicing States','') )),'') as insuranceName , CONCAT(IFNULL(g.DoctorsLastName,''),', ', IFNULL(g.DoctorsFirstName,'')) as renderingProvider, IFNULL(e.Id,'4') as statusId, IFNULL(b.ChargeIdx,'') as chargeId, IFNULL(b.ClaimIdx,'') as claimId,IFNULL(ClaimType,'') as claimType ,REPLACE(FORMAT(IFNULL(b.SequestrationAmt,'0.00'),2),',','') as sequestrationAmount " +
            " from ClaimInfoMaster a " +
            " INNER JOIN Claim_Ledger_Charges_entries b ON a.ClaimNumber=b.ClaimNumber " +
            " LEFT JOIN claim_Status_list e on b.Status = e.Id " +
            " LEFT JOIN ProfessionalPayers f on a.PriInsuranceNameId = f.Id " +
            " LEFT JOIN DoctorsList g on a.BillingProviders = g.Id " +
            " WHERE a.Id=:claimId and b.TransactionType='D' and b.Deleted is NULL AND TransactionIdx=:transactionId "
            , nativeQuery = true)
    List<Map<Object,Object>> findAllByClaimIdxAndTransactionIdx(@Param("claimId")  Integer claimId,@Param("transactionId")  String transactionId);


    @Query(value = "SELECT DISTINCT(ClaimNumber)  from Claim_Ledger_Charges_entries where TransactionIdx=:transactionId", nativeQuery = true)
    List<String> selectDistinctClaimNumbers(@Param("transactionId")  Integer transactionId);

    @Query(value = "SELECT FORMAT(SUM(IFNULL(b.Allowed, '0')),2) as Allowed, " +
            " FORMAT(SUM(IFNULL(b.Paid, '0')),2) as Paid, " +
            " FORMAT(SUM(IFNULL(b.Adjusted, '0')),2) as Adjusted, " +
            " FORMAT(SUM(IFNULL(b.Unpaid, '0')),2) as Unpaid, " +
            " FORMAT(SUM(IFNULL(b.OtherCredits, '0')),2) as OtherCredits," +
            " FORMAT(SUM(b.EndBalance),2) as EndBalance" +
            " from Claim_Ledger_Charges_entries b" +
            " WHERE b.TransactionIdx = :transactionId and b.ClaimNumber=:claimNumber AND TransactionType='D' "
            , nativeQuery = true)
    List<Map<Object,Object>> getClaimLedgerChargesEntriesDetails(@Param("claimNumber")  String claimNumber,@Param("transactionId")  String transactionId);


    @Modifying
    @Transactional
    @Query(value ="UPDATE Claim_Ledger_Charges_entries SET " +
            " StartBalance=:endBalance, TransactionIdx=:transactionIdx, EndBalance=:endBalance" +
            " WHERE ChargeIdx = :chargeIdx  AND TransactionType='Cr' AND ClaimIdx=:claimIdx " , nativeQuery = true)
    void updateClaim_Ledger_Charges_entries(@Param("endBalance") String endBalance, @Param("transactionIdx") String transactionIdx , @Param("chargeIdx") String chargeIdx,
                              @Param("claimIdx") String claimIdx);


    @Query(value = "SELECT Sum(Paid) as Paid , Sum(Adjusted) as Adjusted "  +
            " FROM Claim_Ledger_Charges_entries " +
            " WHERE claimNumber=:claimNumber AND ChargeIdx=:chargeIdx"
            , nativeQuery = true)
//    Map<Object,Object> getSumOfPaidAndAdjusted(@Param("claimNumber")  String claimNumber,@Param("chargeIdx")  Integer chargeIdx);
    Map<Object,Object> getSumOfPaidAndAdjusted(@Param("claimNumber")  String claimNumber,@Param("chargeIdx")  String chargeIdx);


}