package rovermd.project.claimservices.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.dto.*;
import rovermd.project.claimservices.entity.Claimchargesinfo;
import rovermd.project.claimservices.entity.Claiminfomaster;
import rovermd.project.claimservices.exception.ResourceNotFoundException;
import rovermd.project.claimservices.repos.CPTRepository;
import rovermd.project.claimservices.repos.ClaiminfomasterRepository;
import rovermd.project.claimservices.repos.ICDRepository;
import rovermd.project.claimservices.repos.scrubber.AnesthesiacodeRepository;
import rovermd.project.claimservices.service.ClaimServiceEDI;
import rovermd.project.claimservices.service.ExternalService;
import rovermd.project.claimservices.util.x12.Context;
import rovermd.project.claimservices.util.x12.Loop;
import rovermd.project.claimservices.util.x12.Segment;
import rovermd.project.claimservices.util.x12.X12;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static rovermd.project.claimservices.util.UtilityHelper.*;

@Service
public class ClaimServiceEDIImpl implements ClaimServiceEDI {

    @Autowired
    ClaiminfomasterRepository claiminfomasterRepository;

    @Autowired
    private CPTRepository cPTRepository;

    @Autowired
    private ICDRepository iCDRepository;

    @Autowired
    private AnesthesiacodeRepository anesthesiacodeRepository;

    @Autowired
    private ExternalService externalService;

    @Autowired
    private ClaimServiceSrubberImpl claimServiceScrubberImpl;

    @Transactional
    @Override
    public Object createEDI_Prof(Integer claimId) throws ParseException, IOException {
        Claiminfomaster claim = claiminfomasterRepository.findById(claimId).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));
        List<String> ErrorMsgs = new ArrayList<>();
        List<ScrubberRulesDto> rulesList = new ArrayList<>();

        if (claim.getScrubbed() == 0) {
            return claimServiceScrubberImpl.gettingRulesFormatted(claim, rulesList, ErrorMsgs);
        }

        String PatientName = "";
        String PatientRegId = "";
        String VisitId = "";
        String ClaimNumber = claim.getClaimNumber();
        String DirectoryName = "";
        String PriInsuranceName;
        String PriInsuranceNameId;
        String timesSubmitted = String.valueOf(claim.getTimesSubmitted() == null ? "1" : claim.getTimesSubmitted() + 1);
        String PriFillingIndicator = "";
        String OrigClaim = "";
        String GrpNumber = "";
        String MemId = "";

        String Payer_Address = "";
        String Payer_City = "";
        String Payer_State = "";
        String Payer_Zip = "";
        String ClaimCreateDate = "";
        String ClaimCreateTime = "";
        String InterControlNo = "";


        String CreationDate = claim.getCreatedDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        ClaimCreateDate = claim.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        ClaimCreateTime = claim.getCreatedDate().format(DateTimeFormatter.ofPattern("Hmm"));
        String ClaimType = "";


        String BillingProviderLastName = "";
        String BillingProviderFirstName = "";
        String BillingProviderNPI = "";
        String BillingProvider_Taxonomy = "";
        String BillingProvider_Tel = "";
        String BillingProvider_ETIN = "";
        DoctorDTO BillingProvidersDetailsById = null;


        String ReferringProviderLastName = "";
        String ReferringProviderFirstName = "";
        String ReferringProviderNPI = "";
        String ReferringProvider_Taxonomy = "";
        DoctorDTO ReferringProviderDetailsById = null;


        String SupervisingProviderLastName = "";
        String SupervisingProviderFirstName = "";
        String SupervisingProviderNPI = "";
        String SupervisingProvider_Taxonomy = "";

        String RenderingProvidersLastName = "";
        String RenderingProvidersFirstName = "";
        String RenderingProvidersNPI = "";
        String RenderingProviders_Taxonomy = "";
        DoctorDTO RenderingProviderDetailsById = null;


        String OrderingProvidersLastName = "";
        String OrderingProvidersFirstName = "";
        String OrderingProvidersNPI = "";
        String OrderingProviders_Taxonomy = "";
        DoctorDTO OrderingProviderDetailsById = null;


        String OperatingProviderLastName = "";
        String OperatingProviderFirstName = "";
        String OperatingProviderNPI = "";
        String OperatingProvider_Taxonomy = "";


        String PickUpAddressInfoCode = "";
        String PickUpCityInfoCode = "";
        String PickUpStateInfoCode = "";
        String PickUpZipCodeInfoCode = "";
        String DropoffAddressInfoCode = "";
        String DropoffCityInfoCode = "";
        String DropoffStateInfoCode = "";
        String DropoffZipCodeInfoCode = "";


        String Organization = null;
        String Organization_Tel = null;
        String Organization_Email = null;

        String Client_FullName = "";
        String ClientAddress = "";
        String ClientCity = "";
        String ClientTaxID = "";
        String ClientState = "";
        String ClientZipCode = "";
        String ClientPhone = "";
        String ClientNPI = "";
        String TaxanomySpecialty = "";

        String PatientRelationshipCode = "";
        String Related_Causes_Code = "";
        String PayerResponsibilityCode = "P";

        String PatientFirstName = "";
        String SSN = "";
        String PatientLastName = "";
        String DOS = "";
        String DOB = "";
        String _DOB = "";
        String Gender = "";
        String PriInsuredName = "";
        String PriInsuredLastName = "";
        String PriInsuredFirstName = "";
        String PatientRelationtoPrimary = "";
        String IndividualRelationshipCode = "";
        String PatientRelationtoSec = "";


        String SecInsuredName = "";
        String SecInsuredLastName = "";
        String SecInsuredFirstName = "";

        String Address = "";
        String City = "";
        String State = "";
        String StateCode = "";
        String ZipCode = "";
        String PatientControlNumber = "";
        String userid = "";

        String frequencyCode = claim.getFreq();
        Integer ClientId = claim.getClientId();
        String CLIA_number = "";


        if (!isEmpty(claim.getRenderingProvider())) {
            try {
                RenderingProviderDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getRenderingProvider()));

                RenderingProvidersLastName = RenderingProviderDetailsById.getDoctorsLastName().toUpperCase();
                RenderingProvidersFirstName = RenderingProviderDetailsById.getDoctorsFirstName().toUpperCase();
                RenderingProvidersNPI = RenderingProviderDetailsById.getNpi();
                RenderingProviders_Taxonomy = RenderingProviderDetailsById.getTaxonomySpecialty();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting rendering provider details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;

            }

        }

        if (!isEmpty(claim.getBillingProviders())) {
            try {
                BillingProvidersDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getBillingProviders()));

                BillingProviderLastName = BillingProvidersDetailsById.getDoctorsLastName().toUpperCase();
                BillingProviderFirstName = BillingProvidersDetailsById.getDoctorsFirstName().toUpperCase();
                BillingProviderNPI = BillingProvidersDetailsById.getNpi();
                BillingProvider_Taxonomy = BillingProvidersDetailsById.getTaxonomySpecialty();
                BillingProvider_Tel = BillingProvidersDetailsById.getEtin();
                BillingProvider_ETIN = BillingProvidersDetailsById.getPhoneNumber();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting billing provider details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }

        }

        if (!isEmpty(claim.getReferringProvider())) {
            try {
                ReferringProviderDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getReferringProvider()));

                ReferringProviderLastName = ReferringProviderDetailsById.getDoctorsLastName().toUpperCase();
                ReferringProviderFirstName = ReferringProviderDetailsById.getDoctorsFirstName().toUpperCase();
                ReferringProviderNPI = ReferringProviderDetailsById.getNpi();
                ReferringProvider_Taxonomy = ReferringProviderDetailsById.getTaxonomySpecialty();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting referring provider details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;

            }
        }

        if (!isEmpty(claim.getOrderingProvider())) {
            try {
                OrderingProviderDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getOrderingProvider()));

                OrderingProvidersLastName = OrderingProviderDetailsById.getDoctorsLastName().toUpperCase();
                OrderingProvidersFirstName = OrderingProviderDetailsById.getDoctorsFirstName().toUpperCase();
                OrderingProvidersNPI = OrderingProviderDetailsById.getNpi();
                OrderingProviders_Taxonomy = OrderingProviderDetailsById.getTaxonomySpecialty();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting ordering provider details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }

        String ICDA = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcda());
        String ICDB = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdb());
        String ICDC = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdc());
        String ICDD = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdd());
        String ICDE = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcde());
        String ICDF = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdf());
        String ICDG = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdg());
        String ICDH = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdh());
        String ICDI = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdi());
        String ICDJ = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdj());
        String ICDK = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdk());
        String ICDL = CheckStringVariable(claim.getClaimchargesinfo().get(0).getIcdl());

        String ServiceFromDate = "";
        String ServiceToDate = "";
        String POS = claim.getClaimchargesinfo().get(0).getPos();

        ArrayList<Integer> POS_list = new ArrayList<>(Arrays.asList(41, 42, 99));
        String[] Taxonomy_for_InitialTreatment = {"111N00000X", "111NI0013X", "111NI0900X", "111NN0400X", "111NN1001X",
                "111NP0017X", "111NR0200X", "111NR0400X", "111NS0005X", "111NT0100X", "111NX0100X", "111NX0800X"};
        List<String> Taxonomy_for_InitialTreatment_List = Arrays.asList(Taxonomy_for_InitialTreatment);
        String[] Taxonomy = {"213ES0131X", "213EG0000X", "213EP1101X", "213EP0504X", "213ER0200X", "213ES0000X"};
        List<String> TaxonomyList = Arrays.asList(Taxonomy);
        ArrayList<String> ICDs = new ArrayList<>();


        String HospitalizedFromDateAddInfo = "";
        String HospitalizedToDateAddInfo = "";
        String InitialTreatDateAddInfo = "";
        String AccidentIllnesDateAddInfo = "";
        String LastMenstrualPeriodDateAddInfo = "";
        String UnabletoWorkFromDateAddInfo = "";
        String UnabletoWorkToDateAddInfo = "";
        String ClaimNoteAddinfo = "";
        String SpecialProgCodeAddInfo = "";
        String PatientSignOnFileAddInfo = "";
        String InsuredSignOnFileAddInfo = "";
        String LastSeenDateAddInfo = "";


        PatientControlNumber = "FAM" + ClientId + timesSubmitted + "E" + ClaimNumber.replace("-", "");


        StringBuilder LX_SV1 = new StringBuilder();

        if (!isEmpty(claim.getClientId())) {
            try {
                ClientDTO clientDetailsById = externalService.getClientDetailsById(claim.getClientId());
                ClientAddress = clientDetailsById.getAddress();
                ClientCity = clientDetailsById.getCity();
                ClientTaxID = clientDetailsById.getTaxid();
                ClientState = clientDetailsById.getState();
                ClientZipCode = clientDetailsById.getZipCode();
                ClientPhone = clientDetailsById.getPhone();
                ClientNPI = clientDetailsById.getNpi();
                TaxanomySpecialty = clientDetailsById.getTaxanomySpecialty();
                Client_FullName = clientDetailsById.getFullName();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting client details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }

        if (
                (!isEmpty(claim.getPatientRegId())
                        && !isEmpty(claim.getVisitId())
                        && !isEmpty(claim.getPriInsuranceNameId()))
                        || !isEmpty(claim.getSecondaryInsuranceId())
        ) {
            try {

                PatientReqDto patientReqDto = new PatientReqDto();
                patientReqDto.setPatientRegId(claim.getPatientRegId() == null ? null : Long.valueOf(claim.getPatientRegId()));
                patientReqDto.setVisitId(claim.getVisitId() == null ? null : Long.valueOf(claim.getVisitId()));
                patientReqDto.setPrimaryInsuranceId(1L);//claim.getPriInsuranceNameId() == null ? null : Long.valueOf(claim.getPriInsuranceNameId()));
                patientReqDto.setSecondaryInsuranceId(claim.getSecondaryInsuranceId() == null ? null : Long.valueOf(claim.getSecondaryInsuranceId()));

                PatientDto patientDetailsById = externalService.getPatientDetailsById(patientReqDto);
                DateTimeFormatter DOBformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter ClaimCreateDateAndDOSformatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                PatientFirstName = patientDetailsById.getFirstName().toUpperCase();
                SSN = patientDetailsById.getSSN();
                PatientLastName = patientDetailsById.getLastName().toUpperCase();
                DOB = String.valueOf(patientDetailsById.getDOB().format(ClaimCreateDateAndDOSformatter));
                _DOB = String.valueOf(patientDetailsById.getDOB().format(DOBformatter));
                DOS = patientDetailsById.getDOS().format(ClaimCreateDateAndDOSformatter);
                Gender = patientDetailsById.getGender();
                Address = patientDetailsById.getAddress();
                City = patientDetailsById.getCity();
                State = patientDetailsById.getState();
                ZipCode = patientDetailsById.getZipCode();
                PriInsuredFirstName = patientDetailsById.getPriInsurerFirstName();
                PriInsuredLastName = patientDetailsById.getPriInsurerLastName();
                PatientRelationtoPrimary = patientDetailsById.getPatientRelationtoPrimary();
                SecInsuredFirstName = patientDetailsById.getSecInsurerFirstName();
                SecInsuredLastName = patientDetailsById.getSecInsurerLastName();
                PatientRelationtoSec = patientDetailsById.getPatientRelationshiptoSecondry();
                MemId = claim.getMemId();
                GrpNumber = claim.getGrpNumber();

            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting patient details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }


        InterControlNo = claiminfomasterRepository.getInterControlNo();
        InterControlNo = InterControlNo.equals("") ? "000000001" : String.format("%09d", Integer.parseInt(InterControlNo) + 1);
        String TotalChargeAmountStr = String.valueOf(claim.getTotalCharges());

        HashMap<String, Integer> ISATAG = new HashMap<>();
        int ClaimTypes = 1; //1 prof , 2 inst
        int HLCounter = 1;
        String ClaimTypeIdentifier = "005010X222A1";// 005010X222A1 Prof , 005010X223A2 Inst

        Context c = new Context('~', '*', ':');

        ISATAG.put("0000-ISA01-AuthorizationInfoQualifier-00", 2);
        ISATAG.put("0000-ISA02-AuthorizationInformation-", 10);
        ISATAG.put("0000-ISA03-SecurityInformationQualifier-00", 2);
        ISATAG.put("0000-ISA04-SecurityInformation-          ", 10);
        ISATAG.put("0000-ISA05-Interchange ID Qualifier-ZZ", 2);
        ISATAG.put("0000-ISA06-Interchange Sender ID (*)-AV09311993        ", 15);
        ISATAG.put("0000-ISA07-Interchange ID Qualifier-01", 2);
        ISATAG.put("0000-ISA08-Interchange Receiver ID-030240928          ", 15);
        ISATAG.put("0000-ISA09-InterchangeDate-140205", 6);
        ISATAG.put("0000-ISA10-InterchangeTime-1452", 4);
        ISATAG.put("0000-ISA11-RepetitionSeparator-^", 1);
        ISATAG.put("0000-ISA12-InterchangeControlVersionNumber-00501", 5);
        ISATAG.put("0000-ISA13-InterchangeControlNumber-100000467", 9);
        ISATAG.put("0000-ISA14-AcknowledgmentRequested-0", 1);
        ISATAG.put("0000-ISA15-Usage Indicator-T", 1);
        ISATAG.put("0000-ISA16-ComponentElementSeparator-:~", 1);

        HashMap<Integer, String> ISAValue = new HashMap<Integer, String>();

        ISAValue.put(0, "ISA");
        ISAValue.put(1, "00");
        ISAValue.put(2, " ");
        ISAValue.put(3, "00");
        ISAValue.put(4, " ");
        ISAValue.put(5, "ZZ");
//                ISAValue.put(6, "BS01834");
        ISAValue.put(6, "AV09311993");
//                ISAValue.put(7, "ZZ");
        ISAValue.put(7, "01");
//                ISAValue.put(8, "33477");
        ISAValue.put(8, "030240928");
//                ISAValue.put(9,"140205");
        ISAValue.put(9, CreationDate);
//                ISAValue.put(10,"1452");
        ISAValue.put(10, ClaimCreateTime);
        ISAValue.put(11, "^");
        ISAValue.put(12, "00501");
//                ISAValue.put(13,"000987654");
        ISAValue.put(13, InterControlNo);
        ISAValue.put(14, "0");
        ISAValue.put(15, "P");
//                ISAValue.put(15, "T");
        ISAValue.put(16, ":");

        X12 x12 = new X12(c);
        Loop loop_isa = x12.addChild("ISA");

        // add segment
        loop_isa.addSegment("ISA*00*          *00*          *ZZ*SENDERID       *01*RECEIVERID    *" + CreationDate + "*" + ClaimCreateTime + "*U*00401*" + InterControlNo + "*0*P*:");

        for (String key : ISATAG.keySet()) {
            //   //system.out.println(key);
            String temp[] = key.split("-");
            int elementid = Integer.parseInt(getOnlyDigits(temp[1]));
            int len = ISATAG.get(key);
            // //system.out.println(elementid);
            // //system.out.println(len);
            loop_isa.getSegment(0).setElement(elementid, ISAValue.get(elementid), len);
        }


        Loop loop_gs = loop_isa.addChild("GS");
        // Add GS segment directly as a string
        loop_gs.addSegment("GS*HC*AV09311993*030240928*" + ClaimCreateDate + "*" + ClaimCreateTime + "*" + InterControlNo + "*X*" + ClaimTypeIdentifier);

        Loop loop_st = loop_gs.addChild("ST");
        loop_st.addSegment("ST*837*" + InterControlNo + "*" + ClaimTypeIdentifier);// 005010X223 <===> 005010X222A1
        loop_st.addSegment("BHT*0019*00*" + ClaimCreateDate + ClaimCreateTime + "*" + ClaimCreateDate + "*" + ClaimCreateTime + "*CH");

        //1000A SUBMITTER NAME
        Loop loop_1000A = loop_st.addChild("1000A");

//        loop_1000A.addSegment("NM1*41*2*" + Organization + "*****46*12345");//Organization ID to be included later
        loop_1000A.addSegment("NM1*41*2*" + BillingProviderFirstName + " " + BillingProviderLastName + "*****46*" + BillingProvider_ETIN);//Organization ID to be included later
//"PER*IC< Information Contact>*JANE DOE< Name >*TE< Telephone Qualifier >*9005555555< Telephone >"
//                    loop_1000A.addSegment("PER*IC*" + Organization + "*TE*" + Organization_Tel + "*EM*" + Organization_Email);
        loop_1000A.addSegment("PER*IC*" + BillingProviderFirstName + " " + BillingProviderLastName + "*TE*" + BillingProvider_Tel);

        if (isEmpty(Organization))
            ErrorMsgs.add("<b>Organization</b> is <b>Missing</b>");
        if (isEmpty(Organization_Tel))
            ErrorMsgs.add("<b>Organization Telephone</b> is <b>Missing</b>");
        if (isEmpty(Organization_Email))
            ErrorMsgs.add("<b>Organization Email</b> is <b>Missing</b>");

        //1000B RECEIVER NAME
        Loop loop_1000B = loop_st.addChild("1000B");
        String RecieverInsurance = "MEDICARE";
//                    String RecieverInsurance = "";
        String RecieverInsurance_IdentificationCode = "00120";
//                    String RecieverInsurance_IdentificationCode = "";

        if (isEmpty(RecieverInsurance))
            ErrorMsgs.add("<b>Insurance</b> is <b>Missing</b>");
        if (isEmpty(RecieverInsurance_IdentificationCode))
            ErrorMsgs.add("<b>Insurance ID </b> is <b>Missing</b>");

        PriInsuranceName = claim.getPriInsuranceNameId();
        InsuranceDTO PrimaryinsuranceDetailsById = externalService.getInsuranceDetailsById(String.valueOf(902));
        PriInsuranceName = !isEmpty(PriInsuranceName) ? PrimaryinsuranceDetailsById.getPayerName().replace("Servicing States", "").replaceAll(":", " ").trim().replaceAll("&", "").replaceAll("\n", " ").replaceAll(" +", " ") : null;
        PriInsuranceNameId = PrimaryinsuranceDetailsById.getPayerID();
        PriFillingIndicator = PrimaryinsuranceDetailsById.getClaimIndicator_P();
        Payer_Address = PrimaryinsuranceDetailsById.getAddress();
        Payer_City = PrimaryinsuranceDetailsById.getCity();
        Payer_State = PrimaryinsuranceDetailsById.getState();
        Payer_Zip = PrimaryinsuranceDetailsById.getZip();


        PriInsuranceName = PriInsuranceName.length() < 50 ? PriInsuranceName : PriInsuranceName.substring(0, 50).trim();
        loop_1000B.addSegment("NM1*40*2*" + PriInsuranceName + "*****46*" + PriInsuranceNameId);


        Loop loop_2000 = loop_st.addChild("2000");
        loop_2000.addSegment("HL*" + HLCounter + "**20*1"); //HL BILLING PROVIDER HIERARCHICAL LEVEL


        loop_2000.addSegment("PRV*BI*PXC*" + BillingProvider_Taxonomy); //PRV BILLING PROVIDER SPECIALTY

        //2010AA BILLING PROVIDER NAME
        Loop loop_2010_1 = loop_2000.addChild("2010");
        //                    loop_2010_1.addSegment("NM1*85*1*" + BillingProviderLastName + "*" + BillingProviderFirstName + "****XX*" + BillingProviderNPI);//NM1 BILLING PROVIDER NAME INCLUDING NATIONAL PROVIDER ID
        loop_2010_1.addSegment("NM1*85*2*" + Client_FullName + "*****XX*" + ClientNPI);//NM1 BILLING PROVIDER NAME INCLUDING NATIONAL PROVIDER ID
        //                    loop_2010_1.addSegment("N3*" + BillingProvider_Address); //N3 BILLING PROVIDER ADDRESS
        loop_2010_1.addSegment("N3*" + ClientAddress); //N3 BILLING PROVIDER ADDRESS
        loop_2010_1.addSegment("N4*" + ClientCity + "*" + ClientState + "*" + ClientZipCode);//N4 BILLING PROVIDER LOCATION
        loop_2010_1.addSegment("REF*EI*" + ClientTaxID); //REF BILLING PROVIDER TAX IDENTIFICATION NUMBER


        HLCounter++;
        //2000B SUBSCRIBER HL LOOP
        Loop loop_2000B = loop_2000.addChild("2000");
//                    loop_2000B.addSegment("HL*" + HLCounter + "*1*22*0");//HL SUBSCRIBER HIERARCHICAL LEVEL

        if (PatientRelationshipCode.equals("18")) {
            loop_2000B.addSegment("HL*" + HLCounter + "*1*22*0");//HL SUBSCRIBER HIERARCHICAL LEVEL
        } else {
            loop_2000B.addSegment("HL*" + HLCounter + "*1*22*1");//HL SUBSCRIBER HIERARCHICAL LEVEL
        }

        if (PatientRelationtoPrimary.equalsIgnoreCase("Self")) {
            PatientRelationshipCode = "18";
        } else {
            PatientRelationshipCode = "";
        }


        loop_2000B.addSegment("SBR*" + PayerResponsibilityCode + "*" + PatientRelationshipCode + "*" + GrpNumber + "******" + PriFillingIndicator);//SBR SUBSCRIBER INFORMATION

        //2010BA SUBSCRIBER NAME LOOP
        Loop loop_2010BA = loop_2000.addChild("2000");

        if (PatientRelationshipCode.equals("18")) {
            loop_2010BA.addSegment("NM1*IL*1*" + PatientLastName + "*" + PatientFirstName + "*T***MI*" + MemId);//NM1 SUBSCRIBER NAME
            loop_2010BA.addSegment("N3*" + Address);//N3 SUBSCRIBER ADDRESS
            loop_2010BA.addSegment("N4*" + City + "*" + State + "*" + ZipCode);//N4 SUBSCRIBER LOCATION
            loop_2010BA.addSegment("DMG*D8*" + DOB + "*" + Gender); //DMG SUBSCRIBER DEMOGRAPHIC INFORMATION
        } else {
            loop_2010BA.addSegment("NM1*IL*1*" + PriInsuredLastName + "*" + PriInsuredFirstName + "*T***MI*" + MemId);//NM1 SUBSCRIBER NAME
        }

        //2010BB PAYER NAME LOOP
        Loop loop_2010BB = loop_2000.addChild("2000");
        loop_2010BB.addSegment("NM1*PR*2*" + PriInsuranceName + "*****PI*" + PriInsuranceNameId);//NM1 PAYER NAME **PAYER_ID
        loop_2010BB.addSegment("N3*" + Payer_Address);//N3 PAYER ADDRESS ** ADDED
        loop_2010BB.addSegment("N4*" + Payer_City + "*" + Payer_State + "*" + Payer_Zip);//N4 PAYER ADDRESS ** ADDED
//                    loop_2010BB.addSegment("REF*G2*330127");//REF BILLING PROVIDER SECONDARY IDENTIFICATION SITUATIONAL

        if (PatientRelationshipCode.compareTo("18") != 0) {
            HLCounter++;
            Loop loop_2000C = loop_st.addChild("2000");
            loop_2000C.addSegment("HL*" + HLCounter + "*2*23*0");
            loop_2000C.addSegment("PAT*" + IndividualRelationshipCode);
            loop_2000C.addSegment("NM1*QC*1*" + PatientLastName + "*" + PatientFirstName);
            loop_2000C.addSegment("N3*" + Address);
            loop_2000C.addSegment("N4*" + City + "*" + State + "*" + ZipCode);
            loop_2000C.addSegment("DMG*D8*" + DOB + "*" + Gender);
        }

        Loop loop_2300 = loop_st.addChild("2300");

        if (Related_Causes_Code != null)
            loop_2300.addSegment("CLM*" + PatientControlNumber + "*" + TotalChargeAmountStr + "***" + POS + ":B:" + frequencyCode + "*Y*A*Y*Y**" + Related_Causes_Code); //CLM CLAIM LEVEL INFORMATION
        else
            loop_2300.addSegment("CLM*" + PatientControlNumber + "*" + TotalChargeAmountStr + "***" + POS + ":B:" + frequencyCode + "*Y*A*Y*Y***"); //CLM CLAIM LEVEL INFORMATION

        if (ClientId == 32) {
            final String CLIA_REGEX = "^([0-9]{2}[A-Z]{1}[0-9]{7})*$";
            if (!isEmpty(CLIA_number)) {
                if (!CLIA_number.matches(CLIA_REGEX))
                    ErrorMsgs.add(" <b>CLIA</b> is <b>InValid</b> Must be in <b>NNDNNNNNNN</b> format. where <b>N</b> is <b>numeric</b>");
                else
                    loop_2300.addSegment("REF*X4*" + CLIA_number);
            } else {
                ErrorMsgs.add(" <b>CLIA</b> is <b>Missing</b>");
            }
        }


        if (!ICDA.equals("")) {
            ICDs.add(ICDA);
            ICDA = ICDA.replace(".", "");
        }
        if (!ICDB.equals("")) {
            ICDs.add(ICDB);
            ICDB = ICDB.replace(".", "");
            ICDB = "*ABF:" + ICDB;
        }
        if (!ICDC.equals("")) {
            ICDs.add(ICDC);
            ICDC = ICDC.replace(".", "");
            ICDC = "*ABF:" + ICDC;
        }
        if (!ICDD.equals("")) {
            ICDs.add(ICDD);
            ICDD = ICDD.replace(".", "");
            ICDD = "*ABF:" + ICDD;
        }
        if (!ICDE.equals("")) {
            ICDs.add(ICDA);
            ICDE = ICDE.replace(".", "");
            ICDE = "*ABF:" + ICDE;
        }
        if (!ICDF.equals("")) {
            ICDs.add(ICDF);
            ICDF = ICDF.replace(".", "");
            ICDF = "*ABF:" + ICDF;
        }
        if (!ICDG.equals("")) {
            ICDs.add(ICDG);

            ICDG = ICDG.replace(".", "");
            ICDG = "*ABF:" + ICDG;
        }
        if (!ICDH.equals("")) {
            ICDs.add(ICDH);
            ICDH = ICDH.replace(".", "");
            ICDH = "*ABF:" + ICDH;
        }
        if (!ICDI.equals("")) {
            ICDs.add(ICDI);

            ICDI = ICDI.replace(".", "");
            ICDI = "*ABF:" + ICDI;
        }
        if (!ICDJ.equals("")) {
            ICDs.add(ICDJ);
            ICDJ = ICDJ.replace(".", "");
            ICDJ = "*ABF:" + ICDJ;
        }
        if (!ICDK.equals("")) {
            ICDs.add(ICDK);
            ICDK = ICDK.replace(".", "");
            ICDK = "*ABF:" + ICDK;
        }
        if (!ICDL.equals("")) {
            ICDs.add(ICDL);
            ICDL = ICDL.replace(".", "");
            ICDL = "*ABF:" + ICDL;
        }


        loop_2300.addSegment("HI*ABK:" + ICDA + ICDB + ICDC + ICDD + ICDE + ICDF + ICDG + ICDH + ICDI + ICDJ + ICDK);

        if ((!isEmpty(Related_Causes_Code) && Related_Causes_Code.equals("AA")) || (!isEmpty(Related_Causes_Code) && Related_Causes_Code.equals("OA"))) {
            if (isEmpty(AccidentIllnesDateAddInfo)) {
                ErrorMsgs.add(" <b>Accident Date</b> is <b>Missing</b>");
            } else if (isInValidDate(AccidentIllnesDateAddInfo, DOS)) {
                ErrorMsgs.add(" <b>Accident Date</b> is <b>InValid</b>");
            } else {
                loop_2300.addSegment("DTP*439*D8*" + AccidentIllnesDateAddInfo);// Accident Date
            }
        }

        if (!isEmpty(BillingProvider_Taxonomy)) {
            //system.out.println("BillingProvider_Taxonomy ->> " + BillingProvider_Taxonomy);

            if (Taxonomy_for_InitialTreatment_List.contains(BillingProvider_Taxonomy)) {
                //system.out.println("BillingProvider_Taxonomy ->> " + BillingProvider_Taxonomy);
                if (PriFillingIndicator.equals("MB") || PriFillingIndicator.equals("MA")) {
                    if (isEmpty(InitialTreatDateAddInfo)) {
                        ErrorMsgs.add("<b>Initial Treatment Date</b> is  <b>Missing</b>");
                    } else if (isInValidDate(InitialTreatDateAddInfo, DOS)) {
                        ErrorMsgs.add("<b>Initial Treatment Date</b> is  <b>InValid</b>");
                    } else {
                        loop_2300.addSegment("DTP*454*D8*" + InitialTreatDateAddInfo);//Initial Treatment Date
                    }
                }
            }


            if (TaxonomyList.contains(BillingProvider_Taxonomy)) {
                //Last Seen Date of Claim should Not be Equal to Date Time Min Value
                if (isEmpty(LastSeenDateAddInfo)) {
                    ErrorMsgs.add("<b>Last Seen Date</b> is  <b>Missing</b>, Last Seen Date is required for this speciality code <b>[" + BillingProvider_Taxonomy + "]</b>");
                } else if (isInValidDate(LastSeenDateAddInfo, DOS)) {
                    ErrorMsgs.add("<b>Last Seen Date</b> is  <b>InValid</b>,  Last Seen Date is required for this speciality code <b>[" + BillingProvider_Taxonomy + "]</b>");
                } else {
                    loop_2300.addSegment("DTP*304*D8*" + LastSeenDateAddInfo);//LAST SEEN Date
                }
            }
        }


        ArrayList<String> POS_list_AMBULANCE_CLAIMS = new ArrayList<String>(Arrays.asList("21", "51", "61", "11", "12", "13", "22", "31", "32", "65"));
//                    ArrayList<Integer> _POS_list_ = new ArrayList<Integer>(Arrays.asList(11,12,13,22,31,32,65));
        //system.out.println("POS ->>>> " + POS);
        if (POS_list_AMBULANCE_CLAIMS.contains(POS)) {
            if (isEmpty(HospitalizedFromDateAddInfo)) {
                ErrorMsgs.add(" <b>Admission DATE</b> is <b>Missing</b>");
            } else if (isInValidDate(HospitalizedFromDateAddInfo, DOS)) {
                ErrorMsgs.add(" <b>Admission DATE </b> is <b>InValid</b>");
            } else {
                loop_2300.addSegment("DTP*435*D8*" + HospitalizedFromDateAddInfo);// Admission Date
            }
        }


        //                    2310A REFERRING PROVIDER NAME

        if (!isEmpty(ReferringProviderFirstName)) {


            Loop loop_2310A = loop_2300.addChild("2300");
            loop_2310A.addSegment("NM1*DN*1*" + ReferringProviderLastName + "*" + ReferringProviderFirstName + "****XX*" + ReferringProviderNPI);//NM1 Referring PROVIDER
//                        loop_2310A.addSegment("REF*G2*B99937");//REF Referring PROVIDER SECONDARY IDENTIFICATION


        }

        if (!isEmpty(RenderingProvidersFirstName)) {
            // 2310B RENDERING PROVIDER NAME
            Loop loop_2310B = loop_2300.addChild("2300");

            loop_2310B.addSegment("NM1*82*1*" + RenderingProvidersLastName + "*" + RenderingProvidersFirstName + "****XX*" + RenderingProvidersNPI);//NM1 Rendering PROVIDER
//                        String RenderingProviders_Taxonomy = "225100000X";

            loop_2310B.addSegment("PRV*PE*PXC*" + RenderingProviders_Taxonomy); //PRV Rendering Provider SPECIALTY


        }

        if (!isEmpty(SupervisingProviderFirstName)) {

            Loop loop_2310D = loop_2300.addChild("2300");
            loop_2310D.addSegment("NM1*DQ*1*" + SupervisingProviderLastName + "*" + SupervisingProviderFirstName + "****XX*" + SupervisingProviderNPI);//NM1 Supervising PROVIDER
//                        loop_2310D.addSegment("REF*G2*B99937");//REF Referring PROVIDER SECONDARY IDENTIFICATION
        }


        if (POS_list.contains(POS)) {
            if (!isEmpty(PickUpAddressInfoCode) && !isEmpty(PickUpZipCodeInfoCode)
                    && !isEmpty(PickUpCityInfoCode) && !isEmpty(PickUpStateInfoCode)) {

                Loop loop_2310E = loop_2300.addChild("2300"); //AMBULANCE PICK-UP LOCATION
                loop_2310E.addSegment("NM1*PW*2");
                loop_2310E.addSegment("N3*" + PickUpAddressInfoCode);
                loop_2310E.addSegment("N4*" + PickUpCityInfoCode + "*" + PickUpStateInfoCode + "*" + PickUpZipCodeInfoCode);
            }

            if (!isEmpty(DropoffAddressInfoCode) && !isEmpty(DropoffCityInfoCode)
                    && !isEmpty(DropoffStateInfoCode) && !isEmpty(DropoffZipCodeInfoCode)) {


                Loop loop_2310F = loop_2300.addChild("2300"); //AMBULANCE DROP-OFF LOCATION
                loop_2310F.addSegment("NM1*45*2");
                loop_2310F.addSegment("N3*" + DropoffAddressInfoCode);
                loop_2310F.addSegment("N4*" + DropoffCityInfoCode + "*" + DropoffStateInfoCode + "*" + DropoffZipCodeInfoCode);
            }
        }


        int Count = 1;
        char[] DXPointerDB = null;
        StringBuilder DXPointerEDI = new StringBuilder();
        Loop loop_2400 = loop_st.addChild("2400");

        String Units = null;
        String mod1 = null;
        String mod2 = null;
        String mod3 = null;
        String mod4 = null;
        String ProcedureCode = "";
        String Amount = "";
        String MeasurementCode = "";
        SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd");
        Double TotalChargeAmount = 0.00;


        for (Claimchargesinfo charge : claim.getClaimchargesinfo()) {
            if (charge.getStatus() == 1) {
                mod1 = charge.getMod1();
                mod2 = charge.getMod2();
                mod3 = charge.getMod3();
                mod4 = charge.getMod4();
                ProcedureCode = charge.getHCPCSProcedure();
                Amount = String.valueOf(charge.getAmount());
                Units = String.valueOf(charge.getUnits());
                ServiceFromDate = charge.getServiceFromDate();
                ServiceToDate = charge.getServiceToDate();


                loop_2400.addSegment("LX*" + Count);
                String modifier = "";
                if (!isEmpty(mod1)) {//mod1
                    modifier += ":" + mod1;
                }
                if (!isEmpty(mod2)) {//mod2
                    modifier += ":" + mod2;
                }
                if (!isEmpty(mod3)) {//mod3
                    modifier += ":" + mod3;
                }
                if (!isEmpty(mod4)) {//mod4
                    modifier += ":" + mod4;
                }

                DXPointerDB = charge.getDXPointer().toCharArray();
                for (int x = 0; x < DXPointerDB.length; x++) {
                    if (x == 0) {
                        if (DXPointerDB[x] == 'A') {
                            DXPointerEDI = new StringBuilder("1");
                        } else if (DXPointerDB[x] == 'B') {
                            DXPointerEDI = new StringBuilder(":2");
                        } else if (DXPointerDB[x] == 'C') {
                            DXPointerEDI = new StringBuilder("3");
                        } else if (DXPointerDB[x] == 'D') {
                            DXPointerEDI = new StringBuilder("4");
                        } else if (DXPointerDB[x] == 'E') {
                            DXPointerEDI = new StringBuilder("5");
                        } else if (DXPointerDB[x] == 'F') {
                            DXPointerEDI = new StringBuilder("6");
                        } else if (DXPointerDB[x] == 'G') {
                            DXPointerEDI = new StringBuilder("7");
                        } else if (DXPointerDB[x] == 'H') {
                            DXPointerEDI = new StringBuilder("8");
                        } else if (DXPointerDB[x] == 'I') {
                            DXPointerEDI = new StringBuilder("9");
                        } else if (DXPointerDB[x] == 'J') {
                            DXPointerEDI = new StringBuilder("10");
                        } else if (DXPointerDB[x] == 'K') {
                            DXPointerEDI = new StringBuilder("11");
                        } else if (DXPointerDB[x] == 'L') {
                            DXPointerEDI = new StringBuilder("12");
                        }
                    } else {
                        if (DXPointerDB[x] == 'A') {
                            DXPointerEDI.append(":1");
                        } else if (DXPointerDB[x] == 'B') {
                            DXPointerEDI.append(":2");
                        } else if (DXPointerDB[x] == 'C') {
                            DXPointerEDI.append(":3");
                        } else if (DXPointerDB[x] == 'D') {
                            DXPointerEDI.append(":4");
                        } else if (DXPointerDB[x] == 'E') {
                            DXPointerEDI.append(":5");
                        } else if (DXPointerDB[x] == 'F') {
                            DXPointerEDI.append(":6");
                        } else if (DXPointerDB[x] == 'G') {
                            DXPointerEDI.append(":7");
                        } else if (DXPointerDB[x] == 'H') {
                            DXPointerEDI.append(":8");
                        } else if (DXPointerDB[x] == 'I') {
                            DXPointerEDI.append(":9");
                        } else if (DXPointerDB[x] == 'J') {
                            DXPointerEDI.append(":10");
                        } else if (DXPointerDB[x] == 'K') {
                            DXPointerEDI.append(":11");
                        } else if (DXPointerDB[x] == 'L') {
                            DXPointerEDI.append(":12");
                        }
                    }
                }

                MeasurementCode = anesthesiacodeRepository.validateAnesthesiaCodes(ProcedureCode) == 1 ? "MJ" : "UN";


                loop_2400.addSegment("SV1*HC:" + ProcedureCode + modifier + "*" + Amount + "*" + MeasurementCode + "*" + Units + "***" + DXPointerEDI);
                if (ServiceFromDate.equals(ServiceToDate)) {
                    loop_2400.addSegment("DTP*472*D8*" + myFormat.format(fromUser.parse(ServiceFromDate)));
                } else {
                    loop_2400.addSegment("DTP*472*RD8*" + myFormat.format(fromUser.parse(ServiceFromDate)) + "-" + myFormat.format(fromUser.parse(ServiceToDate)));
                }

                if (cPTRepository.validateMammographyCPT(ProcedureCode) > 0 && (!modifier.contains("26") || isEmpty(modifier))) {
                    if (isEmpty(claim.getClaimadditionalinfo().getMemmoCertAddInfo())) {
                        ErrorMsgs.add("<b>FDA Certification Number</b> is Missing with Mammography services [<b>" + ProcedureCode + "</b>] Please check the location settings and update FDA Certification Number \n");
                    } else {
                        loop_2300.addSegment("REF*EW*" + claim.getClaimadditionalinfo().getMemmoCertAddInfo());
                    }
                }

                TotalChargeAmount += charge.getAmount().doubleValue();
                DXPointerDB = null;
                DXPointerEDI = null;
                Count++;
            }


        }

        TotalChargeAmountStr = String.valueOf(String.format("%.2f", TotalChargeAmount));

        Loop loop_se = loop_gs.addChild("SE");
        loop_se.addSegment("SE*XX*" + InterControlNo);
        Loop loop_ge = loop_isa.addChild("GE");
        loop_ge.addSegment("GE*1*" + InterControlNo);
        Loop loop_iea = x12.addChild("IEA");
        loop_iea.addSegment("IEA*1*" + InterControlNo);
        Integer count = loop_st.size();
        count += 1;

//        /TRAILER
        List<Loop> trailer = x12.findLoop("SE");
        trailer.get(0).getSegment(0).setElement(1, count.toString(), count.toString().length());

        //another way
        List<Segment> se = x12.findSegment("SE");
        se.get(0).setElement(1, count.toString(), count.toString().length());

        //another way
        loop_se.getSegment(0).setElement(1, count.toString(), count.toString().length());


        ////system.out.println(loop_st.size());
        System.out.println(x12.toString().toUpperCase());
        ////system.out.println(x12.toXML());

        if (ErrorMsgs.size() == 0) {
            String FILENAME = "EDI_Prof_" + PatientName + "_" + PatientRegId + VisitId + "_" + ClaimNumber + ".txt";


            FileWriter myWriter = new FileWriter(FILENAME);
            myWriter.write(x12.toString().toUpperCase());
            myWriter.close();

            claim.setTimesSubmitted(Integer.valueOf(timesSubmitted));
            claiminfomasterRepository.updateTimesSubmitted(PatientControlNumber, FILENAME, userid, ClaimNumber, timesSubmitted, InterControlNo);

        } else {
            rulesList = claimServiceScrubberImpl.gettingRulesFormatted(claim, rulesList, ErrorMsgs);
        }


        return rulesList;
    }

    @Transactional
    @Override
    public Object createEDI_Inst(Integer claimId) throws ParseException, IOException {
        Claiminfomaster claim = claiminfomasterRepository.findById(claimId).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));
        List<String> ErrorMsgs = new ArrayList<>();
        List<ScrubberRulesDto> rulesList = new ArrayList<>();

        if (claim.getScrubbed() == 0) {
            return claimServiceScrubberImpl.gettingRulesFormatted(claim, rulesList, ErrorMsgs);
        }

        String PatientName = "";
        String PatientRegId = "";
        String VisitId = "";
        String ClaimNumber = claim.getClaimNumber();
        String DirectoryName = "";
        String PriInsuranceName = "";
        String PriInsuranceNameId = "";
        String timesSubmitted = String.valueOf(claim.getTimesSubmitted() == null ? "1" : claim.getTimesSubmitted() + 1);
        String PriFillingIndicator = "";
        String OrigClaim = "";
        String GrpNumber = "";
        String MemId = "";

        String Payer_Address = "";
        String Payer_City = "";
        String Payer_State = "";
        String Payer_Zip = "";
        String ClaimCreateDate = "";
        String ClaimCreateTime = "";
        String InterControlNo = "";


        String CreationDate = claim.getCreatedDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        ClaimCreateDate = claim.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        ClaimCreateTime = claim.getCreatedDate().format(DateTimeFormatter.ofPattern("Hmm"));
        String ClaimType = "";


        String BillingProviderLastName = "";
        String BillingProviderFirstName = "";
        String BillingProviderNPI = "";
        String BillingProvider_Taxonomy = "";
        String BillingProvider_Tel = "";
        String BillingProvider_ETIN = "";
        DoctorDTO BillingProvidersDetailsById = null;


        String ReferringProviderLastName = "";
        String ReferringProviderFirstName = "";
        String ReferringProviderNPI = "";
        String ReferringProvider_Taxonomy = "";
        DoctorDTO ReferringProviderDetailsById = null;


        String SupervisingProviderLastName = "";
        String SupervisingProviderFirstName = "";
        String SupervisingProviderNPI = "";
        String SupervisingProvider_Taxonomy = "";

        String RenderingProvidersLastName = "";
        String RenderingProvidersFirstName = "";
        String RenderingProvidersNPI = "";
        String RenderingProviders_Taxonomy = "";
        DoctorDTO RenderingProviderDetailsById = null;

        String AttendingProviderLastName = "";
        String AttendingProviderFirstName = "";
        String AttendingProviderNPI = "";
        String AttendingProvider_Taxonomy = "";
        DoctorDTO AttendingProviderDetailsById = null;


        String OrderingProvidersLastName = "";
        String OrderingProvidersFirstName = "";
        String OrderingProvidersNPI = "";
        String OrderingProviders_Taxonomy = "";
        DoctorDTO OrderingProviderDetailsById = null;


        String OperatingProviderLastName = "";
        String OperatingProviderFirstName = "";
        String OperatingProviderNPI = "";
        String OperatingProvider_Taxonomy = "";


        String Organization_Address = null;
        String Organization_City = null;
        String Organization_State = null;
        String Organization_ZipCode = null;

        String Client_FullName = "";
        String ClientAddress = "";
        String ClientCity = "";
        String ClientTaxID = "";
        String ClientState = "";
        String ClientZipCode = "";
        String ClientPhone = "";
        String ClientNPI = "";
        String TaxanomySpecialty = "";

        String PatientRelationshipCode = "";
        String Related_Causes_Code = "";
        String PayerResponsibilityCode = "P";

        String PatientFirstName = "";
        String SSN = "";
        String PatientLastName = "";
        String DOS = "";
        String DOB = "";
        String _DOB = "";
        String Gender = "";
        String PriInsuredName = "";
        String PriInsuredLastName = "";
        String PriInsuredFirstName = "";
        String PatientRelationtoPrimary = "";
        String IndividualRelationshipCode = "";
        String PatientRelationtoSec = "";


        String SecInsuredName = "";
        String SecInsuredLastName = "";
        String SecInsuredFirstName = "";

        String Address = "";
        String City = "";
        String State = "";
        String StateCode = "";
        String ZipCode = "";
        String PatientControlNumber = "";
        String userid = "";

        String TypeBillText = claim.getTypeBillText();//"131";
        String StatmentCoverFromDateAddInfo = claim.getClaimadditionalinfo().getStatmentCoverFromDateAddInfo();
        String StatmentCoverToDateAddInfo = claim.getClaimadditionalinfo().getStatmentCoverToDateAddInfo();
        String AdmissionTypeAddInfo = claim.getClaimadditionalinfo().getAdmissionTypeAddInfo();
        String AdmissionSourceAddInfo = claim.getClaimadditionalinfo().getAdmissionSourceAddInfo();
        String PatientStatusAddInfo = claim.getClaimadditionalinfo().getPatientStatusAddInfo();


        String PrincipalDiagInfoCodes = claim.getClaiminformationcode().getPrincipalDiagInfoCodes();
        String POAInfoCodes = claim.getClaiminformationcode().getPOAInfoCodes();
        String AdmittingDiagInfoCodes = claim.getClaiminformationcode().getAdmittingDiagInfoCodes();

        StringBuilder CodeReasVisit = new StringBuilder();
        StringBuilder CodeExtCauseInj = new StringBuilder();
        StringBuilder CodeOthDiag = new StringBuilder();
        StringBuilder CodeOthProcedure = new StringBuilder();
        StringBuilder CodeOccSpan = new StringBuilder();
        StringBuilder InfoOccurance = new StringBuilder();
        StringBuilder CodeValueCode = new StringBuilder();
        StringBuilder CodeConditionCode = new StringBuilder();


        String frequencyCode = claim.getFreq();
        Integer ClientId = claim.getClientId();
        String CLIA_number = "";

        try {
            CompanyDTO companyDetailsById = externalService.getCompanyDetailsById(1);
            Organization_Address = companyDetailsById.getAddress();
            Organization_City = companyDetailsById.getCity();
            Organization_State = companyDetailsById.getState();
            Organization_ZipCode = companyDetailsById.getZipCode();
        } catch (Exception e) {
            ErrorMsgs.add("EXCEPTION in getting company details : " + new RuntimeException(e).getMessage());
            return ErrorMsgs;
        }

        if (!isEmpty(claim.getRenderingProvider())) {
            try {
                RenderingProviderDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getRenderingProvider()));

                RenderingProvidersLastName = RenderingProviderDetailsById.getDoctorsLastName().toUpperCase();
                RenderingProvidersFirstName = RenderingProviderDetailsById.getDoctorsFirstName().toUpperCase();
                RenderingProvidersNPI = RenderingProviderDetailsById.getNpi();
                RenderingProviders_Taxonomy = RenderingProviderDetailsById.getTaxonomySpecialty();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting rendering provider details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;

            }

        }

        if (!isEmpty(claim.getBillingProviders())) {
            try {
                BillingProvidersDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getBillingProviders()));

                BillingProviderLastName = BillingProvidersDetailsById.getDoctorsLastName().toUpperCase();
                BillingProviderFirstName = BillingProvidersDetailsById.getDoctorsFirstName().toUpperCase();
                BillingProviderNPI = BillingProvidersDetailsById.getNpi();
                BillingProvider_Taxonomy = BillingProvidersDetailsById.getTaxonomySpecialty();
                BillingProvider_Tel = BillingProvidersDetailsById.getEtin();
                BillingProvider_ETIN = BillingProvidersDetailsById.getPhoneNumber();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting billing provider details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }

        }

        if (!isEmpty(claim.getReferringProvider())) {
            try {
                ReferringProviderDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getReferringProvider()));

                ReferringProviderLastName = ReferringProviderDetailsById.getDoctorsLastName().toUpperCase();
                ReferringProviderFirstName = ReferringProviderDetailsById.getDoctorsFirstName().toUpperCase();
                ReferringProviderNPI = ReferringProviderDetailsById.getNpi();
                ReferringProvider_Taxonomy = ReferringProviderDetailsById.getTaxonomySpecialty();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting referring provider details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;

            }
        }

        if (!isEmpty(claim.getOrderingProvider())) {
            try {
                OrderingProviderDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getOrderingProvider()));

                OrderingProvidersLastName = OrderingProviderDetailsById.getDoctorsLastName().toUpperCase();
                OrderingProvidersFirstName = OrderingProviderDetailsById.getDoctorsFirstName().toUpperCase();
                OrderingProvidersNPI = OrderingProviderDetailsById.getNpi();
                OrderingProviders_Taxonomy = OrderingProviderDetailsById.getTaxonomySpecialty();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting ordering provider details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }

        String ServiceDate = "";


        PatientControlNumber = "FAM" + ClientId + timesSubmitted + "E" + ClaimNumber.replace("-", "");


        StringBuilder LX_SV1 = new StringBuilder();

        if (!isEmpty(claim.getClientId())) {
            try {
                ClientDTO clientDetailsById = externalService.getClientDetailsById(claim.getClientId());
                ClientAddress = clientDetailsById.getAddress();
                ClientCity = clientDetailsById.getCity();
                ClientTaxID = clientDetailsById.getTaxid();
                ClientState = clientDetailsById.getState();
                ClientZipCode = clientDetailsById.getZipCode();
                ClientPhone = clientDetailsById.getPhone();
                ClientNPI = clientDetailsById.getNpi();
                TaxanomySpecialty = clientDetailsById.getTaxanomySpecialty();
                Client_FullName = clientDetailsById.getFullName();
            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting client details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }

        if (
                (!isEmpty(claim.getPatientRegId())
                        && !isEmpty(claim.getVisitId())
                        && !isEmpty(claim.getPriInsuranceNameId()))
                        || !isEmpty(claim.getSecondaryInsuranceId())
        ) {
            try {

                PatientReqDto patientReqDto = new PatientReqDto();
                patientReqDto.setPatientRegId(claim.getPatientRegId() == null ? null : Long.valueOf(claim.getPatientRegId()));
                patientReqDto.setVisitId(claim.getVisitId() == null ? null : Long.valueOf(claim.getVisitId()));
                patientReqDto.setPrimaryInsuranceId(1L);//claim.getPriInsuranceNameId() == null ? null : Long.valueOf(claim.getPriInsuranceNameId()));
                patientReqDto.setSecondaryInsuranceId(claim.getSecondaryInsuranceId() == null ? null : Long.valueOf(claim.getSecondaryInsuranceId()));

                PatientDto patientDetailsById = externalService.getPatientDetailsById(patientReqDto);
                DateTimeFormatter DOBformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter ClaimCreateDateAndDOSformatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                PatientFirstName = patientDetailsById.getFirstName().toUpperCase();
                SSN = patientDetailsById.getSSN();
                PatientLastName = patientDetailsById.getLastName().toUpperCase();
                DOB = String.valueOf(patientDetailsById.getDOB().format(ClaimCreateDateAndDOSformatter));
                _DOB = String.valueOf(patientDetailsById.getDOB().format(DOBformatter));
                DOS = patientDetailsById.getDOS().format(ClaimCreateDateAndDOSformatter);
                Gender = patientDetailsById.getGender();
                Address = patientDetailsById.getAddress();
                City = patientDetailsById.getCity();
                State = patientDetailsById.getState();
                ZipCode = patientDetailsById.getZipCode();
                PriInsuredFirstName = patientDetailsById.getPriInsurerFirstName();
                PriInsuredLastName = patientDetailsById.getPriInsurerLastName();
                PatientRelationtoPrimary = patientDetailsById.getPatientRelationtoPrimary();
                SecInsuredFirstName = patientDetailsById.getSecInsurerFirstName();
                SecInsuredLastName = patientDetailsById.getSecInsurerLastName();
                PatientRelationtoSec = patientDetailsById.getPatientRelationshiptoSecondry();
                MemId = claim.getMemId();
                GrpNumber = claim.getGrpNumber();

            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting patient details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }


        InterControlNo = claiminfomasterRepository.getInterControlNo();
        InterControlNo = InterControlNo.equals("") ? "000000001" : String.format("%09d", Integer.parseInt(InterControlNo) + 1);
        String TotalChargeAmountStr = String.valueOf(claim.getTotalCharges());

        Context c = new Context('~', '*', ':');
        HashMap<String, Integer> ISATAG = new HashMap<String, Integer>();
        int ClaimTypes = 1; //1 prof , 2 inst
        int HLCounter = 1;
        String ClaimTypeIdentifier = "005010X223A2";// 005010X222A1 Prof , 005010X223A2 Inst

        ISATAG.put("0000-ISA01-AuthorizationInfoQualifier-00", 2);
        ISATAG.put("0000-ISA02-AuthorizationInformation-", 10);
        ISATAG.put("0000-ISA03-SecurityInformationQualifier-00", 2);
        ISATAG.put("0000-ISA04-SecurityInformation-          ", 10);
        ISATAG.put("0000-ISA05-Interchange ID Qualifier-ZZ", 2);
        ISATAG.put("0000-ISA06-Interchange Sender ID (*)-BS01834        ", 15);
        ISATAG.put("0000-ISA07-Interchange ID Qualifier-ZZ", 2);
        ISATAG.put("0000-ISA08-Interchange Receiver ID-33477          ", 15);
        ISATAG.put("0000-ISA09-InterchangeDate-140205", 6);
        ISATAG.put("0000-ISA10-InterchangeTime-1452", 4);
        ISATAG.put("0000-ISA11-RepetitionSeparator-^", 1);
        ISATAG.put("0000-ISA12-InterchangeControlVersionNumber-00501", 5);
        ISATAG.put("0000-ISA13-InterchangeControlNumber-100000467", 9);
        ISATAG.put("0000-ISA14-AcknowledgmentRequested-0", 1);
        ISATAG.put("0000-ISA15-Usage Indicator-T", 1);
        ISATAG.put("0000-ISA16-ComponentElementSeparator-:~", 1);


        HashMap<Integer, String> ISAValue = new HashMap<Integer, String>();

        ISAValue.put(0, "ISA");
        ISAValue.put(1, "00");
        ISAValue.put(2, " ");
        ISAValue.put(3, "00");
        ISAValue.put(4, " ");
        ISAValue.put(5, "ZZ");
//                ISAValue.put(6, "BS01834");
        ISAValue.put(6, "AV09311993");
//                ISAValue.put(7, "ZZ");
        ISAValue.put(7, "01");
//                ISAValue.put(8, "33477");
        ISAValue.put(8, "030240928");
        ISAValue.put(9, "140205");
        ISAValue.put(10, "1452");
        ISAValue.put(11, "^");
        ISAValue.put(12, "00501");
//                ISAValue.put(13, "000987654");
        ISAValue.put(13, InterControlNo);
        ISAValue.put(14, "0");
        ISAValue.put(15, "P");
        ISAValue.put(16, ":");

        X12 x12 = new X12(c);
        Loop loop_isa = x12.addChild("ISA");

        // add segment
        loop_isa.addSegment("ISA*00*          *00*          *ZZ*SENDERID       *ZZ*RECEIVERID    *" + CreationDate + "*" + ClaimCreateTime + "*U*00401*" + InterControlNo + "*0*P*:");

        for (String key : ISATAG.keySet()) {
            //   //system.out.println(key);
            String temp[] = key.split("-");
            int elementid = Integer.parseInt(getOnlyDigits(temp[1]));
            int len = ISATAG.get(key);
            // //system.out.println(elementid);
            // //system.out.println(len);
            loop_isa.getSegment(0).setElement(elementid, ISAValue.get(elementid), len);
        }


        Loop loop_gs = loop_isa.addChild("GS");
        // Add GS segment directly as a string
        loop_gs.addSegment("GS*HC*AV09311993*030240928*" + ClaimCreateDate + "*" + ClaimCreateTime + "*" + InterControlNo + "*X*" + ClaimTypeIdentifier);

        Loop loop_st = loop_gs.addChild("ST");
        loop_st.addSegment("ST*837*" + InterControlNo + "*" + ClaimTypeIdentifier);
        loop_st.addSegment("BHT*0019*00*" + ClaimCreateDate + ClaimCreateTime + "*" + ClaimCreateDate + "*" + ClaimCreateTime + "*CH");

        //1000A SUBMITTER NAME
        Loop loop_1000A = loop_st.addChild("1000A");

        loop_1000A.addSegment("NM1*41*2*" + BillingProviderFirstName + " " + BillingProviderLastName + "*****46*" + BillingProvider_ETIN);//Organization ID to be included later
        loop_1000A.addSegment("PER*IC*" + BillingProviderFirstName + " " + BillingProviderLastName + "*TE*" + BillingProvider_Tel);


//        if (isEmpty(Organization))
//            ErrorMsgs.add("<b>Organization</b> is <b>Missing</b>");
//        if (isEmpty(Organization_Tel))
//            ErrorMsgs.add("<b>Organization Telephone</b> is <b>Missing</b>");
//        if (isEmpty(Organization_Email))
//            ErrorMsgs.add("<b>Organization Email</b> is <b>Missing</b>");

        //1000B RECEIVER NAME
        Loop loop_1000B = loop_st.addChild("1000B");
        String RecieverInsurance = "MEDICARE";
//                    String RecieverInsurance = "";
        String RecieverInsurance_IdentificationCode = "00120";
//                    String RecieverInsurance_IdentificationCode = "";

        if (isEmpty(RecieverInsurance))
            ErrorMsgs.add("<b>Insurance</b> is <b>Missing</b>");
        if (isEmpty(RecieverInsurance_IdentificationCode))
            ErrorMsgs.add("<b>Insurance ID </b> is <b>Missing</b>");

        PriInsuranceName = claim.getPriInsuranceNameId();
        InsuranceDTO PrimaryinsuranceDetailsById = externalService.getInsuranceDetailsById(String.valueOf(902));
        PriInsuranceName = !isEmpty(PriInsuranceName) ? PrimaryinsuranceDetailsById.getPayerName().replace("Servicing States", "").replaceAll(":", " ").trim().replaceAll("&", "").replaceAll("\n", " ").replaceAll(" +", " ") : null;
        PriInsuranceNameId = PrimaryinsuranceDetailsById.getPayerID();
        PriFillingIndicator = PrimaryinsuranceDetailsById.getClaimIndicator_P();
        Payer_Address = PrimaryinsuranceDetailsById.getAddress();
        Payer_City = PrimaryinsuranceDetailsById.getCity();
        Payer_State = PrimaryinsuranceDetailsById.getState();
        Payer_Zip = PrimaryinsuranceDetailsById.getZip();


        PriInsuranceName = PriInsuranceName.length() < 50 ? PriInsuranceName : PriInsuranceName.substring(0, 50).trim();
        loop_1000B.addSegment("NM1*40*2*" + PriInsuranceName + "*****46*" + PriInsuranceNameId);


        Loop loop_2000 = loop_st.addChild("2000");
        loop_2000.addSegment("HL*" + HLCounter + "**20*1"); //HL BILLING PROVIDER HIERARCHICAL LEVEL
        loop_2000.addSegment("PRV*BI*PXC*" + TaxanomySpecialty); //PRV BILLING PROVIDER SPECIALTY

        //2010AA BILLING PROVIDER NAME
        Loop loop_2010AA = loop_2000.addChild("2010");
        loop_2010AA.addSegment("NM1*85*2*" + Client_FullName + "*****XX*" + ClientNPI);//NM1 BILLING PROVIDER NAME INCLUDING NATIONAL PROVIDER ID
        loop_2010AA.addSegment("N3*" + ClientAddress); //N3 BILLING PROVIDER ADDRESS
        loop_2010AA.addSegment("N4*" + ClientCity + "*" + ClientState + "*" + ClientZipCode);//N4 BILLING PROVIDER LOCATION
        loop_2010AA.addSegment("REF*EI*" + ClientTaxID); //REF BILLING PROVIDER TAX IDENTIFICATION NUMBER


        Loop loop_2010AB = loop_2000.addChild("2010");//F&A Address
        loop_2010AB.addSegment("NM1*87*2");//Pay To Name
        loop_2010AB.addSegment("N3*" + Organization_Address);//Pay To Address
        loop_2010AB.addSegment("N4*" + Organization_City + "*" + Organization_State + "*" + Organization_ZipCode);//Pay To Address

        if (isEmpty(Organization_Address)) ErrorMsgs.add("<b>Organization Address</b>  is <b>Missing</b>");
        if (isEmpty(Organization_City)) ErrorMsgs.add("<b>Organization City</b>  is <b>Missing</b>");
        if (isEmpty(Organization_State)) ErrorMsgs.add("<b>Organization State</b>  is <b>Missing</b>");
        if (isEmpty(Organization_ZipCode)) ErrorMsgs.add("<b>Organization Zipcode</b>  is <b>Missing</b>");

        HLCounter++;
        //2000B SUBSCRIBER HL LOOP
        Loop loop_2000B = loop_2000.addChild("2000");
//                    loop_2000B.addSegment("HL*" + HLCounter + "*1*22*0");//HL SUBSCRIBER HIERARCHICAL LEVEL

        if (PatientRelationshipCode.equals("18")) {
            loop_2000B.addSegment("HL*" + HLCounter + "*1*22*0");//HL SUBSCRIBER HIERARCHICAL LEVEL
        } else {
            loop_2000B.addSegment("HL*" + HLCounter + "*1*22*1");//HL SUBSCRIBER HIERARCHICAL LEVEL
        }

        if (PatientRelationtoPrimary.equalsIgnoreCase("Self")) {
            PatientRelationshipCode = "18";
        } else {
            PatientRelationshipCode = "";
        }

        loop_2000B.addSegment("SBR*" + PayerResponsibilityCode + "*" + PatientRelationshipCode + "*" + GrpNumber + "******" + PriFillingIndicator);//SBR SUBSCRIBER INFORMATION

        //2010BA SUBSCRIBER NAME LOOP
        Loop loop_2010BA = loop_2000.addChild("2000");

        if (PatientRelationshipCode.equals("18")) {
            loop_2010BA.addSegment("NM1*IL*1*" + PatientLastName + "*" + PatientFirstName + "*T***MI*" + MemId);//NM1 SUBSCRIBER NAME
            loop_2010BA.addSegment("N3*" + Address);//N3 SUBSCRIBER ADDRESS
            loop_2010BA.addSegment("N4*" + City + "*" + State + "*" + ZipCode);//N4 SUBSCRIBER LOCATION
            loop_2010BA.addSegment("DMG*D8*" + DOB + "*" + Gender); //DMG SUBSCRIBER DEMOGRAPHIC INFORMATION
        } else {
            loop_2010BA.addSegment("NM1*IL*1*" + PriInsuredLastName + "*" + PriInsuredFirstName + "*T***MI*" + MemId);//NM1 SUBSCRIBER NAME
        }

        //2010BB PAYER NAME LOOP
        Loop loop_2010BB = loop_2000.addChild("2000");
        loop_2010BB.addSegment("NM1*PR*2*" + PriInsuranceName + "*****PI*" + PriInsuranceNameId);//NM1 PAYER NAME **PAYER_ID
        loop_2010BB.addSegment("N3*" + Payer_Address);//N3 PAYER ADDRESS ** ADDED
        loop_2010BB.addSegment("N4*" + Payer_City + "*" + Payer_State + "*" + Payer_Zip);//N4 PAYER ADDRESS ** ADDED
//                    loop_2010BB.addSegment("REF*G2*330127");//REF BILLING PROVIDER SECONDARY IDENTIFICATION SITUATIONAL

        if (PatientRelationshipCode.compareTo("18") != 0) {
            HLCounter++;
            Loop loop_2000C = loop_st.addChild("2000");
            loop_2000C.addSegment("HL*" + HLCounter + "*2*23*0");
            loop_2000C.addSegment("PAT*" + IndividualRelationshipCode);
            loop_2000C.addSegment("NM1*QC*1*" + PatientLastName + "*" + PatientFirstName);
            loop_2000C.addSegment("N3*" + Address);
            loop_2000C.addSegment("N4*" + City + "*" + State + "*" + ZipCode);
            loop_2000C.addSegment("DMG*D8*" + DOB + "*" + Gender);
        }

        Loop loop_2300 = loop_st.addChild("2300");

        loop_2300.addSegment("CLM*" + PatientControlNumber + "*" + TotalChargeAmountStr + "***" + TypeBillText.substring(0, 1) + TypeBillText.substring(1, 2) + ":A:" + TypeBillText.substring(2, 3) + "**A*Y*Y***"); //CLM CLAIM LEVEL INFORMATION
        loop_2300.addSegment("DTP*434*RD8*" + StatmentCoverFromDateAddInfo + "-" + StatmentCoverToDateAddInfo);
        loop_2300.addSegment("CL1*" + AdmissionTypeAddInfo + "*" + AdmissionSourceAddInfo + "*" + PatientStatusAddInfo);
        loop_2300.addSegment("HI*ABK:" + PrincipalDiagInfoCodes.replace(".", "") + ":::::::" + POAInfoCodes);

        if (claim.getClaiminfocodereasvisit().size() > 0) {
            claim.getClaiminfocodereasvisit().stream().filter(x -> x.getStatus() == 1).forEach(x -> CodeReasVisit.append("*APR:" + x.getCode().replace(".", "")));
            loop_2300.addSegment("HI" + CodeReasVisit);
        }
        if (claim.getClaiminfocodeextcauseinj().size() > 0) {
            claim.getClaiminfocodeextcauseinj().stream().filter(x -> x.getStatus() == 1).forEach(x -> CodeExtCauseInj.append("*ABN:" + x.getCode().replace(".", "")));
            loop_2300.addSegment("HI" + CodeExtCauseInj);
        }
        if (claim.getClaiminfocodeothdiag().size() > 0) {
            claim.getClaiminfocodeothdiag().stream().filter(x -> x.getStatus() == 1).forEach(x -> CodeOthDiag.append("*ABF:" + x.getCode().replace(".", "")));
            loop_2300.addSegment("HI" + CodeOthDiag);
        }
        if (claim.getClaiminfocodeothprocedure().size() > 0) {
            claim.getClaiminfocodeothprocedure().stream().filter(x -> x.getStatus() == 1).forEach(x -> CodeOthProcedure.append("*BBQ:" + x.getCode().replace(".", "")));
            loop_2300.addSegment("HI" + CodeOthProcedure);
        }
        if (claim.getClaiminfocodeoccspan().size() > 0) {
            claim.getClaiminfocodeothprocedure().stream().filter(x -> x.getStatus() == 1).forEach(x -> CodeOccSpan.append("*BI:" + x.getCode().replace(".", "")));
            loop_2300.addSegment("HI" + CodeOccSpan);
        }
        if (claim.getClaiminfooccurance().size() > 0) {
            claim.getClaiminfocodeothprocedure().stream().filter(x -> x.getStatus() == 1).forEach(x -> InfoOccurance.append("*BH:" + x.getCode().replace(".", "")));
            loop_2300.addSegment("HI" + InfoOccurance);
        }
        if (claim.getClaiminfocodevaluecode().size() > 0) {
            claim.getClaiminfocodevaluecode().stream().filter(x -> x.getStatus() == 1).forEach(x -> CodeValueCode.append("*BE:" + x.getCode().replace(".", "")));
            loop_2300.addSegment("HI" + CodeValueCode);
        }
        if (claim.getClaiminfocodeconditioncode().size() > 0) {
            claim.getClaiminfocodevaluecode().stream().filter(x -> x.getStatus() == 1).forEach(x -> CodeConditionCode.append("*BG:" + x.getCode().replace(".", "")));
            loop_2300.addSegment("HI" + CodeConditionCode);
        }


        //2310A ATTENDING PROVIDER NAME
        if (!isEmpty(AttendingProviderLastName) && !isEmpty(AttendingProviderFirstName)) {
            Loop loop_2310A = loop_2300.addChild("2300");
            loop_2310A.addSegment("NM1*71*1*" + AttendingProviderLastName + "*" + AttendingProviderFirstName + "****XX*" + AttendingProviderNPI);//NM1 ATTENDING PROVIDER
            loop_2310A.addSegment("PRV*AT*PXC*" + AttendingProvider_Taxonomy);//NM1 ATTENDING PROVIDER
        }

        if (!isEmpty(OperatingProviderLastName) && !isEmpty(OperatingProviderFirstName)) {
            Loop loop_2310B = loop_2300.addChild("2300");
            loop_2310B.addSegment("NM1*72*1*" + OperatingProviderLastName + "*" + OperatingProviderFirstName + "****XX*" + OperatingProviderNPI);//NM1 ATTENDING PROVIDER
//                    loop_2310B.addSegment("PRV*AT*PXC*" + OperatingProvider_Taxonomy);//NM1 Operating PROVIDER
        }


        if (!isEmpty(RenderingProvidersFirstName)) {
            // 2310B RENDERING PROVIDER NAME
            Loop loop_2310D = loop_2300.addChild("2300");
            loop_2310D.addSegment("NM1*82*1*" + RenderingProvidersLastName + "*" + RenderingProvidersFirstName + "****XX*" + RenderingProvidersNPI);//NM1 Rendering PROVIDER
            loop_2310D.addSegment("PRV*PE*PXC*" + RenderingProviders_Taxonomy); //PRV Rendering Provider SPECIALTY
        }

        if (!isEmpty(ReferringProviderFirstName)) {
            Loop loop_2310F = loop_2300.addChild("2300");
            loop_2310F.addSegment("NM1*DN*1*" + ReferringProviderLastName + "*" + ReferringProviderFirstName + "****XX*" + ReferringProviderNPI);//NM1 Referring PROVIDER
            loop_2310F.addSegment("REF*G2*B99937");//REF Referring PROVIDER SECONDARY IDENTIFICATION
        }


        int Count = 1;
        Loop loop_2400 = loop_st.addChild("2400");
        String Units = null;
        String mod1 = null;
        String mod2 = null;
        String mod3 = null;
        String mod4 = null;
        String ProcedureCode = "";
        String RevCode = "";
        String DescriptionFrom = "";
        String Amount = "";
        String MeasurementCode = "";
        SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd");
        Double TotalChargeAmount = 0.00;

        for (Claimchargesinfo charge : claim.getClaimchargesinfo()) {
            if (charge.getStatus() == 1) {
                mod1 = charge.getMod1();
                mod2 = charge.getMod2();
                mod3 = charge.getMod3();
                mod4 = charge.getMod4();
                ProcedureCode = charge.getHCPCSProcedure();
                RevCode = charge.getRevCode();
                Amount = String.valueOf(charge.getAmount());
                Units = String.valueOf(charge.getUnits());
                ServiceDate = charge.getServiceDate();


                String modifier = "";
                if (!isEmpty(mod1)) {//mod1
                    modifier += ":" + mod1;
                }
                if (!isEmpty(mod2)) {//mod2
                    modifier += ":" + mod2;
                }
                if (!isEmpty(mod3)) {//mod3
                    modifier += ":" + mod3;
                }
                if (!isEmpty(mod4)) {//mod4
                    modifier += ":" + mod4;
                }

                MeasurementCode = anesthesiacodeRepository.validateAnesthesiaCodes(ProcedureCode) == 1 ? "MJ" : "UN";

                loop_2400.addSegment("LX*" + Count);
                loop_2400.addSegment("SV2*" + RevCode + "*HC:" + ProcedureCode + ":" + mod1 + ":" + mod2 + ":" + mod3 + ":" + mod4 + ":*" + Amount + "*" + MeasurementCode + "*" + Units);//SV2 INSTITUTIONAL SERVICE
                loop_2400.addSegment("DTP*472*D8*" + myFormat.format(fromUser.parse(ServiceDate)));


                TotalChargeAmount += charge.getAmount().doubleValue();
                Count++;
            }
        }


        Loop loop_se = loop_gs.addChild("SE");
        loop_se.addSegment("SE*XX*" + InterControlNo);
        Loop loop_ge = loop_isa.addChild("GE");
        loop_ge.addSegment("GE*1*" + InterControlNo);
        Loop loop_iea = x12.addChild("IEA");
        loop_iea.addSegment("IEA*1*" + InterControlNo);
        Integer count = loop_st.size();
        count += 1;

//        /TRAILER
        List<Loop> trailer = x12.findLoop("SE");
        trailer.get(0).getSegment(0).setElement(1, count.toString(), count.toString().length());

        //another way
        List<Segment> se = x12.findSegment("SE");
        se.get(0).setElement(1, count.toString(), count.toString().length());

        //another way
        loop_se.getSegment(0).setElement(1, count.toString(), count.toString().length());


        ////system.out.println(loop_st.size());
        System.out.println(x12.toString().toUpperCase());
        ////system.out.println(x12.toXML());

        if (ErrorMsgs.size() == 0) {
            String FILENAME = "EDI_INST_" + PatientName + "_" + PatientRegId + VisitId + "_" + ClaimNumber + ".txt";


            FileWriter myWriter = new FileWriter(FILENAME);
            myWriter.write(x12.toString().toUpperCase());
            myWriter.close();

//                claim.setTimesSubmitted(Integer.valueOf(timesSubmitted));
            claiminfomasterRepository.updateTimesSubmitted(PatientControlNumber, FILENAME, userid, ClaimNumber, timesSubmitted, InterControlNo);

            return null;
        } else {
            rulesList = claimServiceScrubberImpl.gettingRulesFormatted(claim, rulesList, ErrorMsgs);
        }


        return rulesList;
    }

}
