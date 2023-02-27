package rovermd.project.claimservices.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rovermd.project.claimservices.dto.insurancePaymentPosting.Request.ChargesWRTClaimDto;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntries;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntriesTemp;
import rovermd.project.claimservices.repos.claimMaster.ClaiminfomasterRepository;
import rovermd.project.claimservices.repos.paymentPosting.ClaimLedgerChargesEntriesRepository;
import rovermd.project.claimservices.service.ClaimPaymentPostingService;

@Service
public class ClaimPaymentPostingServiceImpl implements ClaimPaymentPostingService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ClaimLedgerChargesEntriesRepository claimLedgerChargesEntriesRepository;

    @Override
    public Object setCharges_WRT_Claim(Integer flag, ChargesWRTClaimDto chargesWRTClaims) {

        if(1==flag){//inserting new payments temp table
            chargesWRTClaims.getCharges().forEach(x->{
                ClaimLedgerChargesEntriesTemp claimLedgerChargesEntriesTemp = new ClaimLedgerChargesEntriesTemp();
                claimLedgerChargesEntriesTemp.setCharges(x.getCharges());
                claimLedgerChargesEntriesTemp.setClaimNumber(x.getClaimNo());
                claimLedgerChargesEntriesTemp.setTransactionType("D");
                claimLedgerChargesEntriesTemp.setClaimIdx(x.getClaimIdx());
                claimLedgerChargesEntriesTemp.setChargeIdx(x.getChargeIdx());
                claimLedgerChargesEntriesTemp.setCharges(x.getCharges());
                claimLedgerChargesEntriesTemp.setAmount(x.getAmount());
                claimLedgerChargesEntriesTemp.setStartBalance(x.getStartBalance());
                claimLedgerChargesEntriesTemp.setAllowed(x.getAllowed());
                claimLedgerChargesEntriesTemp.setPaid(x.getPaid());
                claimLedgerChargesEntriesTemp.setRemarks(x.getRemarks());
                claimLedgerChargesEntriesTemp.setAdjReasons(x.getAdjReasons());
                claimLedgerChargesEntriesTemp.setAdjusted(x.getAdjusted());
                claimLedgerChargesEntriesTemp.setUnpaidReasons(x.getUnpaidReasons());
                claimLedgerChargesEntriesTemp.setUnpaid(x.getUnpaid());
                claimLedgerChargesEntriesTemp.setDeductible(x.getDeductible());
                claimLedgerChargesEntriesTemp.setStatus(x.getStatus());
                claimLedgerChargesEntriesTemp.setOtherCredits(x.getOtherCredits());
                claimLedgerChargesEntriesTemp.setEndBalance(x.getEndBalance());
                claimLedgerChargesEntriesTemp.setCreatedAt(x.getCreatedAt());
                claimLedgerChargesEntriesTemp.setCreatedBy(x.getCreatedBy());
                claimLedgerChargesEntriesTemp.setUserIp(x.getUserIp());
                claimLedgerChargesEntriesTemp.setPayment(x.getPayment());
                claimLedgerChargesEntriesTemp.setAdjustment(x.getAdjustment());
                claimLedgerChargesEntriesTemp.setBalance(x.getBalance());
                claimLedgerChargesEntriesTemp.setTransactionIdx(x.getTransactionIdx());
                claimLedgerChargesEntriesTemp.setSequestrationAmt(x.getSequestrationAmt());
                claimLedgerChargesEntriesTemp.setUpdatedAt(x.getUpdatedAt());
                claimLedgerChargesEntriesTemp.setUpdatedBy(x.getUpdatedBy());
                claimLedgerChargesEntriesTemp.setDeleted(x.getDeleted());
            });

            ClaimLedgerChargesEntries claimLedgerChargesEntries = dtoToChargesWRTClaim(chargesWRTClaims);
            claimLedgerChargesEntriesRepository.save(claimLedgerChargesEntries);
        }
//        else if(3==flag){//updating new payments in temp table
//
//
//        }else{//updating existing payment in org table
//
//
//        }


        return null;
    }

    private ClaimLedgerChargesEntries dtoToChargesWRTClaim(ChargesWRTClaimDto chargeWRTClaimDto) {
        return modelMapper.map(chargeWRTClaimDto, ClaimLedgerChargesEntries.class);
    }
}
