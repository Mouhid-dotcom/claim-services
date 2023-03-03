package rovermd.project.claimservices.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.dto.APIResponse;
import rovermd.project.claimservices.dto.insurancePaymentPosting.*;
import rovermd.project.claimservices.entity.claimMaster.ClaimAudittrail;
import rovermd.project.claimservices.entity.paymentPosting.*;
import rovermd.project.claimservices.repos.claimMaster.ClaimchargesinfoRepository;
import rovermd.project.claimservices.repos.claimMaster.ClaiminfomasterRepository;
import rovermd.project.claimservices.repos.paymentPosting.*;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimPaymentPostingService;
import rovermd.project.claimservices.service.ExternalService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static rovermd.project.claimservices.util.UtilityHelper.isEmpty;

@Service
public class ClaimPaymentPostingServiceImpl implements ClaimPaymentPostingService {

    @Autowired
    private ClaiminfomasterRepository claiminfomasterRepository;

    @Autowired
    private ClaimchargesinfoRepository claimchargesinfoRepository;

    @Autowired
    private EobMasterRepository eobMasterRepository;

    @Autowired
    private EobMasterHistoryRepository eobMasterHistoryRepository;

    @Autowired
    private ClaimLedgerEntriesRepository claimLedgerEntriesRepository;
    @Autowired
    private ClaimLedgerChargesEntriesTempRepository claimLedgerChargesEntriesTempRepository;

    @Autowired
    private ClaimLedgerChargesEntriesRepository claimLedgerChargesEntriesRepository;

    @Autowired
    private ClaimLedgerChargesEntriesHistoryRepository claimLedgerChargesEntriesHistoryRepository;

    @Autowired
    private ClaimAudittrailService claimAudittrailService;

    @Autowired
    private ExternalService externalService;

    @Transactional
    @Override
    public Object setCharges_WRT_Claim(Integer flag, ChargesWRTClaimDto chargesWRTClaims) {
        Double TOTAL_wrt_Claim_Paid = 0.0;
        Double TOTAL_wrt_Claim_Adjusted = 0.0;
        Double TOTAL_Charges_wrt_Claim = 0.0;
        Double TOTAL_wrt_Claim_Balance = 0.0;

        try {
            if (1 == flag) {//inserting & updating new payments temp table
                chargesWRTClaims.getCharges().forEach(x -> {
                    ClaimLedgerChargesEntriesTemp claimLedgerChargesEntriesTemp = new ClaimLedgerChargesEntriesTemp();
                    if (!isEmpty(x.getLedgerIdx())) {
                        claimLedgerChargesEntriesTemp.setId(x.getLedgerIdx());
                    }
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
                        getClaims_WRT_Check(new ClaimIdAndTransactionId(chargesWRTClaims.getCharges().get(0).getClaimIdx(),
                                chargesWRTClaims.getCharges().get(0).getTransactionIdx()), 1));

            } else {//updating existing payment in org table
                BigDecimal Existing_Total_Paid = new BigDecimal(0);
                BigDecimal AppliedAmount;
                BigDecimal UnappliedAmount;

                List<Map<String, String>> claimLedgerChargesEntriesExisting = claimLedgerChargesEntriesRepository.getClaimLedgerChargesEntries(chargesWRTClaims.getCharges().get(0).getClaimNo(),
                        chargesWRTClaims.getCharges().get(0).getTransactionIdx());

                for (Map<String, String> x : claimLedgerChargesEntriesExisting) {

                    ClaimLedgerChargesEntriesHistory claimLedgerChargesEntriesHistory = new ClaimLedgerChargesEntriesHistory();
                    claimLedgerChargesEntriesHistory.setCharges(String.valueOf(x.get("Charges")));
                    claimLedgerChargesEntriesHistory.setClaimNumber(String.valueOf(x.get("ClaimNo")));
                    claimLedgerChargesEntriesHistory.setTransactionType("D");
                    claimLedgerChargesEntriesHistory.setClaimIdx(x.get("ClaimIdx"));
                    claimLedgerChargesEntriesHistory.setChargeIdx(x.get("ChargeIdx"));
                    claimLedgerChargesEntriesHistory.setCharges(x.get("Charges"));
                    claimLedgerChargesEntriesHistory.setAmount(x.get("Amount"));
                    claimLedgerChargesEntriesHistory.setStartBalance(x.get("StartBalance"));
                    claimLedgerChargesEntriesHistory.setAllowed(x.get("Allowed"));
                    claimLedgerChargesEntriesHistory.setPaid(x.get("Paid"));
                    claimLedgerChargesEntriesHistory.setRemarks(x.get("Remarks"));
                    claimLedgerChargesEntriesHistory.setAdjReasons(x.get("AdjReasons"));
                    claimLedgerChargesEntriesHistory.setAdjusted(x.get("Adjusted"));
                    claimLedgerChargesEntriesHistory.setUnpaidReasons(x.get("UnpaidReasons"));
                    claimLedgerChargesEntriesHistory.setUnpaid(x.get("Unpaid"));
                    claimLedgerChargesEntriesHistory.setDeductible(x.get("Deductible"));
                    claimLedgerChargesEntriesHistory.setStatus(x.get("Status"));
                    claimLedgerChargesEntriesHistory.setOtherCredits(x.get("OtherCredits"));
                    claimLedgerChargesEntriesHistory.setEndBalance(x.get("EndBalance"));
                    claimLedgerChargesEntriesHistory.setTransactionIdx(x.get("TransactionIdx"));
                    claimLedgerChargesEntriesHistory.setSequestrationAmt(x.get("SequestrationAmt"));
                    claimLedgerChargesEntriesHistory.setAction(x.get("Action"));
                    claimLedgerChargesEntriesHistory.setTcn(x.get("Tcn"));
                    claimLedgerChargesEntriesHistory.setClaimControlNo(chargesWRTClaims.getClaimControlNo());
                    claimLedgerChargesEntriesHistoryRepository.save(claimLedgerChargesEntriesHistory);

                    Existing_Total_Paid = Existing_Total_Paid.add(new BigDecimal(x.get("Paid")));
                }

                BigDecimal NEW_Total_Paid = new BigDecimal(0);
                Integer ClaimType;
                for (ChargeWRTClaimDto x : chargesWRTClaims.getCharges()) {
                    ClaimLedgerChargesEntries claimLedgerChargesEntries = new ClaimLedgerChargesEntries();
                    if (!isEmpty(x.getLedgerIdx())) {
                        claimLedgerChargesEntries.setId(x.getLedgerIdx());
                    }
                    claimLedgerChargesEntries.setCharges(x.getCharges());
                    claimLedgerChargesEntries.setClaimNumber(x.getClaimNo());
                    claimLedgerChargesEntries.setTransactionType("D");
                    claimLedgerChargesEntries.setClaimIdx(x.getClaimIdx());
                    claimLedgerChargesEntries.setChargeIdx(x.getChargeIdx());
                    claimLedgerChargesEntries.setCharges(x.getCharges());
                    claimLedgerChargesEntries.setAmount(x.getAmount());
                    claimLedgerChargesEntries.setStartBalance(x.getStartBalance());
                    claimLedgerChargesEntries.setAllowed(x.getAllowed());
                    claimLedgerChargesEntries.setPaid(x.getPaid());
                    claimLedgerChargesEntries.setRemarks(x.getRemarks());
                    claimLedgerChargesEntries.setAdjReasons(x.getAdjReasons());
                    claimLedgerChargesEntries.setAdjusted(x.getAdjusted());
                    claimLedgerChargesEntries.setUnpaidReasons(x.getUnpaidReasons());
                    claimLedgerChargesEntries.setUnpaid(x.getUnpaid());
                    claimLedgerChargesEntries.setDeductible(x.getDeductible());
                    claimLedgerChargesEntries.setStatus(x.getStatus());
                    claimLedgerChargesEntries.setOtherCredits(x.getOtherCredits());
                    claimLedgerChargesEntries.setEndBalance(x.getEndBalance());
                    claimLedgerChargesEntries.setTransactionIdx(x.getTransactionIdx());
                    claimLedgerChargesEntries.setSequestrationAmt(x.getSequestrationAmt());
                    claimLedgerChargesEntries.setAction(chargesWRTClaims.getAction());
                    claimLedgerChargesEntries.setTcn(chargesWRTClaims.getTcn());
                    claimLedgerChargesEntries.setClaimControlNo(chargesWRTClaims.getClaimControlNo());
                    ClaimLedgerChargesEntries save = claimLedgerChargesEntriesRepository.save(claimLedgerChargesEntries);

                    NEW_Total_Paid = NEW_Total_Paid.add(x.getPaid());


                    if (x.getClaimNo().split("-")[0].equals("CP"))
                        ClaimType = 2;
                    else
                        ClaimType = 1;

                    insertClaimAuditTrails(x.getClaimNo(),
                            "Payment Updated Against " + x.getCharges() + " in Cheque No : " + chargesWRTClaims.getCheckNo(),
                            "PAYMENT UPDATED", ClaimType, "MOUHID", 39, "MOUHID_IP");


                    claimchargesinfoRepository.updateStatusWrtStatus(x.getStatus(), "MOUHID", x.getChargeIdx());

                    insertClaimAuditTrails(String.valueOf(x.getClaimNo()),
                            "Charge Status Updated Against " + x.getCharges() + " in Cheque No : " + chargesWRTClaims.getCheckNo(),
                            "CHARGE STATUS UPDATED FROM PAYMENT POSTING", ClaimType, "MOUHID", 39, "MOUHID_IP");

                    Map<String, Double> sumOfPaidAndAdjusted = claimLedgerChargesEntriesRepository.getSumOfPaidAndAdjusted(x.getClaimNo(), x.getChargeIdx());

                    //getting Sum of Paid , Sum of Adjusted w.r.t Claim from Ledger
                    TOTAL_wrt_Claim_Paid += sumOfPaidAndAdjusted.get("Paid");
                    TOTAL_wrt_Claim_Adjusted += sumOfPaidAndAdjusted.get("Adjusted");

                }

                //getting Total Amount of Claim
                TOTAL_Charges_wrt_Claim = claiminfomasterRepository.getTotalCharges(chargesWRTClaims.getCharges().get(0).getClaimNo());

                //Calculating Balance w.r.t Claim
                TOTAL_wrt_Claim_Balance = TOTAL_Charges_wrt_Claim - (TOTAL_wrt_Claim_Paid + TOTAL_wrt_Claim_Adjusted);

                //Updating Claim's Total Paid , Adjusted , Balance
                claiminfomasterRepository.updatePaidAndAdjustedAndBalance(TOTAL_wrt_Claim_Paid, TOTAL_wrt_Claim_Adjusted, TOTAL_wrt_Claim_Balance, chargesWRTClaims.getCharges().get(0).getClaimNo());


                Map<String, String> eobMasterDetails = eobMasterRepository.getEOBMasterDetails(Integer.valueOf(chargesWRTClaims.getCharges().get(0).getTransactionIdx()));
                EobMasterHistory eobMasterHistory = new EobMasterHistory();
                System.out.println(eobMasterDetails.get("PatientIdx"));
                eobMasterHistory.setPatientIdx(Integer.valueOf(eobMasterDetails.get("PatientIdx")));
                eobMasterHistory.setPaymentAmount(eobMasterDetails.get("PaymentAmount"));
                eobMasterHistory.setReceivedDate(eobMasterDetails.get("ReceivedDate"));
                eobMasterHistory.setCheckNumber(eobMasterDetails.get("CheckNumber"));
                eobMasterHistory.setPaymentType(eobMasterDetails.get("PaymentType"));
                eobMasterHistory.setPaymentSource(eobMasterDetails.get("PaymentSource"));
                eobMasterHistory.setMemo(eobMasterDetails.get("Memo"));
                eobMasterHistory.setStatus(Integer.valueOf(eobMasterDetails.get("Status")));
                eobMasterHistory.setCopayDos(eobMasterDetails.get("CopayDOS"));
                eobMasterHistory.setCardType(eobMasterDetails.get("CardType"));
                eobMasterHistory.setInsuranceIdx(Integer.valueOf(eobMasterDetails.get("InsuranceIdx")));
                eobMasterHistory.setOtherRefrenceNo(eobMasterDetails.get("OtherRefrenceNo"));
                eobMasterHistory.setPaymentFrom(eobMasterDetails.get("PaymentFrom"));
                eobMasterHistory.setIsPaymentOnly(Integer.valueOf(eobMasterDetails.get("isPaymentOnly")));
                eobMasterHistory.setIsCreditAccount(Integer.valueOf(eobMasterDetails.get("isCreditAccount")));
                eobMasterHistory.setAppliedAmount(eobMasterDetails.get("AppliedAmount"));
                eobMasterHistory.setUnappliedAmount(eobMasterDetails.get("UnappliedAmount"));
                eobMasterHistory.setId(Integer.valueOf(chargesWRTClaims.getCharges().get(0).getTransactionIdx()));
                eobMasterHistoryRepository.save(eobMasterHistory);


                AppliedAmount = (NEW_Total_Paid.subtract(Existing_Total_Paid));
                UnappliedAmount = new BigDecimal(eobMasterDetails.get("PaymentAmount")).subtract(AppliedAmount);

                eobMasterRepository.updateAppliedAmountAndUnappliedAmount(AppliedAmount, UnappliedAmount,
                        Integer.valueOf(chargesWRTClaims.getCharges().get(0).getTransactionIdx()));


                return new APIResponse<>(200,
                        "Success",
                        getClaims_WRT_Check(new ClaimIdAndTransactionId(chargesWRTClaims.getCharges().get(0).getClaimIdx(),
                                chargesWRTClaims.getCharges().get(0).getTransactionIdx()), 0));
            }
        } catch (Exception e) {
            //send or log e
            throw e;
//            return new APIResponse<>(500, "something went wrong", null);
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

                List<Map<String, String>> allByClaimIdAndTransactionId = claimLedgerChargesEntriesTempRepository.findAllByClaimIdAndTransactionIdx(claimsWRTCheckDto.getClaims().get(i).getClaimNo(),
                        claimsWRTCheckDto.getInsurancePayment().getCheckNo());

                for (Map<String, String> x : allByClaimIdAndTransactionId) {
                    System.out.println("ChargeIdx -> " + x.get("ChargeIdx"));
                    ClaimLedgerChargesEntries claimLedgerChargesEntries = new ClaimLedgerChargesEntries();

                    claimLedgerChargesEntries.setClaimNumber(String.valueOf(x.get("ClaimNumber")));
                    claimLedgerChargesEntries.setClaimIdx(Integer.valueOf(x.get("ClaimIdx")));
                    claimLedgerChargesEntries.setChargeIdx(Integer.valueOf(x.get("ChargeIdx")));
                    claimLedgerChargesEntries.setCharges(x.get("Charges"));
                    claimLedgerChargesEntries.setAmount(new BigDecimal(x.get("Amount")));
                    claimLedgerChargesEntries.setStartBalance(new BigDecimal(x.get("StartBalance")));
                    claimLedgerChargesEntries.setAllowed(new BigDecimal(x.get("Allowed")));
                    claimLedgerChargesEntries.setPaid(new BigDecimal(x.get("Paid")));
                    claimLedgerChargesEntries.setRemarks(String.valueOf(x.get("Remarks")));
                    claimLedgerChargesEntries.setAdjReasons(String.valueOf(x.get("AdjReasons")));
                    claimLedgerChargesEntries.setAdjusted(new BigDecimal(x.get("Adjusted")));
                    claimLedgerChargesEntries.setSequestrationAmt(new BigDecimal(x.get("SequestrationAmt")));
                    claimLedgerChargesEntries.setUnpaidReasons(String.valueOf(x.get("UnpaidReasons")));
                    claimLedgerChargesEntries.setUnpaid(new BigDecimal(x.get("Unpaid")));
                    claimLedgerChargesEntries.setDeductible(new BigDecimal(x.get("Deductible")));
                    claimLedgerChargesEntries.setStatus(Integer.valueOf(x.get("Status")));
                    claimLedgerChargesEntries.setOtherCredits(new BigDecimal(x.get("OtherCredits")));
                    claimLedgerChargesEntries.setEndBalance(new BigDecimal(x.get("EndBalance")));
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

                    Map<String, Double> sumOfPaidAndAdjusted = claimLedgerChargesEntriesRepository.getSumOfPaidAndAdjusted(String.valueOf(x.get("ClaimNumber")), Integer.valueOf(x.get("ChargeIdx")));

                    //getting Sum of Paid , Sum of Adjusted w.r.t Claim from Ledger
                    TOTAL_wrt_Claim_Paid += sumOfPaidAndAdjusted.get("Paid");
                    TOTAL_wrt_Claim_Adjusted += sumOfPaidAndAdjusted.get("Adjusted");


                    claimchargesinfoRepository.updateStatusWrtStatus(Integer.valueOf(x.get("Status")), "MOUHID", Integer.valueOf(x.get("ChargeIdx")));

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
        if (2 == flag)//For payments in temp table
            return claimLedgerChargesEntriesTempRepository.findAllByClaimIdxAndTransactionIdxTransactionTypeD(claimIdAndTransactionId.getClaimId(),
                    claimIdAndTransactionId.getTransactionId());
        //For payments in org table
        return claimLedgerChargesEntriesRepository.findAllByClaimIdxAndTransactionIdx(claimIdAndTransactionId.getClaimId(),
                claimIdAndTransactionId.getTransactionId());
    }

    @Override
    public Object getClaims_WRT_Check(ClaimIdAndTransactionId claimIdAndTransactionId, Integer flag) {
        ClaimsWRTCheckDto claimsWRTCheckDto = new ClaimsWRTCheckDto();
        InsurancePaymentDto insurancePaymentDto = new InsurancePaymentDto();

        List<ClaimWRTCheckDto> claimWRTCheckDtoList = new ArrayList<>();
        List<String> claimNumbers = flag == 1 ? claimLedgerChargesEntriesTempRepository.selectDistinctClaimNumbers(claimIdAndTransactionId.getTransactionId()) : claimLedgerChargesEntriesRepository.selectDistinctClaimNumbers(Integer.valueOf(claimIdAndTransactionId.getTransactionId()));
        Map<String, Object> eobMasterDetails = eobMasterRepository.getEOBMaster(Integer.valueOf(claimIdAndTransactionId.getTransactionId()));

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
        insurancePaymentDto.setAppliedAmount(new BigDecimal((String) eobMasterDetails.get("AppliedAmount")));
        insurancePaymentDto.setUnAppliedAmount(new BigDecimal((String) eobMasterDetails.get("UnappliedAmount")));
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
