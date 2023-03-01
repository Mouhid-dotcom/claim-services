package rovermd.project.claimservices.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.dto.APIResponse;
import rovermd.project.claimservices.dto.insurancePaymentPosting.*;
import rovermd.project.claimservices.entity.claimMaster.ClaimAudittrail;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntries;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntriesTemp;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerEntries;
import rovermd.project.claimservices.entity.paymentPosting.EobMaster;
import rovermd.project.claimservices.repos.claimMaster.ClaiminfomasterRepository;
import rovermd.project.claimservices.repos.paymentPosting.ClaimLedgerChargesEntriesRepository;
import rovermd.project.claimservices.repos.paymentPosting.ClaimLedgerChargesEntriesTempRepository;
import rovermd.project.claimservices.repos.paymentPosting.ClaimLedgerEntriesRepository;
import rovermd.project.claimservices.repos.paymentPosting.EobMasterRepository;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimPaymentPostingService;
import rovermd.project.claimservices.service.ExternalService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClaimPaymentPostingServiceImpl implements ClaimPaymentPostingService {

    @Autowired
    private ClaiminfomasterRepository claiminfomasterRepository;

    @Autowired
    private EobMasterRepository eobMasterRepository;

    @Autowired
    private ClaimLedgerEntriesRepository claimLedgerEntriesRepository;
    @Autowired
    private ClaimLedgerChargesEntriesTempRepository claimLedgerChargesEntriesTempRepository;

    @Autowired
    private ClaimLedgerChargesEntriesRepository claimLedgerChargesEntriesRepository;

    @Autowired
    private ClaimAudittrailService claimAudittrailService;

    @Autowired
    private ExternalService externalService;

    @Transactional
    @Override
    public Object setCharges_WRT_Claim(Integer flag, ChargesWRTClaimDto chargesWRTClaims) {

        try {
            if (1 == flag) {//inserting new payments temp table
                chargesWRTClaims.getCharges().forEach(x -> {
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
                    claimLedgerChargesEntriesTemp.setTransactionIdx(x.getTransactionIdx());
                    claimLedgerChargesEntriesTemp.setSequestrationAmt(x.getSequestrationAmt());
                    claimLedgerChargesEntriesTemp.setAction(chargesWRTClaims.getAction());
                    claimLedgerChargesEntriesTemp.setTcn(chargesWRTClaims.getTcn());
                    claimLedgerChargesEntriesTemp.setClaimControlNo(chargesWRTClaims.getClaimControlNo());
                    claimLedgerChargesEntriesTempRepository.save(claimLedgerChargesEntriesTemp);
                });

                return new APIResponse<>(200,
                        "Success",
                        getClaims_WRT_Check(new ClaimIdAndTransactionId(chargesWRTClaims.getCharges().get(0).getClaimIdx(), chargesWRTClaims.getCharges().get(0).getTransactionIdx()),1));

            } else if (2 == flag) {//updating payments in temp table

                chargesWRTClaims.getCharges().forEach(x -> {
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
                    claimLedgerChargesEntriesTemp.setTransactionIdx(x.getTransactionIdx());
                    claimLedgerChargesEntriesTemp.setSequestrationAmt(x.getSequestrationAmt());
                    claimLedgerChargesEntriesTemp.setAction(chargesWRTClaims.getAction());
                    claimLedgerChargesEntriesTemp.setTcn(chargesWRTClaims.getTcn());
                    claimLedgerChargesEntriesTemp.setClaimControlNo(chargesWRTClaims.getClaimControlNo());
                    claimLedgerChargesEntriesTempRepository.save(claimLedgerChargesEntriesTemp);
                });

                return new APIResponse<>(200,
                        "Success",
                        getClaims_WRT_Check(new ClaimIdAndTransactionId(chargesWRTClaims.getCharges().get(0).getClaimIdx(), chargesWRTClaims.getCharges().get(0).getTransactionIdx()),1));
            } else {//updating existing payment in org table
                //**************to be continue *******************
                return new APIResponse<>(200,
                        "Success",
                        getClaims_WRT_Check(new ClaimIdAndTransactionId(chargesWRTClaims.getCharges().get(0).getClaimIdx(), chargesWRTClaims.getCharges().get(0).getTransactionIdx()),0));
            }
        } catch (Exception e) {
            //send or log e
            return new APIResponse<>(500, "something went wrong", null);
        }

        //claimLedgerChargesEntriesTempRepository.findAllByClaimIdx(chargesWRTClaims.getCharges().get(0).getClaimIdx()));
    }

    @Transactional
    @Override
    public Object setClaims_WRT_Checks(Integer flag, ClaimsWRTCheckDto claimsWRTCheckDto) {

        try {
            final int[] ClaimType = new int[1];
            Double TOTAL_wrt_Claim_Paid = 0.0;
            Double TOTAL_wrt_Claim_Adjusted = 0.0;
            Double TOTAL_Charges_wrt_Claim = 0.0;
            Double TOTAL_wrt_Claim_Balance = 0.0;


            EobMaster eobMaster = new EobMaster();
            eobMaster.setAppliedAmount(claimsWRTCheckDto.getInsurancePayment().getAppliedAmount());
            eobMaster.setUnappliedAmount(claimsWRTCheckDto.getInsurancePayment().getUnAppliedAmount());
            eobMaster.setPaymentAmount(claimsWRTCheckDto.getInsurancePayment().getAmount());
            eobMaster.setReceivedDate(claimsWRTCheckDto.getInsurancePayment().getRecievedDate());
            eobMaster.setCheckNumber(claimsWRTCheckDto.getInsurancePayment().getCheckNo());
            eobMaster.setInsuranceIdx(claimsWRTCheckDto.getInsurancePayment().getInsuranceId());
            eobMaster.setOtherRefrenceNo(claimsWRTCheckDto.getInsurancePayment().getOtherRefNo());
            EobMaster save = eobMasterRepository.save(eobMaster);

            for (int i = 0; i < claimsWRTCheckDto.getClaims().size(); i++) {
                ClaimLedgerEntries claimLedgerEntries = new ClaimLedgerEntries();
                claimLedgerEntries.setTransactionIdx(save.getId());
                claimLedgerEntries.setClaimNumber(claimsWRTCheckDto.getClaims().get(i).getClaimNo());
                claimLedgerEntries.setAmount(claimsWRTCheckDto.getClaims().get(i).getBilled());
                claimLedgerEntries.setTransactionType("D");
                claimLedgerEntriesRepository.save(claimLedgerEntries);

                List<Map<Object, Object>> allByClaimIdAndTransactionId = claimLedgerChargesEntriesTempRepository.findAllByClaimIdAndTransactionIdx(claimsWRTCheckDto.getClaims().get(i).getClaimNo(),
                        claimsWRTCheckDto.getInsurancePayment().getCheckNo());

                for (Map<Object, Object> x : allByClaimIdAndTransactionId) {
                    System.out.println("ChargeIdx -> " + x.get("ChargeIdx"));
                    ClaimLedgerChargesEntries claimLedgerChargesEntries = new ClaimLedgerChargesEntries();

                    claimLedgerChargesEntries.setClaimNumber(String.valueOf(x.get("ClaimNumber")));
                    claimLedgerChargesEntries.setClaimIdx(String.valueOf(x.get("ClaimIdx")));
                    claimLedgerChargesEntries.setChargeIdx(String.valueOf(x.get("ChargeIdx")));
                    claimLedgerChargesEntries.setCharges(String.valueOf(x.get("Charges")));
                    claimLedgerChargesEntries.setAmount(String.valueOf(x.get("Amount")));
                    claimLedgerChargesEntries.setStartBalance(String.valueOf(x.get("StartBalance")));
                    claimLedgerChargesEntries.setAllowed(String.valueOf(x.get("Allowed")));
                    claimLedgerChargesEntries.setPaid(String.valueOf(x.get("Paid")));
                    claimLedgerChargesEntries.setRemarks(String.valueOf(x.get("Remarks")));
                    claimLedgerChargesEntries.setAdjReasons(String.valueOf(x.get("AdjReasons")));
                    claimLedgerChargesEntries.setAdjusted(String.valueOf(x.get("Adjusted")));
                    claimLedgerChargesEntries.setSequestrationAmt(String.valueOf(x.get("SequestrationAmt")));
                    claimLedgerChargesEntries.setUnpaidReasons(String.valueOf(x.get("UnpaidReasons")));
                    claimLedgerChargesEntries.setUnpaid(String.valueOf(x.get("Unpaid")));
                    claimLedgerChargesEntries.setDeductible(String.valueOf(x.get("Deductible")));
                    claimLedgerChargesEntries.setStatus(String.valueOf(x.get("Status")));
                    claimLedgerChargesEntries.setOtherCredits(String.valueOf(x.get("OtherCredits")));
                    claimLedgerChargesEntries.setEndBalance(String.valueOf(x.get("EndBalance")));
                    claimLedgerChargesEntries.setTransactionIdx(String.valueOf(save.getId()));
                    claimLedgerChargesEntries.setTransactionType(String.valueOf(x.get("TransactionType")));
                    claimLedgerChargesEntries.setPayment(String.valueOf(x.get("Payment")));
                    claimLedgerChargesEntries.setAdjustment(String.valueOf(x.get("Adjustment")));
                    claimLedgerChargesEntries.setBalance(String.valueOf(x.get("Balance")));
                    claimLedgerChargesEntriesRepository.save(claimLedgerChargesEntries);


                    if (String.valueOf(x.get("ClaimNumber")).split("-")[0].equals("CP"))
                        ClaimType[0] = 2;
                    else
                        ClaimType[0] = 1;


                    claimLedgerChargesEntriesRepository.updateClaim_Ledger_Charges_entries(String.valueOf(x.get("EndBalance")), String.valueOf(save.getId()),
                            String.valueOf(x.get("ChargeIdx")), String.valueOf(x.get("ClaimIdx")));

                    insertClaimAuditTrails(String.valueOf(x.get("ClaimNumber")),
                            "Payment Posted Against " + x.get("Charges") + " in Cheque No : " + save.getCheckNumber(),
                            "PAYMENT POSTED", ClaimType[0], "MOUHID", 39, "MOUHID_IP");

                    Map<Object, Object> sumOfPaidAndAdjusted = claimLedgerChargesEntriesRepository.getSumOfPaidAndAdjusted(String.valueOf(x.get("ClaimNumber")), String.valueOf(x.get("ChargeIdx")));
//
                    TOTAL_wrt_Claim_Paid += (Double) sumOfPaidAndAdjusted.get("Paid");
                    TOTAL_wrt_Claim_Adjusted += (Double) sumOfPaidAndAdjusted.get("Adjusted");
//

                    insertClaimAuditTrails(String.valueOf(x.get("ClaimNumber")),
                            "Charge Status Updated Against " + x.get("Charges") + " in Cheque No : " + save.getCheckNumber(),
                            "CHARGE STATUS UPDATED FROM PAYMENT POSTING", ClaimType[0], "MOUHID", 39, "MOUHID_IP");

                }


                //getting Total Amount of Claim
                TOTAL_Charges_wrt_Claim = claiminfomasterRepository.getTotalCharges(claimsWRTCheckDto.getClaims().get(i).getClaimNo());


                //Calculating Balance w.r.t Claim
                TOTAL_wrt_Claim_Balance = TOTAL_Charges_wrt_Claim - (TOTAL_wrt_Claim_Paid + TOTAL_wrt_Claim_Adjusted);

                //Updating Claim's Total Paid , Adjusted , Balance
                claiminfomasterRepository.updatePaidAndAdjustedAndBalance(TOTAL_wrt_Claim_Paid, TOTAL_wrt_Claim_Adjusted, TOTAL_wrt_Claim_Balance, claimsWRTCheckDto.getClaims().get(i).getClaimNo());

                TOTAL_wrt_Claim_Paid = 0.0;
                TOTAL_wrt_Claim_Adjusted = 0.0;
                TOTAL_wrt_Claim_Balance = 0.0;
                TOTAL_Charges_wrt_Claim = 0.0;
            }

            for (int i = 0; i < claimsWRTCheckDto.getClaims().size(); i++) {
                claimLedgerChargesEntriesTempRepository.deleteByClaimNumberAndTransactionIdx(claimsWRTCheckDto.getClaims().get(i).getClaimNo(), save.getCheckNumber());
            }

        } catch (Exception e) {
            //send or log e
            throw e;
//            return new APIResponse<>(500, "something went wrong", null);
        }

        return new APIResponse<>(200, "Success", null);//claimLedgerChargesEntriesTempRepository.findAllByClaimIdx(chargesWRTClaims.getCharges().get(0).getClaimIdx()));
    }

    @Override
    public Object getCharges_WRT_Claim(ClaimIdAndTransactionId claimIdAndTransactionId, Integer flag) {
        if (1 == flag)//For New Payment Posting
            return claimLedgerChargesEntriesRepository.findAllByClaimIdxTransactionTypeCr(claimIdAndTransactionId.getClaimId());
        else if (2 == flag)//For payments in temp table
            return claimLedgerChargesEntriesTempRepository.findAllByClaimIdxAndTransactionIdxTransactionTypeD(claimIdAndTransactionId.getClaimId(),
                    claimIdAndTransactionId.getTransactionId());
        else if (3 == flag)//For payments in org table
            return claimLedgerChargesEntriesRepository.findAllByClaimIdxAndTransactionIdx(claimIdAndTransactionId.getClaimId(),
                    claimIdAndTransactionId.getTransactionId());
        return null;
    }

    @Override
    public Object getClaims_WRT_Check(ClaimIdAndTransactionId claimIdAndTransactionId, Integer flag) {
        ClaimsWRTCheckDto claimsWRTCheckDto = new ClaimsWRTCheckDto();
        InsurancePaymentDto insurancePaymentDto = new InsurancePaymentDto();

        List<ClaimWRTCheckDto> claimWRTCheckDtoList = new ArrayList<>();
        List<String> claimNumbers = flag == 1 ? claimLedgerChargesEntriesTempRepository.selectDistinctClaimNumbers(claimIdAndTransactionId.getTransactionId()): claimLedgerChargesEntriesRepository.selectDistinctClaimNumbers(Integer.valueOf(claimIdAndTransactionId.getTransactionId()));
        Map<Object, Object> eobMasterDetails = eobMasterRepository.getEOBMasterDetails(Integer.valueOf(claimIdAndTransactionId.getTransactionId()));

        claimNumbers.forEach(claimNumber -> {
            List<Map<Object, Object>> claimDetails = claiminfomasterRepository.getClaimDetails(claimNumber);
            List<Map<Object, Object>> claimLedgerChargesEntriesDetails = flag == 1 ? claimLedgerChargesEntriesTempRepository.getClaimLedgerChargesEntriesDetails(claimNumber, claimIdAndTransactionId.getTransactionId()) : claimLedgerChargesEntriesRepository.getClaimLedgerChargesEntriesDetails(claimNumber, claimIdAndTransactionId.getTransactionId());

            for (int i = 0; i < claimDetails.size(); i++) {
                ClaimWRTCheckDto claimWRTCheckDto = new ClaimWRTCheckDto();
                claimWRTCheckDto.setPatientName(String.valueOf(claimDetails.get(i).get("PatientName")));
                claimWRTCheckDto.setAcctNo(String.valueOf(claimDetails.get(i).get("AcctNo")));
                claimWRTCheckDto.setClaimNo(claimNumber);
                claimWRTCheckDto.setPcn(String.valueOf(claimDetails.get(i).get("PCN")));
                claimWRTCheckDto.setDos(String.valueOf(claimDetails.get(i).get("DOS")));
                claimWRTCheckDto.setBilled(String.valueOf(claimLedgerChargesEntriesDetails.get(i).get("Amount")));
                claimWRTCheckDto.setAllowed(String.valueOf(claimLedgerChargesEntriesDetails.get(i).get("Allowed")));
                claimWRTCheckDto.setPaid(String.valueOf(claimLedgerChargesEntriesDetails.get(i).get("Paid")));
                claimWRTCheckDto.setAdjusted(String.valueOf(claimLedgerChargesEntriesDetails.get(i).get("Adjusted")));
                claimWRTCheckDto.setUnpaid(String.valueOf(claimLedgerChargesEntriesDetails.get(i).get("Unpaid")));
                claimWRTCheckDto.setAdditionalActions(String.valueOf(claimLedgerChargesEntriesDetails.get(i).get("AdditionalActions")));
                claimWRTCheckDto.setBalance(String.valueOf(claimLedgerChargesEntriesDetails.get(i).get("EndBalance")));
                claimWRTCheckDtoList.add(claimWRTCheckDto);
            }
        });

        insurancePaymentDto.setRecievedDate(String.valueOf(eobMasterDetails.get("RecievedDate")));
        insurancePaymentDto.setAmount(String.valueOf(eobMasterDetails.get("PaymentAmount")));
        insurancePaymentDto.setAppliedAmount(String.valueOf(eobMasterDetails.get("AppliedAmount")));
        insurancePaymentDto.setUnAppliedAmount(String.valueOf(eobMasterDetails.get("UnappliedAmount")));
        insurancePaymentDto.setOtherRefNo(String.valueOf(eobMasterDetails.get("OtherRefrenceNo")));
        insurancePaymentDto.setInsuranceId((Integer) eobMasterDetails.get("InsuranceIdx"));
        insurancePaymentDto.setInsuranceName(externalService.getInsuranceDetailsById(String.valueOf(eobMasterDetails.get("InsuranceIdx"))).getPayerName());

        claimsWRTCheckDto.setInsurancePayment(insurancePaymentDto);
        claimsWRTCheckDto.setClaims(claimWRTCheckDtoList);


        return claimsWRTCheckDto;
    }

    private void insertClaimAuditTrails(String claimNumber, String description, String action,
                                        int ClaimType, String UserId, int ClientId, String ClientIP) {
        claimAudittrailService.createAuditTrail(ClaimAudittrail.builder()
                .claimNo(claimNumber)
                .claimType(String.valueOf(ClaimType))
                .userID(UserId)
                .userIP(ClientIP)
                .clientID(String.valueOf(ClientId))
                .userIP(ClientIP)
                .action(action)
                .ruleText(description).build());
    }

}
