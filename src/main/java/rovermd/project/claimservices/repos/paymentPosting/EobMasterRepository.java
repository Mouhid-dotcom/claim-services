package rovermd.project.claimservices.repos.paymentPosting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.paymentPosting.EobMaster;

import java.util.List;
import java.util.Map;

public interface EobMasterRepository extends JpaRepository<EobMaster, Integer> {

    @Query(value = "SELECT DATE_FORMAT(ReceivedDate,'%Y-%m-%d') as ReceivedDate  , IFNULL(PaymentAmount,'0.00') as PaymentAmount , " +
            "IFNULL(AppliedAmount,'0.00') as AppliedAmount, IFNULL(UnappliedAmount, IFNULL(PaymentAmount,'0.00')) as UnappliedAmount, IFNULL(OtherRefrenceNo,'') as OtherRefrenceNo" +
            ",Id, IFNULL(Claim_ERA_Id,'-1') " +
            " from EOB_Master" +
            " WHERE Id=:transactionId"
            , nativeQuery = true)
    Map<Object,Object> getEOBMasterDetails(@Param("transactionId")  Integer transactionId);

}