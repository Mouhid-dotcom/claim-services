package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.entity.paymentPosting.EobMaster;

import java.math.BigDecimal;
import java.util.Map;

public interface EobMasterRepository extends JpaRepository<EobMaster, Integer> {

    @Query(value = "SELECT DATE_FORMAT(ReceivedDate,'%Y-%m-%d') as ReceivedDate  , IFNULL(PaymentAmount,'0.00') as PaymentAmount , " +
            "IFNULL(AppliedAmount,'0.00') as AppliedAmount, IFNULL(UnappliedAmount, IFNULL(PaymentAmount,'0.00')) as UnappliedAmount," +
            " IFNULL(OtherRefrenceNo,'') as OtherRefrenceNo,Id, IFNULL(Claim_ERA_Id,'-1') , InsuranceIdx  " +
            " from EOB_Master" +
            " WHERE Id=:transactionId"
            , nativeQuery = true)
    Map<String,Object> getEOBMaster(@Param("transactionId")  Integer transactionId);


    @Query(value = "SELECT IFNULL(PatientIdx,'-1') as PatientIdx  ,IFNULL(PaymentAmount,'') as PaymentAmount ,IFNULL(ReceivedDate,'') as ReceivedDate ,IFNULL(CheckNumber,'') as CheckNumber ,IFNULL(PaymentType,'') as PaymentType ," +
            " IFNULL(PaymentSource,'') as PaymentSource ,IFNULL(Memo,'') as Memo ,IFNULL(Status,'-1') as Status ,CopayDOS ,IFNULL(CardType,'') as CardType  ,IFNULL(CreatedDate,'') as CreatedDate ,IFNULL(CreatedBy,'') as CreatedBy,IFNULL(ModifyBy,'') as ModifyBy ,IFNULL(ModifyDate,'') as ModifyDate " +
            " ,IFNULL(InsuranceIdx,'') as InsuranceIdx ,IFNULL(OtherRefrenceNo,'') as OtherRefrenceNo  ,IFNULL(PaymentFrom,'') as PaymentFrom ,IFNULL(isPaymentOnly,'-1') as isPaymentOnly,IFNULL(isCreditAccount,'-1') as isCreditAccount ,IFNULL(AppliedAmount,'') as AppliedAmount ,IFNULL(UnappliedAmount,'') as UnappliedAmount" +
            " FROM EOB_Master WHERE Id=:transactionId", nativeQuery = true )
    Map<String,String> getEOBMasterDetails(@Param("transactionId")  Integer transactionId);

    @Modifying
    @Transactional
    @Query(value ="UPDATE EOB_Master SET " +
            "  AppliedAmount= :appliedAmount" +
            " ,UnappliedAmount= :unappliedAmount" +
            " WHERE Id= :transactionIdx", nativeQuery = true)
    void updateAppliedAmountAndUnappliedAmount(@Param("appliedAmount") BigDecimal appliedAmount,
                                               @Param("unappliedAmount") BigDecimal unappliedAmount ,@Param("transactionIdx") Integer transactionIdx);



}