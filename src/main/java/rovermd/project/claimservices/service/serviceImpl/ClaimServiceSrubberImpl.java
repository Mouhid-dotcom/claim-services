package rovermd.project.claimservices.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.dto.*;
import rovermd.project.claimservices.entity.claimMaster.ClaimAudittrail;
import rovermd.project.claimservices.entity.claimMaster.Claiminfomaster;
import rovermd.project.claimservices.repos.CPTRepository;
import rovermd.project.claimservices.repos.ClaimAudittrailRepository;
import rovermd.project.claimservices.repos.ICDRepository;
import rovermd.project.claimservices.repos.RevenueCodeRepository;
import rovermd.project.claimservices.repos.scrubber.*;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimServiceSrubber;
import rovermd.project.claimservices.service.ExternalService;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static rovermd.project.claimservices.util.UtilityHelper.isEmpty;
import static rovermd.project.claimservices.util.UtilityHelper.isInValidDate;

@Service
public class ClaimServiceSrubberImpl implements ClaimServiceSrubber {

    @Autowired
    private ICDRepository iCDRepository;

    @Autowired
    private WpctaxonomyRepository wpctaxonomyRepository;

    @Autowired
    private ZipcodefacilityRepository zipcodefacilityRepository;

    @Autowired
    private ZipcodelibraryRepository zipcodelibraryRepository;

    @Autowired
    private OrderreferringRepository orderreferringRepository;

    @Autowired
    private EnmprocedureRepository enmprocedureRepository;

    @Autowired
    private EnmNotallowedModifierRepository enmNotallowedModifierRepository;

    @Autowired
    private AnesthesiacodeRepository anesthesiacodeRepository;

    @Autowired
    private AnesthesiamodifierRepository anesthesiamodifierRepository;

    @Autowired
    private MajorsurgerycodeRepository majorsurgerycodeRepository;

    @Autowired
    private EnmsurgerycodeRepository enmsurgerycodeRepository;

    @Autowired
    private ConsultantprocedureRepository consultantprocedureRepository;

    @Autowired
    private EspdtprocedureRepository espdtprocedureRepository;

    @Autowired
    private CliaRepository cliaRepository;

    @Autowired
    private DmeHcpcRepository dmeHcpcRepository;

    @Autowired
    private VaccinecodescomponentRepository vaccinecodescomponentRepository;


    @Autowired
    private NoofunitshcpcRepository noofunitshcpcRepository;

    @Autowired
    private PqrscodeRepository pqrscodeRepository;

    @Autowired
    private NdcCptCrosswalkRepository ndcCptCrosswalkRepository;

    @Autowired
    private CPTRepository cPTRepository;

    @Autowired
    private IcodeRepository icodeRepository;

    @Autowired
    private McrvaccinerulecodeRepository mcrvaccinerulecodeRepository;

    @Autowired
    private AddoncodeRepository addoncodeRepository;

    @Autowired
    private CciNotAllowedRepository cciNotAllowedRepository;

    @Autowired
    private GenderspecificdiagnosisRepository genderspecificdiagnosisRepository;

    @Autowired
    private GenderspecificcptIcdRepository genderspecificcptIcdRepository;

    @Autowired
    private AgewiseicdRepository agewiseicdRepository;

    @Autowired
    private NcdCptGroupRepository ncdCptGroupRepository;

    @Autowired
    private ArticleXHcpcCodeRepository articleXHcpcCodeRepository;

    @Autowired
    private CciModMustRepository cciModMustRepository;

    @Autowired
    private ExternalService externalService;
    @Autowired
    private ClaimAudittrailRepository claimAudittrailRepository;

    @Autowired
    private ClaimAudittrailService claimAudittrailService;
    @Autowired
    private RevenueCodeRepository revenueCodeRepository;

    @Autowired
    private ClaimServiceProfessionalImpl claimServiceProf;
    @Autowired
    private ClaimServiceInstitutionalImpl claimServiceInst;

    @Transactional
    @Override
    public List<?> scrubberProf(Claiminfomaster claim, int... changeDetected) {
        Instant start = Instant.now();
        List<ScrubberRulesDto> rulesList = new ArrayList<>();
        List<String> ErrorMsgs = new ArrayList<>();

        if (changeDetected.length==0 || changeDetected[0] == 0) {
            rulesList = gettingRulesFormatted(claim, rulesList, ErrorMsgs);
            if (rulesList.size() > 0) return rulesList;
        }

        claim = claimServiceProf.filterChargesWrtStatus(claim);

        String ReasonVisit = "";
        String PatientFirstName = "";
        String SSN = "";
        String PatientLastName = "";
        String DOB = "";
        String _DOB = "";
        String Gender = "";
        String City = "";
        String State = "";
        String StateCode = "";
        String ZipCode = "";
        int SelfPayChk = 0;
        String ClaimCreateDate = "";


        String Freq = "";
        String OrigClaim = "";

        String PriFillingIndicator = "";
        String SecFillingIndicator = "";

        String PriInsuranceName = "";
        String PriInsuranceNameId = "";
        String MemId = "";
        String PolicyType = "";
        String GrpNumber = "";
        String SecondaryInsurance = "";
        String SecondaryInsuranceId = "";
        String SecondaryInsuranceMemId = "";
        String SecondaryInsuranceGrpNumber = "";


        String ClientAddress = "";
        String ClientCity = "";
        String ClientTaxID = "";
        String ClientState = "";
        String ClientZipCode = "";
        String ClientPhone = "";
        String ClientNPI = "";
        String CLIA_number = "";
        String TaxanomySpecialty = "";


        String Payer_Address = "";
        String Payer_City = "";
        String Payer_State = "";
        String Payer_Zip = "";


        String PatientRelationtoPrimary = "";
        String PatientRelationtoSec = "";

        String PriInsuredName = "";
        String PriInsuredLastName = "";
        String PriInsuredFirstName = "";

        String SecInsuredName = "";
        String SecInsuredLastName = "";
        String SecInsuredFirstName = "";

        String DOS = "";


        String BillingProviderLastName = "";
        String BillingProviderFirstName = "";
        String BillingProviderNPI = "";
        String BillingProvider_Taxonomy = "";
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

        String ICDA = claim.getClaimchargesinfo().get(0).getIcda();
        String ICDB = claim.getClaimchargesinfo().get(0).getIcdb();
        String ICDC = claim.getClaimchargesinfo().get(0).getIcdc();
        String ICDD = claim.getClaimchargesinfo().get(0).getIcdd();
        String ICDE = claim.getClaimchargesinfo().get(0).getIcde();
        String ICDF = claim.getClaimchargesinfo().get(0).getIcdf();
        String ICDG = claim.getClaimchargesinfo().get(0).getIcdg();
        String ICDH = claim.getClaimchargesinfo().get(0).getIcdh();
        String ICDI = claim.getClaimchargesinfo().get(0).getIcdi();
        String ICDJ = claim.getClaimchargesinfo().get(0).getIcdj();
        String ICDK = claim.getClaimchargesinfo().get(0).getIcdk();
        String ICDL = claim.getClaimchargesinfo().get(0).getIcdl();

        String[] Taxonomy = {"213ES0131X", "213EG0000X", "213EP1101X", "213EP0504X", "213ER0200X", "213ES0000X"};
        List<String> TaxonomyList = Arrays.asList(Taxonomy);
        String[] Taxonomy_for_InitialTreatment = {"111N00000X", "111NI0013X", "111NI0900X", "111NN0400X", "111NN1001X",
                "111NP0017X", "111NR0200X", "111NR0400X", "111NS0005X", "111NT0100X", "111NX0100X", "111NX0800X"};
        List<String> Taxonomy_for_InitialTreatment_List = Arrays.asList(Taxonomy_for_InitialTreatment);

        ArrayList<String> ICDs = new ArrayList<>();

        try {
            if (!isEmpty(claim.getClientId())) {
                ClientDTO clientDetailsById = externalService.getClientDetailsById(claim.getClientId());
                ClientAddress = clientDetailsById.getAddress();
                ClientCity = clientDetailsById.getCity();
                ClientTaxID = clientDetailsById.getTaxid();
                ClientState = clientDetailsById.getState();
                ClientZipCode = clientDetailsById.getZipCode();
                ClientPhone = clientDetailsById.getPhone();
                ClientNPI = clientDetailsById.getNpi();
                TaxanomySpecialty = clientDetailsById.getTaxanomySpecialty();
            }

        } catch (Exception e) {
            ErrorMsgs.add("EXCEPTION in getting client details : " + new RuntimeException(e).getMessage());
            return ErrorMsgs;
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
                City = patientDetailsById.getCity();
                State = patientDetailsById.getState();
                ZipCode = patientDetailsById.getZipCode();
                ClaimCreateDate = String.valueOf(ZonedDateTime.now().format(ClaimCreateDateAndDOSformatter));
//                PriInsuredFirstName = patientDetailsById.getPriInsurerFirstName();
//                PriInsuredLastName = patientDetailsById.getPriInsurerLastName();
                PatientRelationtoPrimary = patientDetailsById.getPatientRelationtoPrimary();
                SecInsuredFirstName = patientDetailsById.getSecInsurerFirstName();
                SecInsuredLastName = patientDetailsById.getSecInsurerLastName();
                PatientRelationtoSec = patientDetailsById.getPatientRelationshiptoSecondry();
                ReasonVisit = patientDetailsById.getReasonVisit();

            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting patient details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }


        if (!isEmpty(claim.getRenderingProvider())) {
            try {
                RenderingProviderDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getRenderingProvider()));

                RenderingProvidersLastName = RenderingProviderDetailsById.getDoctorsLastName().toUpperCase();
                RenderingProvidersFirstName = RenderingProviderDetailsById.getDoctorsFirstName().toUpperCase();
                RenderingProvidersNPI = RenderingProviderDetailsById.getNpi();
                RenderingProviders_Taxonomy = RenderingProviderDetailsById.getTaxonomySpecialty();
            } catch (NumberFormatException e) {
                ErrorMsgs.add("EXCEPTION in getting rendering provider details" + new RuntimeException(e).getMessage());
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
            } catch (NumberFormatException e) {
                ErrorMsgs.add("EXCEPTION in getting billing provider details" + new RuntimeException(e).getMessage());
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
            } catch (NumberFormatException e) {
                ErrorMsgs.add("EXCEPTION in getting referring provider details" + new RuntimeException(e).getMessage());
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
            } catch (NumberFormatException e) {
                ErrorMsgs.add("EXCEPTION in getting ordering provider details " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }


        if (isBMI_ICD(ICDA))
            ErrorMsgs.add("<b>BMI Diagnosis code </b> identified at <b>principal position </b> , the service might be <b>Denied</b> ,  Please change its position\n");


        if (!isEmpty(ICDA)) {
            if (iCDRepository.validateICD(ICDA) == 0) {
                ErrorMsgs.add("<b>ICD A</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDA);
        }
        if (!isEmpty(ICDB)) {
            if (iCDRepository.validateICD(ICDB) == 0) {
                ErrorMsgs.add("<b>ICD B</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDB);
        }
        if (!isEmpty(ICDC)) {
            if (iCDRepository.validateICD(ICDC) == 0) {
                ErrorMsgs.add("<b>ICD C</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDC);
        }
        if (!isEmpty(ICDD)) {
            if (iCDRepository.validateICD(ICDD) == 0) {
                ErrorMsgs.add("<b>ICD D</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDD);
        }
        if (!isEmpty(ICDE)) {
            if (iCDRepository.validateICD(ICDE) == 0) {
                ErrorMsgs.add("<b>ICD E</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDE);
        }
        if (!isEmpty(ICDF)) {
            if (iCDRepository.validateICD(ICDF) == 0) {
                ErrorMsgs.add("<b>ICD F</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDF);
        }
        if (!isEmpty(ICDG)) {
            if (iCDRepository.validateICD(ICDG) == 0) {
                ErrorMsgs.add("<b>ICD G</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDG);
        }
        if (!isEmpty(ICDH)) {
            if (iCDRepository.validateICD(ICDH) == 0) {
                ErrorMsgs.add("<b>ICD H</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDH);
        }
        if (!isEmpty(ICDI)) {
            if (iCDRepository.validateICD(ICDI) == 0) {
                ErrorMsgs.add("<b>ICD I</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDI);
        }
        if (!isEmpty(ICDJ)) {
            if (iCDRepository.validateICD(ICDJ) == 0) {
                ErrorMsgs.add("<b>ICD J</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDJ);
        }
        if (!isEmpty(ICDK)) {
            if (iCDRepository.validateICD(ICDK) == 0) {
                ErrorMsgs.add("<b>ICD k</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDK);
        }
        if (!isEmpty(ICDL)) {
            if (iCDRepository.validateICD(ICDL) == 0) {
                ErrorMsgs.add("<b>ICD L</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n");
            }
            ICDs.add(ICDL);
        }

        if (PatientRelationtoPrimary.compareTo("Self") != 0) {
            if (isEmpty(PriInsuredFirstName) && isEmpty(PriInsuredLastName)) {
                ErrorMsgs.add("<b>Primary Insurer Name</b> is <b>Missing</b>\n");
            }
        }

        if (!isEmpty(DOB) && !isEmpty(DOS)) {
            if (isInValidDate(DOB, DOS)) {
                ErrorMsgs.add("DOS cannot be prior to the DOB, correction is required");
            }
        }

        if (!isEmpty(BillingProvider_Taxonomy)) {
            if (isInValidTaxonomy(BillingProvider_Taxonomy)) {
                ErrorMsgs.add("Billing Provider <b>Taxonomy Code</b> is  <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>Taxonomy Code</b> is <b>Missing</b>\n");
        }

        final String NPI_REGEX = "[0-9]{10}";

        if (!isEmpty(BillingProviderNPI)) {
            if (BillingProviderNPI.matches(NPI_REGEX)) {
                if (!isValid(BillingProviderNPI))
                    ErrorMsgs.add("Billing Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                            "* Length of the NPI should be 10 digits\"> ");
            } else {
                ErrorMsgs.add("Billing Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                        "* Length of the NPI should be 10 digits\"> ");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>NPI</b> is <b>Missing</b>\n");
        }

        final String Name_REGEX = "^([A-Z0-9 ]{2,100})$";
        if (!isEmpty(BillingProviderLastName)) {
            if (!BillingProviderLastName.matches(Name_REGEX)) {
                ErrorMsgs.add("Billing Provider <b>LastName</b> is <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>LastName</b> is <b>Missing</b>\n");
        }

        if (!isEmpty(BillingProviderFirstName)) {
            if (!BillingProviderFirstName.matches(Name_REGEX)) {
                ErrorMsgs.add("Billing Provider <b>FirstName</b> is <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>FirstName</b> is <b>Missing</b>\n");
        }

        if (!isEmpty(ClientAddress)) {
            if (isInValidAddress(ClientAddress)) {
                ErrorMsgs.add("Billing Provider <b>Address/b> Cannot be <b>PO BOX</b> Address. Only Physical Address Is <b>Allowed</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>Address</b> is <b>Missing</b>\n");
        }


        if (!isEmpty(ClientZipCode)) {
            if (ClientZipCode.contains("-")) {
                String[] ClientZipCodes = ClientZipCode.split("-");
                if (isInValidZipCode(ClientState, ClientZipCodes[0])) {
                    ErrorMsgs.add("Billing Provider <b>ZipCode</b> does not match with <b>State</b>\n");
                }
            } else {
                if (isInValidZipCode(ClientState, ClientZipCode)) {
                    ErrorMsgs.add("Billing Provider <b>ZipCode</b> does not match with <b>State</b>\n");
                }
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>ZipCode</b> is <b>Missing</b>\n");
        }

        final String CITY_REGEX = "^([0-9, A-Z, a-z]{2,30})$";
        if (!isEmpty(ClientCity)) {
            if (!ClientCity.matches(CITY_REGEX)) {
                ErrorMsgs.add("Billing Provider <b>City</b> is <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>City</b> is <b>Missing</b>\n");
        }


        final String BillingProvider_TaxID_LENGTH_REGEX = "([0-9]{9})";
        final String BillingProvider_TaxID_SEQUENCE_REGEX = "(?=012345678|987654321|098765432|000000000|111111111|222222222|333333333|444444444|555555555|666666666|777777777|888888888|999999999|123456789).{9}";
        if (!isEmpty(ClientTaxID)) {
            if (ClientTaxID.matches(BillingProvider_TaxID_LENGTH_REGEX)) {
                if (ClientTaxID.matches(BillingProvider_TaxID_SEQUENCE_REGEX)) {
                    ErrorMsgs.add("Billing Provider <b>Tax ID</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Billing Provider <b>Tax ID</b> is <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>Tax ID</b> is <b>Missing</b>\n");
        }

        InsuranceDTO PrimaryinsuranceDetailsById = new InsuranceDTO();
        try {
            if (!isEmpty(claim.getPriInsuranceNameId())) {
                PrimaryinsuranceDetailsById = externalService.getInsuranceDetailsById(claim.getPriInsuranceNameId());
                PriFillingIndicator = PrimaryinsuranceDetailsById.getClaimIndicator_P();
                Payer_Address = PrimaryinsuranceDetailsById.getAddress();
                Payer_City = PrimaryinsuranceDetailsById.getCity();
                Payer_State = PrimaryinsuranceDetailsById.getState();
                Payer_Zip = PrimaryinsuranceDetailsById.getZip();

            }
        } catch (NumberFormatException e) {
            ErrorMsgs.add("EXCEPTION in getting Primary Insurance Details" + new RuntimeException(e).getMessage());
            return ErrorMsgs;
        }

        InsuranceDTO SecondaryinsuranceDetailsById = new InsuranceDTO();
        try {
            if (!isEmpty(claim.getSecondaryInsuranceId())) {
                SecondaryinsuranceDetailsById = externalService.getInsuranceDetailsById(claim.getSecondaryInsuranceId());
                SecFillingIndicator = SecondaryinsuranceDetailsById.getClaimIndicator_P();
            }
        } catch (NumberFormatException e) {
            ErrorMsgs.add("EXCEPTION in getting Secondary Insurance Details" + new RuntimeException(e).getMessage());
            return ErrorMsgs;
        }


        try {

            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && (PrimaryinsuranceDetailsById.getPayerID().equals("13174") || PrimaryinsuranceDetailsById.getPayerID().equals("06111"))) {
                if (claim.getFreq().equals("7") || claim.getFreq().equals("8")) {
                    ErrorMsgs.add("<b>Payer</b> does not accept <b>correct/void</b> claims electronically, please file your claim on paper\n");
                }
            }

            if (claim.getFreq().equals("7") || claim.getFreq().equals("8")) {
                if (isEmpty(OrigClaim)) {
                    ErrorMsgs.add("<b>Insurance Claim Control Number </b> is  <b>missing</b> , If you use frequency code <b> 7 </b> or <b>8</b> \n");

                }
            }

            if (isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && isEmpty(SecondaryinsuranceDetailsById.getPayerID())) {
                ErrorMsgs.add("<b>Insurance </b> is <b>Missing</b> \n");
            }


            if (!isEmpty(PriFillingIndicator) && (PriFillingIndicator.equals("MB") || PriFillingIndicator.equals("MA"))) {

                //                if (PatientRelationshipCode.compareTo("18") != 0) {
                //                    ErrorMsgs.add("<b>Subscriber Relationship</b>  must be   <b>Self</b>\n");
                //                }

                if (!isEmpty(ReferringProviderNPI) && !isEmpty(BillingProviderNPI)) {

                    if (orderreferringRepository.validateOrderingOrRefferingProvider(ReferringProviderLastName, ReferringProviderNPI) == 0) {
                        ErrorMsgs.add("<b>Referring provider </b> is <b>Invalid</b> \n");
                    }

                }


                if (!isEmpty(OrderingProvidersNPI) && !isEmpty(BillingProviderNPI)) {
                    if (orderreferringRepository.validateOrderingOrRefferingProvider(OrderingProvidersLastName, OrderingProvidersNPI) == 0) {
                        ErrorMsgs.add("<b>Ordering provider </b> is <b>Invalid</b> \n");
                    }
                }
            }


            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && (PrimaryinsuranceDetailsById.getPayerID().equals("84146") || PrimaryinsuranceDetailsById.getPayerID().equals("31114"))) {
                if (PatientRelationtoPrimary.compareTo("Self") != 0 || PatientRelationtoSec.compareTo("Self") != 0) {
                    ErrorMsgs.add("<b>Subscriber Relationship</b>  must be   <b>Self</b>\n");
                }
            }

            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && (PriFillingIndicator.equals("MB") || PriFillingIndicator.equals("MA") || PriFillingIndicator.equals("MC"))) {
                if (PrimaryinsuranceDetailsById.getPayerID().equals("DNC00")) {
                    if (!isEmpty(GrpNumber)) {
                        ErrorMsgs.add("Subscriber <b>group number</b> not allowed for <b>Medicare</b> and <b>Medicaid</b> \n");
                    }
                }
            }

            if (!isEmpty(PatientLastName)) {
                if (!PatientLastName.matches(Name_REGEX)) {
                    ErrorMsgs.add("Subscriber  <b>LastName</b> is <b>InValid</b>\n");
                }

                if (PatientLastName.equalsIgnoreCase("TEST") || PatientLastName.equalsIgnoreCase("DEMO")) {
                    ErrorMsgs.add("THE PATIENT SEEMS TO BE A TEST PATIENT . DO NOT SUBMIT CLAIM WITHOUT DUE VERIFICATION \n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>LastName</b> is <b>Missing</b>\n");
            }

            if (!isEmpty(PatientFirstName)) {
                if (!PatientFirstName.matches(Name_REGEX)) {
                    ErrorMsgs.add("Subscriber <b>FirstName</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>FirstName</b> is <b>Missing</b>\n");
            }


            final String MemberID_REGEX777 = "^([V]{1})+([0-9]{8})$";//payerid 77073

            if (!isEmpty(claim.getMemId()) && PrimaryinsuranceDetailsById.getPayerID().equals("77073")) {
                if (!claim.getMemId().matches(MemberID_REGEX777)) {
                    ErrorMsgs.add("subscriber id for VNS choice should start with “V” proceeded with 8 digits numeric\n");
                }
            }

            final String MemberID_REGEX = "^(123456789|^(TEST)|^[0]{2,}|^[1]{2,}|^[2]{2,}|^[3]{2,}|^[4]{2,}|^[5]{2,}|^[6]{2,}|^[7]{2,}|^[8]{2,}|^[9]{2,})$";
            final String MemberID_REGEX999 = "^[^@!$\\-\\/+*.,<>&%#]*$";

//                    MemId
            if (!isEmpty(claim.getMemId())) {
                if (claim.getMemId().matches(MemberID_REGEX) || !claim.getMemId().matches(MemberID_REGEX999)) {
                    ErrorMsgs.add("Subscriber <b>Member-ID</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>Member-ID</b> is <b>Missing</b>\n");
            }

            if (!isEmpty(ZipCode)) {
                if (isInValidZipCode(State, ZipCode)) {
                    ErrorMsgs.add("Subscriber <b>ZipCode</b> does not match with <b>State</b>\n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>ZipCode</b> is <b>Missing</b>\n");
            }

            if (!isEmpty(City)) {
                if (!City.matches(CITY_REGEX)) {
                    ErrorMsgs.add("Subscriber <b>City</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>City</b> is <b>Missing</b>\n");
            }

            String SubscriberDOB = "19261111";
            //system.out.println("DOB -> " + DOB);
            if (!isEmpty(DOB) && !isEmpty(ClaimCreateDate)) {
                if (Integer.parseInt(DOB) > Integer.parseInt(ClaimCreateDate)) {
                    ErrorMsgs.add("Subscriber <b>DOB</b> is <b>InValid</b>\n");
                }
            }

            final String MemberID_REGEX000 = "^(YUB|YUX|XOJ|XOD|ZGJ|ZGD|YIJ|YID|YDJ|YDL)+([A-Z0-9])*$";//payerid 77073
            if (!isEmpty(DOS)) {
                if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID())
                        && (PrimaryinsuranceDetailsById.getPayerID().equals("00621") || PrimaryinsuranceDetailsById.getPayerID().equals("00840") || PrimaryinsuranceDetailsById.getPayerID().equals("84980") || PrimaryinsuranceDetailsById.getPayerID().equals("00790"))
                        && !isEmpty(claim.getMemId())
                        && Integer.parseInt(DOS) > 20170101) {
                    if (claim.getMemId().toUpperCase().matches(MemberID_REGEX000)) {
                        ErrorMsgs.add("Medicare Advantage Claim need to be resubmitter with <b>PayerID : 66006</b> \n");
                    }
                }
            }

            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && PrimaryinsuranceDetailsById.getPayerID().equals("66006") && !isEmpty(claim.getMemId()) && Integer.parseInt(DOS) > 20170101) {
                if (claim.getMemId().startsWith("YUB") || claim.getMemId().startsWith("YUX") || claim.getMemId().startsWith("XOJ") || claim.getMemId().startsWith("XOD") || claim.getMemId().startsWith("ZGJ") ||
                        claim.getMemId().startsWith("ZGD") || claim.getMemId().startsWith("YIJ") || claim.getMemId().startsWith("YID") || claim.getMemId().startsWith("YDJ") || claim.getMemId().startsWith("YDL")) {
                    ErrorMsgs.add("Medicare Advantage Claim need to be resubmitter with <b>PayerID : 66006</b> when <b>secondary</b> insurance is <b>Medicare</b>\n");
                }
            }


            if (!isEmpty(PriFillingIndicator) && PriFillingIndicator.equals("BL") && !isEmpty(claim.getMemId())) {
                if (claim.getMemId().startsWith("TFC") || claim.getMemId().startsWith("CUX") || claim.getMemId().startsWith("DSN") || claim.getMemId().startsWith("FMW") || claim.getMemId().startsWith("HAR") ||
                        claim.getMemId().startsWith("IPW") || claim.getMemId().startsWith("KER") || claim.getMemId().startsWith("NUZ") || claim.getMemId().startsWith("NVC") || claim.getMemId().startsWith("XNE")
                        || claim.getMemId().startsWith("XNH") || claim.getMemId().startsWith("XNJ") || claim.getMemId().startsWith("XNN") || claim.getMemId().startsWith("XNV")) {
                    if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && !PrimaryinsuranceDetailsById.getPayerID().equals("00611"))
                        ErrorMsgs.add(" <b>Subscriber ID</b> belongs to <b>Regence Blue Shield</b> please submit the claim to  <b>Payer ID : 00611</b>\n");
                }
                //                else if (!claimDto.getMemId().startsWith("TFC") && !claimDto.getMemId().startsWith("CUX") && !claimDto.getMemId().startsWith("DSN") && !claimDto.getMemId().startsWith("FMW") && !claimDto.getMemId().startsWith("HAR") ||
                //                        !claimDto.getMemId().startsWith("IPW") && !claimDto.getMemId().startsWith("KER") && !claimDto.getMemId().startsWith("NUZ") && !claimDto.getMemId().startsWith("NVC") && !claimDto.getMemId().startsWith("XNE")
                //                                && !claimDto.getMemId().startsWith("XNH") && !claimDto.getMemId().startsWith("XNJ") && !claimDto.getMemId().startsWith("XNN") && !claimDto.getMemId().startsWith("XNV")) {
                //                    if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && !PrimaryinsuranceDetailsById.getPayerID().equals("00054"))
                //                        ErrorMsgs.add(" <b>Subscriber ID</b> belongs to <b>Blue Cross Of Idaho</b> please submit the claim to  <b>Payer ID : 00054</b>\n");
                //                }
                else if (!isEmpty(claim.getMemId()) && (claim.getMemId().startsWith("YLS89") || claim.getMemId().startsWith("NYC"))) {
                    ErrorMsgs.add(" This Claim Does Not Belongs To <b>BCBS</b> ,Patient’s Policy Covers Only Hospital Benefits, Please Confirm Insurance For Medical Benefits.\n");
                }
            }


            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && PrimaryinsuranceDetailsById.getPayerID().equals("87726")) {
                if (!isEmpty(claim.getMemId()) && claim.getMemId().startsWith("7") && (claim.getMemId().length() == 7)) {
                    ErrorMsgs.add(" <b>Subscriber ID</b> belongs to <b>Student Resources Insurance </b> please submit the claim to  <b>Payer ID : 74227</b>\n");
                }
            }


            if (!isEmpty(Payer_Zip)) {
                if (isInValidZipCode(Payer_State, Payer_Zip)) {
                    ErrorMsgs.add("Payer <b>ZipCode</b> does not match with <b>State</b>\n");
                }
            } else {
                ErrorMsgs.add("Payer <b>ZipCode</b> is <b>Missing</b>\n");
            }

            if (!isEmpty(Payer_City)) {
                if (!Payer_City.matches(CITY_REGEX)) {
                    ErrorMsgs.add("Payer <b>City</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Payer <b>City</b> is <b>Missing</b>\n");
            }

            if (PriFillingIndicator.equals("MB") && !claim.getFreq().equals("1")) {
                ErrorMsgs.add("Medicare always accept the claim as <b>ORIGINAL/NEW CLAIM </b>. rejected due to claim <b>frequency code</b>\n");
            }

            String Related_Causes_Code = null;

            if (!isEmpty(claim.getClaimadditionalinfo().getAutoAccidentAddInfo()) && claim.getClaimadditionalinfo().getAutoAccidentAddInfo().equals("1")) {
                Related_Causes_Code = "AA";
            } else if (!isEmpty(claim.getClaimadditionalinfo().getEmploymentStatusAddInfo()) && claim.getClaimadditionalinfo().getEmploymentStatusAddInfo().equals("1")) {
                Related_Causes_Code = "EM";
            } else if (!isEmpty(claim.getClaimadditionalinfo().getOtherAccidentAddInfo()) && claim.getClaimadditionalinfo().getOtherAccidentAddInfo().equals("1")) {
                Related_Causes_Code = "OA";
            }

            if (isEmpty(claim.getFreq())) {
                ErrorMsgs.add(" <b>Frequency Code</b> is  <b>Missing</b>\n");
            }
            System.out.println("DOS ->> " + DOS);
            if (!isEmpty(String.valueOf(claim.getClaimadditionalinfo().getAccidentIllnesDateAddInfo()))
                    && !isEmpty(String.valueOf(claim.getClaimadditionalinfo().getLastMenstrualPeriodDateAddInfo()))) {
                if (!isInValidDate(String.valueOf(claim.getClaimadditionalinfo().getAccidentIllnesDateAddInfo()), DOS)
                        && !isInValidDate(String.valueOf(claim.getClaimadditionalinfo().getLastMenstrualPeriodDateAddInfo()), DOS))
                    if (claim.getClaimadditionalinfo().getAccidentIllnesDateAddInfo().equals(claim.getClaimadditionalinfo().getLastMenstrualPeriodDateAddInfo()))
                        ErrorMsgs.add(" <b>Illness Date</b> and <b>Last Menstrual Period Date</b>  cannot be <b>Same</b>\n");
            }


            if ((!isEmpty(Related_Causes_Code) && Related_Causes_Code.equals("AA")) || (!isEmpty(Related_Causes_Code) && Related_Causes_Code.equals("OA"))) {
                if (isEmpty(String.valueOf(claim.getClaimadditionalinfo().getAccidentIllnesDateAddInfo()))) {
                    ErrorMsgs.add(" <b>Accident Date</b> is <b>Missing</b>\n");
                } else if (isInValidDate(String.valueOf(claim.getClaimadditionalinfo().getAccidentIllnesDateAddInfo()), DOS)) {
                    ErrorMsgs.add(" <b>Accident Date</b> is <b>InValid</b>\n");
                }
            }

            if (!isEmpty(BillingProvider_Taxonomy)) {
                if (Taxonomy_for_InitialTreatment_List.contains(BillingProvider_Taxonomy)) {
                    if (PriFillingIndicator.equals("MB") || PriFillingIndicator.equals("MA")) {
                        if (isEmpty(String.valueOf(claim.getClaimadditionalinfo().getInitialTreatDateAddInfo()))) {
                            ErrorMsgs.add("<b>Initial Treatment Date</b> is  <b>Missing</b>\n");
                        } else if (isInValidDate(String.valueOf(claim.getClaimadditionalinfo().getInitialTreatDateAddInfo()), DOS)) {
                            ErrorMsgs.add("<b>Initial Treatment Date</b> is  <b>InValid</b>\n");
                        }
                    }
                }


                if (TaxonomyList.contains(BillingProvider_Taxonomy)) {
                    //Last Seen Date of Claim should Not be Equal to Date Time Min Value
                    if (isEmpty(String.valueOf(claim.getClaimadditionalinfo().getLastSeenDateAddInfo()))) {
                        ErrorMsgs.add("<b>Last Seen Date</b> is  <b>Missing</b>, Last Seen Date is required for this speciality code <b>[" + BillingProvider_Taxonomy + "]</b>\n");
                    } else if (isInValidDate(String.valueOf(claim.getClaimadditionalinfo().getLastSeenDateAddInfo()), DOS)) {
                        ErrorMsgs.add("<b>Last Seen Date</b> is  <b>InValid</b>,  Last Seen Date is required for this speciality code <b>[" + BillingProvider_Taxonomy + "]</b>\n");
                    }
                }
            }


            if (!isEmpty(ReferringProviderFirstName)) {
                if (!isEmpty(ReferringProviderNPI)) {
                    if (ReferringProviderNPI.matches(NPI_REGEX)) {
                        if (!isValid(ReferringProviderNPI))
                            ErrorMsgs.add("Referring Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                    "* Length of the NPI should be 10 digits\"> ");
                    } else {
                        ErrorMsgs.add("Referring Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                "* Length of the NPI should be 10 digits\"> ");
                    }
                } else {
                    ErrorMsgs.add("Referring Provider <b>NPI</b> is <b>Missing</b>\n");
                }


                if (!isEmpty(ReferringProviderLastName)) {
                    if (!ReferringProviderLastName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Referring Provider <b>LastName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Referring Provider <b>LastName</b> is <b>Missing</b>\n");
                }

                if (!isEmpty(ReferringProviderFirstName)) {
                    if (!ReferringProviderFirstName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Referring Provider <b>FirstName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Referring Provider <b>FirstName</b> is <b>Missing</b>\n");
                }


                if (PrimaryinsuranceDetailsById.getPayerID().equals("11315") || PrimaryinsuranceDetailsById.getPayerID().equals("87726")) {
                    if (ReferringProviderNPI.equals(BillingProviderNPI)) {
                        ErrorMsgs.add("Referring Provider  is <b>Missing/InValid</b>\n");
                    }
                }
            }

            if (!isEmpty(RenderingProvidersFirstName)) {


                // 2310B RENDERING PROVIDER NAME
                //                Loop loop_2310B = loop_2300.addChild("2300");

                if (!isEmpty(RenderingProvidersNPI)) {
                    if (RenderingProvidersNPI.matches(NPI_REGEX)) {
                        if (!isValid(RenderingProvidersNPI))
                            ErrorMsgs.add("Rendering Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                    "* Length of the NPI should be 10 digits\"> ");


                        if (!isEmpty(ClientNPI)) {
                            if (ClientNPI.equals(RenderingProvidersNPI)) {
                                ErrorMsgs.add("Rendering Provider and Service Location <b> NPI</b> cannot be  <b>Same</b>\n");
                            }
                        }

                        //                        if (!isEmpty(BillingProviderNPI)) {
                        //                            if (BillingProviderNPI.equals(RenderingProvidersNPI)) {
                        //                                ErrorMsgs.add("Rendering Provider and Billing Provider <b> NPI</b> cannot be  <b>Same</b>\n");
                        //                            }
                        //                        }

                    } else {
                        ErrorMsgs.add("Rendering Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                "* Length of the NPI should be 10 digits\"> ");
                    }
                } else {
                    ErrorMsgs.add("Rendering Provider <b>NPI</b> is <b>Missing</b>\n");
                }

                if (!isEmpty(RenderingProvidersLastName)) {
                    if (!RenderingProvidersLastName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Rendering Provider <b>LastName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Rendering Provider <b>LastName</b> is <b>Missing</b>\n");
                }

                if (!isEmpty(RenderingProvidersFirstName)) {
                    if (!RenderingProvidersFirstName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Rendering Provider <b>FirstName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Rendering Provider <b>FirstName</b> is <b>Missing</b>\n");
                }


                //                loop_2310B.addSegment("NM1*82*1*" + RenderingProvidersLastName + "*" + RenderingProvidersFirstName + "****XX*" + RenderingProvidersNPI);//NM1 Rendering PROVIDER
                //                String RenderingProviders_Taxonomy = "225100000X";

                if (!isEmpty(RenderingProviders_Taxonomy)) {
                    if (isInValidTaxonomy(RenderingProviders_Taxonomy)) {
                        ErrorMsgs.add("Rendering Provider <b>Taxonomy Code</b> is  <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Rendering Provider <b>Taxonomy Code</b> is <b>Missing</b>\n");
                }
                //                loop_2310B.addSegment("PRV*PE*PXC*" + RenderingProviders_Taxonomy); //PRV Rendering Provider SPECIALTY


            }

            if (!isEmpty(ClientNPI)) {
                if (ClientNPI.matches(NPI_REGEX)) {
                    if (!isValid(ClientNPI))
                        ErrorMsgs.add("Client <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                "* Length of the NPI should be 10 digits\"> ");
                } else {
                    ErrorMsgs.add("Client <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                            "* Length of the NPI should be 10 digits\"> ");
                }

                if (!isEmpty(BillingProviderNPI)) {
                    if (ClientNPI.equals(BillingProviderNPI)) {
                        ErrorMsgs.add("<b>Billing Provider NPI </b> and <b>Client NPI</b> cannot be <b>same</b> \n");
                    }
                }
            } else {
                ErrorMsgs.add("Client  <b>NPI</b> is <b>Missing</b>\n");
            }


            if (!isEmpty(SupervisingProviderFirstName)) {
                if (!isEmpty(SupervisingProviderNPI)) {
                    if (SupervisingProviderNPI.matches(NPI_REGEX)) {
                        if (!isValid(SupervisingProviderNPI))
                            ErrorMsgs.add("Supervising Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                    "* Length of the NPI should be 10 digits\"> ");
                    } else {
                        ErrorMsgs.add("Supervising Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                "* Length of the NPI should be 10 digits\"> ");
                    }
                } else {
                    ErrorMsgs.add("Supervising Provider <b>NPI</b> is <b>Missing</b>\n");
                }


                if (!isEmpty(SupervisingProviderLastName)) {
                    if (!SupervisingProviderLastName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Supervising Provider <b>LastName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Supervising Provider <b>LastName</b> is <b>Missing</b>\n");
                }

                if (!isEmpty(SupervisingProviderFirstName)) {
                    if (!SupervisingProviderFirstName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Supervising Provider <b>FirstName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Supervising Provider <b>FirstName</b> is <b>Missing</b>\n");
                }

                //                Loop loop_2310D = loop_2300.addChild("2300");
                //                loop_2310D.addSegment("NM1*DQ*1*" + SupervisingProviderLastName + "*" + SupervisingProviderFirstName + "****XX*" + SupervisingProviderNPI);//NM1 Supervising PROVIDER
                //                        loop_2310D.addSegment("REF*G2*B99937");//REF Referring PROVIDER SECONDARY IDENTIFICATION
            }

            Double totalcharges = 0.0;
            String POS = "";
            ArrayList<String> POS_list_AMBULANCE_CLAIMS = new ArrayList<String>(Arrays.asList("21", "51", "61", "11", "12", "13", "22", "31", "32", "65"));
            ArrayList<String> POS_list = new ArrayList<String>(Arrays.asList("41", "42", "99"));
            ArrayList<String> POS_list_radiology = new ArrayList<String>(Arrays.asList("21", "22", "23", "24", "26", "31", "34", "41", "42", "51", "52", "53", "56", "61"));
            ArrayList<String> ProcedureCodes_List = new ArrayList<String>(Arrays.asList("99221", "99222", "99223"));
            ArrayList<String> ProcedureCodes_List_A_S_DATES_r1 = new ArrayList<String>(Arrays.asList("99221", "99222", "99223"));
            ArrayList<String> ProcedureCodes_List_A_S_DATES_r2 = new ArrayList<String>(Arrays.asList("99231", "99232", "99233"));
            ArrayList<String> Charge_ProcedureCodes = new ArrayList<String>();
            ArrayList<String> Charge_ProcedureCodes_WITHOUT_MODIFIER = new ArrayList<String>();
            ArrayList<String> Charge_VaccineCodes = new ArrayList<String>();
            ArrayList<String> WELL_VISIT_ICDs = new ArrayList<String>(Arrays.asList("Z00.00", "Z00.01", "Z00.110", "Z00.111", "Z00.121", "Z00.129", "Z01.411",
                    "Z01.419", "Z30.015", "Z30.016", "Z30.44", "Z30.45"));
            ArrayList<String> WELL_VISIT_CPTs = new ArrayList<String>(Arrays.asList("G0402", "G0438", "G0439"));
            ArrayList<String> COVID_CPTS = new ArrayList<String>(Arrays.asList("91300", "91301", "0001A", "0002A", "0011A", "0012A", "91302", "91303", "0021A", "0022A", "0031A", "0003A", "0013A", "M0201", "99401", "0041A", "0042A", "0051A", "0052A", "0053A", "0054A", "0071A",
                    "0072A", "91304", "91305", "91307", "91306", "0064A", "0004A", "0034A"));

            ArrayList<String> COVID_CPTS_TJ = new ArrayList<String>(Arrays.asList("90471", "0001A", "0002A", "0003A", "0004A", "0011A", "0012A", "0013A", "0021A", "0022A", "0031A", "0034A", "0041A", "0042A", "0051A", "0052A", "0053A", "0054A", "0064A", "0071A", "0072A", "90662", "90672", "90674", "90682", "90685", "90686", "90687", "90688", "90694", "90756",
                    "90653", "91300", "91301", "91302", "91303", "91304", "91305", "91306", "91307"));

            ArrayList<String> ProcedureCodeModifier = new ArrayList<String>(Arrays.asList("A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "GY", "GZ", "H9", "HA", "HB", "HC", "HD", "HE", "HF", "HG", "HH", "HI", "HJ", "HK", "HL", "HM", "HN", "HO", "HP", "HQ", "HR", "HS", "HT", "HU", "HV", "HW", "HX", "HY", "HZ", "SA",
                    "SB", "SC", "SD", "SE", "SH", "SJ", "SK", "SL", "ST", "SU", "SV", "TD", "TE", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TP", "TQ", "TR", "TS", "TW", "U1", "U2", "U3", "U4", "U5", "U6", "U7", "U8", "U9", "UA", "UB", "UC", "UD"));


            String modifier = "";
            String DXPointer = "";
            String MeasurementCode = null;
            String ProcedureCode = null;
            String NDC_REGEX = "^[0-9A-Z]{11}$";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String Units = null;
            String Amount = null;
            String ServiceFromDate = null;
            String ServiceToDate = null;
            String mod1 = null;
            String mod2 = null;
            String mod3 = null;
            String mod4 = null;
            String MajorSurgeryDOS = null;
            String EnMSurgeryDOS = null;
            boolean isENMProcedureCode = false;
            boolean isMajorSurgeryProcedureCode = false;
            boolean addCovidMsg = false;
            boolean addAdminCodeMsg = false;
            boolean is_91303 = false;
            boolean errorMsgAdded = false;
            boolean isENM_SurgeryProcedureCode = false;
            boolean isENM_SurgeryProcedureCode2 = false;

            int units_Of_90472 = -1;
            int units_Of_90460 = -1;
            int units_Of_90461 = -1;
            int sum_Of_units_Of_allVaccines = 0;

            for (int i = 0; i < claim.getClaimchargesinfo().size(); i++) {
                modifier = "";
                ServiceFromDate = claim.getClaimchargesinfo().get(i).getServiceFromDate();//ChargesInput[i][0];
                ServiceToDate = claim.getClaimchargesinfo().get(i).getServiceToDate();//ChargesInput[i][1];
                System.out.println("ServiceToDate -> " + ServiceToDate);
                System.out.println("ServiceFromDate -> " + ServiceFromDate);


                POS = claim.getClaimchargesinfo().get(i).getPos();//ChargesInput[i][3];
                if (POS_list_AMBULANCE_CLAIMS.contains(POS)) {
                    if (isEmpty(claim.getClaimadditionalinfo().getHospitalizedFromDateAddInfo())) {
                        ErrorMsgs.add(" <b>Admission DATE</b> is <b>Missing</b>\n");

                    } else if (isInValidDate(claim.getClaimadditionalinfo().getHospitalizedFromDateAddInfo(), DOS)) {
                        ErrorMsgs.add(" <b>Admission DATE </b> is <b>InValid</b>\n");

                    }
                }


                if (POS_list.contains(POS)) {
                    if (!isEmpty(claim.getClaimambulancecode().getPickUpAddressInfoCode()) && !isEmpty(claim.getClaimambulancecode().getPickUpZipCodeInfoCode())
                            && !isEmpty(claim.getClaimambulancecode().getPickUpCityInfoCode()) && !isEmpty(claim.getClaimambulancecode().getPickUpStateInfoCode())) {
                        if (!isEmpty(claim.getClaimambulancecode().getPickUpZipCodeInfoCode())) {
                            if (isInValidZipCode(claim.getClaimambulancecode().getPickUpStateInfoCode(), claim.getClaimambulancecode().getPickUpZipCodeInfoCode())) {
                                ErrorMsgs.add("Pick Up <b>ZipCode</b> does not match with <b>State</b>\n");
                            }
                        } else {
                            ErrorMsgs.add("Pick Up <b>ZipCode</b> is <b>Missing</b>\n");
                        }

                        if (!isEmpty(claim.getClaimambulancecode().getPickUpCityInfoCode())) {
                            if (!claim.getClaimambulancecode().getPickUpCityInfoCode().matches(CITY_REGEX)) {
                                ErrorMsgs.add("Pick Up <b>City</b> is <b>InValid</b>\n");
                            }
                        } else {
                            ErrorMsgs.add("Pick Up <b>City</b> is <b>Missing</b>\n");
                        }

                        //                    Loop loop_2310E = loop_2300.addChild("2300"); //AMBULANCE PICK-UP LOCATION
                        //                    loop_2310E.addSegment("NM1*PW*2");
                        //                    loop_2310E.addSegment("N3*" + PickUpAddressInfoCode);
                        //                    loop_2310E.addSegment("N4*" + PickUpCityInfoCode + "*" + PickUpStateInfoCode + "*" + PickUpZipCodeInfoCode);
                    }

                    if (!isEmpty(claim.getClaimambulancecode().getDropoffAddressInfoCode()) && !isEmpty(claim.getClaimambulancecode().getDropoffCityInfoCode())
                            && !isEmpty(claim.getClaimambulancecode().getDropoffStateInfoCode()) && !isEmpty(claim.getClaimambulancecode().getDropoffZipCodeInfoCode())) {
                        if (!isEmpty(claim.getClaimambulancecode().getDropoffZipCodeInfoCode())) {
                            if (isInValidZipCode(claim.getClaimambulancecode().getDropoffStateInfoCode(), claim.getClaimambulancecode().getDropoffZipCodeInfoCode())) {
                                ErrorMsgs.add("Drop off <b>ZipCode</b> does not match with <b>State</b>\n");
                            }
                        } else {
                            ErrorMsgs.add("Drop off <b>ZipCode</b> is <b>Missing</b>\n");
                        }

                        if (!isEmpty(claim.getClaimambulancecode().getDropoffCityInfoCode())) {
                            if (!claim.getClaimambulancecode().getDropoffCityInfoCode().matches(CITY_REGEX)) {
                                ErrorMsgs.add("Drop off <b>City</b> is <b>InValid</b>\n");
                            }
                        } else {
                            ErrorMsgs.add("Drop off <b>City</b> is <b>Missing</b>\n");
                        }

                        //                    Loop loop_2310F = loop_2300.addChild("2300"); //AMBULANCE DROP-OFF LOCATION
                        //                    loop_2310F.addSegment("NM1*45*2");
                        //                    loop_2310F.addSegment("N3*" + DropoffAddressInfoCode);
                        //                    loop_2310F.addSegment("N4*" + DropoffCityInfoCode + "*" + DropoffStateInfoCode + "*" + DropoffZipCodeInfoCode);
                    }
                }


                Units = String.valueOf(claim.getClaimchargesinfo().get(i).getUnits());
                Units = Units.contains(".") ? Units.substring(0, Units.indexOf(".")) : Units;
                Amount = String.valueOf(claim.getClaimchargesinfo().get(i).getAmount());//ChargesInput[i][12];
                ProcedureCode = claim.getClaimchargesinfo().get(i).getHCPCSProcedure();//ChargesInput[i][2];
                mod1 = claim.getClaimchargesinfo().get(i).getMod1();//ChargesInput[i][5];
                mod2 = claim.getClaimchargesinfo().get(i).getMod2();//ChargesInput[i][6];
                mod3 = claim.getClaimchargesinfo().get(i).getMod3();//ChargesInput[i][7];
                mod4 = claim.getClaimchargesinfo().get(i).getMod4();//ChargesInput[i][8];
                DXPointer = claim.getClaimchargesinfo().get(i).getDXPointer();//ChargesInput[i][9];
                ////system.out.println("****ProcedureCode");


                if (!isEmpty(ProcedureCode)) {
                    if (cPTRepository.validateCPT(ProcedureCode) == 0) {
                        ErrorMsgs.add("<b>Procedure [" + ProcedureCode + "]</b> USED DOESNOT MATCH WITH American Medical Association (AMA). PLEASE USE THIS LINK FOR CORRECT Procedure CODE: https://www.cms.gov/medicare/fraud-and-abuse/physicianselfreferral/list_of_codes\n");
                    }
                } else {
                    ErrorMsgs.add("<b>Procedure</b> is Missing. It cannot be null or empty\n");
                }


                if (enmprocedureRepository.validateE_N_M_ProceduresCodes(ProcedureCode) == 1) {//isValid_E_N_M_ProceduresCodes(conn, ProcedureCode)) {
                    if (!isEmpty(mod1)) {
                        if (enmNotallowedModifierRepository.validateE_N_M_Modifiers(mod1) > 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod1 + " ]</b> is <b>not allowed </b> with E&M Procedure code <b>[ " + ProcedureCode + " ]</b>\n");
                        }
                    }
                    if (!isEmpty(mod2)) {
                        if (enmNotallowedModifierRepository.validateE_N_M_Modifiers(mod2) > 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod2 + " ]</b> is <b>not allowed </b> with E&M Procedure code <b>[ " + ProcedureCode + " ]</b>\n");
                        }
                    }
                    if (!isEmpty(mod3)) {
                        if (enmNotallowedModifierRepository.validateE_N_M_Modifiers(mod3) > 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod3 + " ]</b> is <b>not allowed </b> with E&M Procedure code <b>[ " + ProcedureCode + " ]</b>\n");
                        }
                    }
                    if (!isEmpty(mod4)) {
                        if (enmNotallowedModifierRepository.validateE_N_M_Modifiers(mod4) > 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod4 + " ]</b> is <b>not allowed </b> with E&M Procedure code <b>[ " + ProcedureCode + " ]</b>\n");
                        }
                    }
                }


                if (anesthesiacodeRepository.validateAnesthesiaCodes(ProcedureCode) == 1) {
                    MeasurementCode = "MJ";
                    if (!isEmpty(mod1)) {//mod1
                        modifier += ":" + mod1; //SV101-3
                        if (Units.equals("1") && mod1.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }

                        if (anesthesiamodifierRepository.validateAnesthesiaModifier(mod1) == 0) {//isValidAnesthesiaModifier(conn, mod1)) {
                            ErrorMsgs.add("Modifier <b>[ " + mod1 + " ]</b> is <b>not allowed </b> <b>Anesthesia Modifier</b>  is only allowed with  <b>Anesthesia Procedures [" + ProcedureCode + "]</b>\n");
                        }


                    }
                    if (!isEmpty(mod2)) {//mod2
                        modifier += ":" + mod2;
                        if (Units.equals("1") && mod2.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }

                        if (anesthesiamodifierRepository.validateAnesthesiaModifier(mod2) == 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod2 + " ]</b> is <b>not allowed </b> <b>Anesthesia Modifier</b>  is only allowed with  <b>Anesthesia Procedures [" + ProcedureCode + "]</b>\n");
                        }
                    }
                    if (!mod3.equals("")) {//mod3
                        modifier += ":" + mod3;
                        if (Units.equals("1") && mod3.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }


                        if (anesthesiamodifierRepository.validateAnesthesiaModifier(mod3) == 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod3 + " ]</b> is <b>not allowed </b>b>Anesthesia Modifier</b>  is only allowed with  <b>Anesthesia Procedures [" + ProcedureCode + "]</b>\n");
                        }
                    }
                    if (!mod4.equals("")) {//mod4
                        modifier += ":" + mod4;
                        if (Units.equals("1") && mod4.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }


                        if (anesthesiamodifierRepository.validateAnesthesiaModifier(mod4) == 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod4 + " ]</b> is <b>not allowed </b> <b>Anesthesia Modifier</b>  is only allowed with  <b>Anesthesia Procedures [" + ProcedureCode + "]</b>\n");
                        }
                    }


                } else {
                    MeasurementCode = "UN";
                    if (!isEmpty(mod1)) {//mod1
                        modifier += ":" + mod1; //SV101-3
                        if (claim.getClientId() == 32) {
                            if (Units.equals("1") && mod1.equals("50")) {
                                ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                            }
                        }
                    }
                    if (!isEmpty(mod2)) {//mod2
                        modifier += ":" + mod2;
                        if (Units.equals("1") && mod2.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }
                    }
                    if (!isEmpty(mod3)) {//mod3
                        modifier += ":" + mod3;
                        if (Units.equals("1") && mod3.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }
                    }
                    if (!isEmpty(mod4)) {//mod4
                        modifier += ":" + mod4;
                        if (Units.equals("1") && mod4.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }
                    }
                }


                if (majorsurgerycodeRepository.validateMajor_Surgery_ProceduresCodes(ProcedureCode) == 1) {//isValid_Major_Surgery_ProceduresCodes(conn, ProcedureCode)) {
                    //                    //system.out.println("******isValid_Major_Surgery_ProceduresCodes ->> "+ProcedureCode);
                    isMajorSurgeryProcedureCode = true;
                    if (ServiceToDate.equals(ServiceFromDate))
                        MajorSurgeryDOS = ServiceToDate.replace("/", "");
                }


                if (enmsurgerycodeRepository.validateE_N_M_Surgery_ProceduresCodes(ProcedureCode) == 1 && isMajorSurgeryProcedureCode) {
                    //                    //system.out.println("****isValid_E_N_M_Surgery_ProceduresCodes ->> "+ProcedureCode);
                    //                    //system.out.println("****MajorSurgeryDOS ->> "+MajorSurgeryDOS);
                    isENM_SurgeryProcedureCode2 = true;
                    if (ServiceToDate.equals(ServiceFromDate))
                        EnMSurgeryDOS = ServiceToDate.replace("/", "");
                    else
                        EnMSurgeryDOS = ServiceFromDate.replace("/", "");

                    System.out.println("****EnMSurgeryDOS ->> " + EnMSurgeryDOS);
                    System.out.println("****ServiceToDate ->> " + ServiceToDate);
                    System.out.println("****ServiceToDate.replace(\"-\", \"\") ->> " + ServiceToDate.replace("/", ""));
                    System.out.println("Procedure Code -> " + ProcedureCode);
                    if (MajorSurgeryDOS.equals(EnMSurgeryDOS)
                            || MajorSurgeryDOS.equals(String.valueOf(Integer.parseInt(EnMSurgeryDOS) - 1))
                            || EnMSurgeryDOS.equals(String.valueOf(Integer.parseInt(MajorSurgeryDOS) - 1))) {
                        if (!modifier.contains("57")) {
                            ErrorMsgs.add("<b>Major Surgery</b> identified on the day or one day before E&M please append <b>modifier 57</b> with <b>E&M</b>\n");
                        }
                    }
                }


                if (DXPointer.contains("A") && !isEmpty(ICDA)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDA);
                    ////                    //system.out.println("r1 ->> " + r1);
                    //
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDA + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDA + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDA + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }


                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H6120", '.', 3), addChar("H6123", '.', 3), ICDA) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDA + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }

                }
                if (DXPointer.contains("B") && !isEmpty(ICDB)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDB);
                    ////                    //system.out.println("r1 ->> " + r1);
                    //
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDB + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDB + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDB + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }

                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDB) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDB + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("C") && !isEmpty(ICDC)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDC);
                    ////                    //system.out.println("r1 ->> " + r1);
                    //
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDC + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDC + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDC + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }

                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDC) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDC + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("D") && !isEmpty(ICDD)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDD);
                    ////                    //system.out.println("r1 ->> " + r1);
                    //
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDD + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDD + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDD + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }


                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDD) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDD + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("E") && !isEmpty(ICDE)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDE);
                    ////                    //system.out.println("r1 ->> " + r1);
                    //
                    ////                    result2 = r1 == null ? null : r1;
                    ////                    //system.out.println("r1 ->> " + r1);
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDE + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDE + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDE + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }

                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDE) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDE + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("F") && !isEmpty(ICDF)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDF);
                    //                    //system.out.println("r1 ->> " + r1);

                    //                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDF + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDF + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDF + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }

                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDF) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDF + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("G") && !isEmpty(ICDG)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDG);
                    ////                    //system.out.println("r1 ->> " + r1);
                    //
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDG + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDG + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDG + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }

                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDG) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDG + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("H") && !isEmpty(ICDH)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDH);
                    ////                    //system.out.println("r1 ->> " + r1);
                    //
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDH + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDH + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDH + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }


                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDH) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDH + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code  i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("I") && !isEmpty(ICDI)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDI);
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDI + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDI + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDI + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }


                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDI) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDI + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code. \n");
                        }
                    }
                }
                if (DXPointer.contains("J") && !isEmpty(ICDJ)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDJ);
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDJ + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDJ + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDJ + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }


                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDJ) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDJ + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code  i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("K") && !isEmpty(ICDK)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDK);
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDK + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDK + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDK + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }


                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDK) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDK + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }
                if (DXPointer.contains("L") && !isEmpty(ICDL)) {
                    //                    r1 = isValidBodySidewaysICD(conn, ICDL);
                    ////                    result2 = r1 == null ? null : r1;
                    //                    if (r1 != null) {
                    //                        if (r1.equals("Right")) {
                    //                            if (!(modifier.contains("RT") || modifier.contains("F5") || modifier.contains("F6") || modifier.contains("F7") ||
                    //                                    modifier.contains("F8") || modifier.contains("F9") || modifier.contains("T5") || modifier.contains("T6") ||
                    //                                    modifier.contains("T7") || modifier.contains("T8") || modifier.contains("T9") || modifier.contains("E3") ||
                    //                                    modifier.contains("E4"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDL + "]</b> Please append one of these <b>relative modifiers : [RT, F5, F6, F7, F8, F9, T5, T6, T7, T8, T9, E3, E4] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Left")) {
                    //                            if (!(modifier.contains("LT") || modifier.contains("FA") || modifier.contains("F1") || modifier.contains("F2") ||
                    //                                    modifier.contains("F3") || modifier.contains("F4") || modifier.contains("TA") || modifier.contains("T1") ||
                    //                                    modifier.contains("T2") || modifier.contains("T3") || modifier.contains("T4") || modifier.contains("E1") ||
                    //                                    modifier.contains("E2"))) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDL + "]</b> Please append one of these <b>relative modifiers : [LT, FA, F1, F2, F3, F4, TA, T1, T2, T3, T4, E1, E2] </b> \n");
                    //                            }
                    //                        }
                    //                        if (r1.equals("Bilateral")) {
                    //                            if (!modifier.contains("50")) {
                    //                                ErrorMsgs.add(" <b> Modifier </b> used with <b>Procedure : [" + ProcedureCode + "]</b> does not follow the laterality of <b>Diagnosis Code : [" + ICDL + "]</b> Please append  <b>relative modifier : 50 </b> \n");
                    //                            }
                    //                        }
                    //                    }


                    if (ProcedureCode.equals("69209") || ProcedureCode.equals("69210") || ProcedureCode.equals("G0268")) {
                        if (iCDRepository.find_ICD_between_Ranges(addChar("H612", '.', 3), addChar("H6123", '.', 3), ICDL) == 0) {
                            ErrorMsgs.add("<b>Non Supportive ICD : [" + ICDL + "]</b> Found With <b>Procedure Code [" + ProcedureCode + "]</b>, Please Report Supportive Diagnosis Code  i.e [<b>H61.20 - H61.23</b>] \n");
                        }
                    }
                }

                //                if (!isValidCLIACodes(conn, ProcedureCode)) {
                //                    if (modifier.contains("QW")) {
                //                        ErrorMsgs.add("<b>QW Modifier</b>  is not allowed with  <b>Procedure : [" + ProcedureCode + "]</b>\n");
                //                    }
                //                }


                //                //system.out.println("MODIFIER ->> " + modifier);
                if (!isEmpty(modifier) && (modifier.contains("59") && (modifier.contains("XE") || modifier.contains("XP") || modifier.contains("XS") || modifier.contains("XU")))) {
                    ErrorMsgs.add("<b>distinct procedural services modifiers</b> and <b>59</b> is incorrect to report of same line-item , please remove one of them\n");
                }


                if (consultantprocedureRepository.validateConsultantProceduresCodes(ProcedureCode) == 1) {//isValidConsultantProceduresCodes(conn, ProcedureCode)) {
                    if (isEmpty(ReferringProviderFirstName) && isEmpty(ReferringProviderLastName)) {
                        ErrorMsgs.add("<b>Referring provider</b>  is required with  <b>Consultation Procedure : [" + ProcedureCode + "]</b>\n");
                    }

                    if (!isEmpty(claim.getMemId()) && claim.getMemId().startsWith("MC") && !isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && PrimaryinsuranceDetailsById.getPayerID().equals("27094")) {
                        ErrorMsgs.add(" Patient has <b>Medicare</b> insurance type , We Cannot Bill <b>Consultation Codes</b> To This Plan.\n");
                    }
                }


                if (enmprocedureRepository.validateE_N_M_ProceduresCodes(ProcedureCode) == 1 && !errorMsgAdded) {//isValid_E_N_M_ProceduresCodes(conn, ProcedureCode) && !errorMsgAdded) {

                    ////system.out.println(" isValid_E_N_M_ProceduresCodes ProcedureCode -> " + ProcedureCode);

                    if (isENM_SurgeryProcedureCode) {
                        ErrorMsgs.add("<b> E&M Procedure </b> and  <b> E&M Surgery Codes </b> cannot be billed together on same DOS\n");
                        isENM_SurgeryProcedureCode = false;
                        isENMProcedureCode = false;
                        errorMsgAdded = true;
                    } else
                        isENMProcedureCode = true;
                }


                if (enmsurgerycodeRepository.validateE_N_M_Surgery_ProceduresCodes(ProcedureCode) == 1 && !errorMsgAdded) {
                    ////system.out.println("isValid_E_N_M_Surgery_ProceduresCodes ProcedureCode -> " + ProcedureCode);
                    if (isENMProcedureCode) {
                        ErrorMsgs.add("<b> E&M Procedure </b> and  <b> E&M Surgery Codes </b> cannot be billed together on same DOS\n");
                        isENM_SurgeryProcedureCode = false;
                        isENMProcedureCode = false;
                        errorMsgAdded = true;
                    } else
                        isENM_SurgeryProcedureCode = true;
                }


                if (espdtprocedureRepository.validateEPSDT_ProceduresCodes(ProcedureCode) == 1 && (PriFillingIndicator.equals("MC") || SecFillingIndicator.equals("MC"))
                        && (getAge(LocalDate.parse(_DOB)) < 21) && modifier.contains("EP")) {
                    ErrorMsgs.add("eligible medicaid recipient for <b>EPSDT</b> services  are less than <b>21 year</b> of age. patient  age is not appropriate to bill this service please remove modifier <b>EP</b>.\n");
                }

                if (espdtprocedureRepository.validateEPSDT_ProceduresCodes(ProcedureCode) == 1 && (!PriFillingIndicator.equals("MC") || !SecFillingIndicator.equals("MC"))) {
                    ErrorMsgs.add(" <b>EPSDT</b> Code is used it may be used on <b>MEDICAID</b> claims only \n");
                }

                if ((modifier.contains("GV") || modifier.contains("GW")) && (!PriFillingIndicator.equals("MB") || !SecFillingIndicator.equals("MB"))) {
                    ErrorMsgs.add("modifier <b> GW/GV </b> indicates hospice services, please file claim to <b>medicare</b> or remove the modifier\n");
                }

                if (ProcedureCodes_List.contains(ProcedureCode)) {
                    if (Integer.parseInt(Units) > 1) {
                        ErrorMsgs.add("Hospital Admission Service For Cpt <b> [" + ProcedureCode + "] </b> Should Always Billed As <b>One</b> Unit \n");
                    }

                    if (!ServiceFromDate.equals(ServiceToDate)) {
                        ErrorMsgs.add("Hospital Admission Service For Cpt <b> [" + ProcedureCode + "] </b> Start/End Dos Should Be Same \n");
                    }
                }

                if (ProcedureCode.equals("G0447")) {
                    //                            if()

                }

                if (ProcedureCodes_List_A_S_DATES_r1.contains(ProcedureCode) || ProcedureCodes_List_A_S_DATES_r2.contains(ProcedureCode)) {
                    if (claim.getClaimadditionalinfo().getHospitalizedFromDateAddInfo().equals(claim.getClaimadditionalinfo().getHospitalizedToDateAddInfo())) {
                        ErrorMsgs.add("Procedure Code <b>[" + ProcedureCode + "]</b> is inconsistent  \n");
                    }
                }

                if (cliaRepository.validateCLIACodes(ProcedureCode) == 1 && (modifier.contains("24") || modifier.contains("25"))) {
                    ErrorMsgs.add("<b>Modifier [ 24 , 25 ]</b> is <b>Not</b> allowed with  procedure <b>[" + ProcedureCode + "]</b> \n");
                }


                if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA")) ||
                        ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB"))) ||
                        ((PriFillingIndicator.equals("16") || SecFillingIndicator.equals("16")))) {

                    if (cliaRepository.validateCLIACodes(ProcedureCode) == 1 && !modifier.contains("QW")) {
                        ErrorMsgs.add("CLIA required procedure <b>[" + ProcedureCode + "]</b> found, Procedure may require <b>QW</b> modifier  \n");
                    }
                    //                    else if (modifier.contains("QW") && !isValidCLIACodes(conn, ProcedureCode)) {
                    //                        ErrorMsgs.add("<b>QW</b> modifier found, Modifier may require <b>CLIA</b>procedure code  \n");
                    //                    }


                    if (dmeHcpcRepository.validateDMEProceduresCodes(ProcedureCode) == 1) {
                        if (isEmpty(OrderingProvidersFirstName) && isEmpty(OrderingProvidersLastName)) {
                            ErrorMsgs.add("<b>Ordering provider</b>  is required with  <b>DME Procedure : [" + ProcedureCode + "]</b>\n");
                        }
                    }


                    //                            if ((ICDA.equals("Z23") || ICDB.equals("Z23") || ICDC.equals("Z23") ||
                    //                                    ICDD.equals("Z23") || ICDE.equals("Z23") || ICDF.equals("Z23")
                    //                                    || ICDG.equals("Z23") || ICDH.equals("Z23") || ICDI.equals("Z23")
                    //                                    || ICDJ.equals("Z23") || ICDK.equals("Z23") || ICDL.equals("Z23"))) {
                    //                                if (ProcedureCode.equals("G0008") || ProcedureCode.equals("G0009") || ProcedureCode.equals("G0010")) {
                    //                                    if(!containsValidRelativeAdminCode(conn,ProcedureCode,Charge_ProcedureCodes,"Z23")){
                    //                                        ErrorMsgs.add("<b>Bill Vaccine Code : "+getRespectiveVaccineCode(conn,ProcedureCode,"Z23").toString()+"</b>  is required to bill  <b>Relative Admin Code : [" + ProcedureCode + "]</b>\n");
                    //                                    }
                    //                                }
                    //
                    //
                    //                            }


                } else {
                    if (((!isEmpty(ICDA) && ICDA.equals("Z23"))
                            || (!isEmpty(ICDB) && ICDB.equals("Z23"))
                            || (!isEmpty(ICDC) && ICDC.equals("Z23"))
                            || (!isEmpty(ICDD) && ICDD.equals("Z23"))
                            || (!isEmpty(ICDE) && ICDE.equals("Z23"))
                            || (!isEmpty(ICDF) && ICDF.equals("Z23"))
                            || (!isEmpty(ICDG) && ICDG.equals("Z23"))
                            || (!isEmpty(ICDH) && ICDH.equals("Z23"))
                            || (!isEmpty(ICDI) && ICDI.equals("Z23"))
                            || (!isEmpty(ICDJ) && ICDJ.equals("Z23"))
                            || (!isEmpty(ICDK) && ICDK.equals("Z23"))
                            || (!isEmpty(ICDL) && ICDL.equals("Z23"))
                    )) {

                        if (vaccinecodescomponentRepository.validateVaccineCode(ProcedureCode) == 1) {
                            Charge_VaccineCodes.add(ProcedureCode);
                            sum_Of_units_Of_allVaccines += Integer.parseInt(Units);
                        }

                        if (ProcedureCode.equals("90472"))
                            units_Of_90472 = Integer.parseInt(Units);

                        if (ProcedureCode.equals("90460"))
                            units_Of_90460 = Integer.parseInt(Units);

                        if (ProcedureCode.equals("90461"))
                            units_Of_90461 = Integer.parseInt(Units);
                    }
                }

                if (noofunitshcpcRepository.isNotNoOfUnits(ProcedureCode) == 0) {
                    SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");

                    Date date1 = myFormat.parse(myFormat.format(fromUser.parse(ServiceFromDate)));
                    Date date2 = myFormat.parse(myFormat.format(fromUser.parse(ServiceToDate)));
                    long daysBetween = date2.getTime() - date1.getTime();
                    if (Double.parseDouble(Units) < TimeUnit.DAYS.convert(daysBetween, TimeUnit.MILLISECONDS)) {
                        ErrorMsgs.add("<b>Units</b> of  Procedure [<b>" + ProcedureCode + "</b>] is <b>InValid</b> <b>Units per day</b> must be <b>greater</b> than <b>1</b> \n");
                    }
                    //system.out.println("daysBetween -> " + daysBetween);
                    //system.out.println("daysBetween -> " + TimeUnit.DAYS.convert(daysBetween, TimeUnit.MILLISECONDS));
                }


                if (pqrscodeRepository.validatePQRSCodes(ProcedureCode) == 1 && Double.parseDouble(Amount) != 0) {
                    ErrorMsgs.add("<b>PQRS : [" + ProcedureCode + "]</b> Procedure found, <b>Amount</b> should be <b>equal</b> to 0 \n");
                } else if (Double.parseDouble(Amount) <= 0) {
                    ErrorMsgs.add(" <b>Amount</b> should be <b>greater</b> than 0 for Procedure [" + ProcedureCode + "]\n");
                }


                if (ndcCptCrosswalkRepository.validateNDC_Code(ProcedureCode) > 0) {
                    if (claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo() == null) {//ChargesInput[i][14].compareTo("NULL") == 0) {
                        ErrorMsgs.add("<b>NDC information</b> is <b>missing</b> for Procedure <b>[" + ProcedureCode + "]</b>. it must be used when reporting for <b>drug</b>\n");
                    } else {
                        //                    obj = mapper.readValue(ChargesInput[i][14], JsonNode.class);
                        if (claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().getDrugCode().equals("")) {
                            ErrorMsgs.add("<b>National Drug Code</b> is <b>missing</b> for Procedure <b>[" + ProcedureCode + "]</b> . it must be used when reporting for <b>drug</b>\n");
                        } else {
                            if (!claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().getDrugCode().replace("/", "").matches(NDC_REGEX) || ndcCptCrosswalkRepository.validateNDC_Code_Format(ProcedureCode, claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().getDrugCode()) == 0) {
                                ErrorMsgs.add("<b>National Drug Code</b> is <b>inValid</b> for Procedure <b>[" + ProcedureCode + "]</b>\n");
                            }
                        }
                        if (claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().getDrugUnit().equals("")) {
                            ErrorMsgs.add("<b>National Drug Quantity</b> is <b>missing</b> for Procedure <b>[" + ProcedureCode + "]</b> . it must be used when reporting for <b>drug</b>\n");
                        }
                    }
                }

                if (cPTRepository.validateOFFICE_E_N_M(ProcedureCode) == 2 && (WELL_VISIT_ICDs.contains(ICDA) || WELL_VISIT_ICDs.contains(ICDB) || WELL_VISIT_ICDs.contains(ICDC) ||
                        WELL_VISIT_ICDs.contains(ICDD) || WELL_VISIT_ICDs.contains(ICDE) || WELL_VISIT_ICDs.contains(ICDF)
                        || WELL_VISIT_ICDs.contains(ICDG) || WELL_VISIT_ICDs.contains(ICDH) || WELL_VISIT_ICDs.contains(ICDI)
                        || WELL_VISIT_ICDs.contains(ICDJ) || WELL_VISIT_ICDs.contains(ICDK) || WELL_VISIT_ICDs.contains(ICDL))) {
                    ErrorMsgs.add("Procedure Code <b>[" + ProcedureCode + "]</b>  is not <b>billable</b> with wellness ICDs \n");
                }

                if ((PriFillingIndicator.equals("CI") || SecFillingIndicator.equals("CI"))) {
                    if (cPTRepository.find_CPT_between_Ranges("99381", "99397", ProcedureCode) > 0 && iCDRepository.find_ICD_between_Ranges(addChar("Z0000", '.', 3), addChar("Z139", '.', 3), ICDA) == 0) {
                        ErrorMsgs.add("<b>ICD A</b> must be <b>Well visit</b> diagnose i.e  <b>Z00 - Z13.9</b> \n");
                    }

                }

                if ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB"))) {
                    if (WELL_VISIT_CPTs.contains(ProcedureCode) && iCDRepository.find_ICD_between_Ranges(addChar("Z0000", '.', 3), addChar("Z139", '.', 3), ICDA) == 0) {
                        ErrorMsgs.add("<b>ICD A</b> must be <b>Well visit</b> diagnose i.e  <b>Z00 - Z13.9</b> \n");
                    }

                    if (cPTRepository.find_CPT_between_Ranges("99381", "99397", ProcedureCode) > 0 || cPTRepository.find_CPT_between_Ranges("90460", "90461", ProcedureCode) > 0) {
                        ErrorMsgs.add("<b>Well visit CPT/Vaccine</b> is not allowed by  <b>MEDICARE</b> \n");
                    }
                }

                //                if (find_CPT_between_Ranges(conn, "90476", "90749", ProcedureCode) && !((!isEmpty(ICDB) && ICDA.equals("Z23"))
                //                        || (!isEmpty(ICDB) && ICDB.equals("Z23"))
                //                        || (!isEmpty(ICDC) && ICDC.equals("Z23"))
                //                        || (!isEmpty(ICDD) && ICDD.equals("Z23"))
                //                        || (!isEmpty(ICDE) && ICDE.equals("Z23"))
                //                        || (!isEmpty(ICDF) && ICDF.equals("Z23"))
                //                        || (!isEmpty(ICDG) && ICDG.equals("Z23"))
                //                        || (!isEmpty(ICDH) && ICDH.equals("Z23"))
                //                        || (!isEmpty(ICDI) && ICDI.equals("Z23"))
                //                        || (!isEmpty(ICDJ) && ICDJ.equals("Z23"))
                //                        || (!isEmpty(ICDK) && ICDK.equals("Z23"))
                //                        || (!isEmpty(ICDL) && ICDL.equals("Z23")))) {
                //                    ErrorMsgs.add("<b>Well visit Vaccine</b> should be billed with <b> Z23</b>\n");
                //                }

                if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA"))
                        || ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB")))) {
                    if (icodeRepository.validateI_Codes(ProcedureCode) == 1) {
                        ErrorMsgs.add("Procedure Code <b>[" + ProcedureCode + "]</b>  is  <b>I HCPCS Code</b> which is not payable by <b>Medicare</b>\n");
                    }
                }


                if (ReasonVisit.toUpperCase().contains("CORONA VIRUS") || ReasonVisit.toUpperCase().contains("COVID")) {
                    //                    if (!COVID_CPTS.contains(ProcedureCode) && !addCovidMsg) {
                    //                        ErrorMsgs.add("Please Bill COVID-19 codes <b>" + COVID_CPTS.toString() + "</b>  and  create separate charge for rest of the CPT Codes\n");
                    //                        addCovidMsg = true;
                    //                    }

                    if (ProcedureCode.equals("91303") && !is_91303) {
                        is_91303 = true;
                    }

                    if (!Arrays.asList("91300", "91301", "91302", "0031A").contains(ProcedureCode) && is_91303 && !addAdminCodeMsg) {
                        ErrorMsgs.add("<b>Admin Code " + Arrays.asList("91300", "91301", "91302", "0031A").toString() + " </b>  is  missing with  <b>Vaccine [91303]</b>\n");
                        addAdminCodeMsg = true;
                    }

                    if (COVID_CPTS_TJ.contains(ProcedureCode) && !modifier.contains("TJ")) {
                        ErrorMsgs.add("Per COVID Guidelines,  <b>Modifier : TJ </b>  is  required with CPT <b>[" + ProcedureCode + "]</b>\n");
                    }
                }


                if (cPTRepository.validateMammographyCPT(ProcedureCode) > 0 && (!modifier.contains("26") || isEmpty(modifier))) {
                    if (isEmpty(claim.getClaimadditionalinfo().getMemmoCertAddInfo())) {
                        ErrorMsgs.add("<b>FDA Certification Number</b> is Missing with Mammography services [<b>" + ProcedureCode + "</b>] Please check the location settings and update FDA Certification Number \n");
                    }
                }

                if (!isEmpty(ProcedureCode) && (ProcedureCode.equals("99238") || ProcedureCode.equals("99239"))) {
                    if (!Units.equals("1")) {
                        ErrorMsgs.add("<b>Unit</b> should be eq <b>required</b> when billed with Procedure  [<b>" + ProcedureCode + "</b>]  \n");
                    }
                    if (!ServiceToDate.equals(ServiceFromDate)) {
                        ErrorMsgs.add("<bSTART AND END DOS </b> should be <b>same</b> when billed with Procedure  [<b>" + ProcedureCode + "</b>]  \n");
                    }
                    if (isEmpty(claim.getClaimadditionalinfo().getHospitalizedToDateAddInfo())) {
                        ErrorMsgs.add("<b>Discharge Date </b> is <b>required</b> when billed with Procedure  [<b>" + ProcedureCode + "</b>]  \n");
                    }
                }


                //                if (((!isEmpty(ICDA) && ICDA.startsWith("F"))
                //                        || (!isEmpty(ICDB) && ICDB.startsWith("F"))
                //                        || (!isEmpty(ICDC) && ICDC.startsWith("F"))
                //                        || (!isEmpty(ICDD) && ICDD.startsWith("F"))
                //                        || (!isEmpty(ICDE) && ICDE.startsWith("F"))
                //                        || (!isEmpty(ICDF) && ICDF.startsWith("F"))
                //                        || (!isEmpty(ICDG) && ICDG.startsWith("F"))
                //                        || (!isEmpty(ICDH) && ICDH.startsWith("F"))
                //                        || (!isEmpty(ICDI) && ICDI.startsWith("F"))
                //                        || (!isEmpty(ICDJ) && ICDJ.startsWith("F"))
                //                        || (!isEmpty(ICDK) && ICDK.startsWith("F"))
                //                        || (!isEmpty(ICDL) && ICDL.startsWith("F"))
                //                )) {
                //                    if (isValid_E_N_M_ProceduresCodes(conn, ProcedureCode)) {
                //                        ErrorMsgs.add("<b>Mental Disorder</b> diagnosis may not be billed with E&Ms \n");
                //                    }
                //                }

                //                if (isValid_E_N_M_ProceduresCodes(conn, ProcedureCode)) {
                //                    if (!modifier.contains("24") && !modifier.contains("25") && !modifier.contains("57")) {
                //                        ErrorMsgs.add("<b>E&M Modifier</b> is required with E&Ms Procedures \n");
                //                    }
                //                }

                //                //system.out.println("PriFillingIndicator ->> " + PriFillingIndicator);
                if (PriFillingIndicator.equals("MA") || PriFillingIndicator.equals("MB")) {
                    if (ProcedureCodeModifier.contains(mod1) || ProcedureCodeModifier.contains(mod2) || ProcedureCodeModifier.contains(mod3) || ProcedureCodeModifier.contains(mod4)) {
                        ErrorMsgs.add("<b>Procedure Code Modifier</b> for Services rendered is <b>InValid</b> \n");
                    }
                }


                //                //system.out.println("Modifier ->> "+modifier);
                if (modifier.equals("")) {
                    Charge_ProcedureCodes_WITHOUT_MODIFIER.add(ProcedureCode);
                    //                    //system.out.println("Charge_ProcedureCodes_WITHOUT_MODIFIER");
                }


                Charge_ProcedureCodes.add(ProcedureCode);
            }

            if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA"))
                    || ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB")))) {
                for (String CPT :
                        Charge_ProcedureCodes) {
                    if (CPT.startsWith("S")) {
                        ErrorMsgs.add("<b>Procedure Code [" + CPT + "]</b> is <b>InValid</b> for <b>MEDICARE</b> \n");
                    }
                }
            }


            if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA")) || ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB"))) || ((PriFillingIndicator.equals("16") || SecFillingIndicator.equals("16")))) {
                if (((!isEmpty(ICDA) && ICDA.equals("Z23"))
                        || (!isEmpty(ICDB) && ICDB.equals("Z23"))
                        || (!isEmpty(ICDC) && ICDC.equals("Z23"))
                        || (!isEmpty(ICDD) && ICDD.equals("Z23"))
                        || (!isEmpty(ICDE) && ICDE.equals("Z23"))
                        || (!isEmpty(ICDF) && ICDF.equals("Z23"))
                        || (!isEmpty(ICDG) && ICDG.equals("Z23"))
                        || (!isEmpty(ICDH) && ICDH.equals("Z23"))
                        || (!isEmpty(ICDI) && ICDI.equals("Z23"))
                        || (!isEmpty(ICDJ) && ICDJ.equals("Z23"))
                        || (!isEmpty(ICDK) && ICDK.equals("Z23"))
                        || (!isEmpty(ICDL) && ICDL.equals("Z23"))
                )) {
                    if (Charge_ProcedureCodes.contains("G0009")) {
                        if (!containsValidRelativeAdminCode("G0009", Charge_ProcedureCodes, "Z23")) {
                            ErrorMsgs.add("<b>Bill Vaccine Code : " + mcrvaccinerulecodeRepository.getRespectiveVaccineCode("G0009", "Z23").toString() + "</b>  is required to bill  <b>Relative Admin Code : [G0009]</b>\n");
                        }
                    }
                    if (Charge_ProcedureCodes.contains("G0010")) {
                        if (!containsValidRelativeAdminCode("G0010", Charge_ProcedureCodes, "Z23")) {
                            ErrorMsgs.add("<b>Bill Vaccine Code : " + mcrvaccinerulecodeRepository.getRespectiveVaccineCode("G0010", "Z23").toString() + "</b>  is required to bill  <b>Relative Admin Code : [G0010]</b>\n");
                        }
                    }
                    if (Charge_ProcedureCodes.contains("G0008")) {
                        if (!containsValidRelativeAdminCode("G0008", Charge_ProcedureCodes, "Z23")) {
                            ErrorMsgs.add("<b>Bill Vaccine Code : " + mcrvaccinerulecodeRepository.getRespectiveVaccineCode("G0008", "Z23").toString() + "</b>  is required to bill  <b>Relative Admin Code : [G0008]</b>\n");
                        }
                    }

                }
            } else {
                if (((!isEmpty(ICDA) && ICDA.equals("Z23"))
                        || (!isEmpty(ICDB) && ICDB.equals("Z23"))
                        || (!isEmpty(ICDC) && ICDC.equals("Z23"))
                        || (!isEmpty(ICDD) && ICDD.equals("Z23"))
                        || (!isEmpty(ICDE) && ICDE.equals("Z23"))
                        || (!isEmpty(ICDF) && ICDF.equals("Z23"))
                        || (!isEmpty(ICDG) && ICDG.equals("Z23"))
                        || (!isEmpty(ICDH) && ICDH.equals("Z23"))
                        || (!isEmpty(ICDI) && ICDI.equals("Z23"))
                        || (!isEmpty(ICDJ) && ICDJ.equals("Z23"))
                        || (!isEmpty(ICDK) && ICDK.equals("Z23"))
                        || (!isEmpty(ICDL) && ICDL.equals("Z23"))
                )) {
                    if (Charge_ProcedureCodes.contains("90471")) {
                        if (Charge_VaccineCodes.size() > 1) {
                            if (!Charge_ProcedureCodes.contains("90472")) {
                                if ((Charge_VaccineCodes.size() - 1) == 1)
                                    ErrorMsgs.add(" <b>Additional Vaccine of " + (Charge_VaccineCodes.size() - 1) + "</b> Administration Code is <b>Missing</b>\n");
                                else
                                    ErrorMsgs.add(" <b>Additional Vaccine of " + (Charge_VaccineCodes.size() - 1) + "</b> Administration Codes are <b>Missing</b>\n");
                            } else if (Charge_ProcedureCodes.contains("90472") && (units_Of_90472 != (Charge_VaccineCodes.size() - 1))) {
                                ErrorMsgs.add(" <b>Units</b> of <b>90472</b> must be equal to number of additional vaccine codes\n");
                            }
                        }
                    } else {
                        ErrorMsgs.add(" <b>Primary Vaccine admin 90471</b> is <b>Missing</b>\n");
                    }
                }


                if (((!isEmpty(ICDA) && ICDA.equals("Z23"))
                        || (!isEmpty(ICDB) && ICDB.equals("Z23"))
                        || (!isEmpty(ICDC) && ICDC.equals("Z23"))
                        || (!isEmpty(ICDD) && ICDD.equals("Z23"))
                        || (!isEmpty(ICDE) && ICDE.equals("Z23"))
                        || (!isEmpty(ICDF) && ICDF.equals("Z23"))
                        || (!isEmpty(ICDG) && ICDG.equals("Z23"))
                        || (!isEmpty(ICDH) && ICDH.equals("Z23"))
                        || (!isEmpty(ICDI) && ICDI.equals("Z23"))
                        || (!isEmpty(ICDJ) && ICDJ.equals("Z23"))
                        || (!isEmpty(ICDK) && ICDK.equals("Z23"))
                        || (!isEmpty(ICDL) && ICDL.equals("Z23"))
                )) {


                    //system.out.println("No. of Vaccines -> " + Charge_VaccineCodes.size());
                    //system.out.println("Units units_Of_90460  -> " + units_Of_90460);
                    if (Charge_ProcedureCodes.contains("90460")) {
                        if (Charge_VaccineCodes.size() > 1) {
                            if (!Charge_ProcedureCodes.contains("90461")) {
                                if (Charge_VaccineCodes.size() == 1)
                                    ErrorMsgs.add(" <b>Vaccine Toxoid of " + Charge_VaccineCodes.size() + "</b> Administration Code is missing\n");
                                else
                                    ErrorMsgs.add(" <b>Vaccine Toxoid of " + Charge_VaccineCodes.size() + "</b> Administration Codes are missing\n");
                            } else if (Charge_ProcedureCodes.contains("90461") && (units_Of_90461 != sum_Of_units_Of_allVaccines)) {
                                ErrorMsgs.add(" <b>Units</b> of <b>90461</b> must be equal to number of additional vaccine codes\n");
                            }
                        }


                        if (units_Of_90460 != Charge_VaccineCodes.size()) {
                            ErrorMsgs.add(" <b>Units</b> of <b>90460</b> must be equal to number of additional vaccine codes\n");
                        }

                        if ((getAge(LocalDate.parse(_DOB)) > 18)) {
                            ErrorMsgs.add(" <b>Age</b> of patient must be less then or equal to <b> 18 Yrs </b> for <b>90460</b> & <b>90461</b>\n");
                        }
                    } else {
                        ErrorMsgs.add(" <b>Primary Vaccine admin 90460</b> is <b> Missing</b>\n");
                    }
                }
            }

            String vaccGrp1 = null, vaccGrp2 = null, r = null;
            String[] result = null;

            for (String CPT :
                    Charge_ProcedureCodes) {
                if (CPT.equals("G0009") || CPT.equals("G0010") || CPT.equals("G0008")) {
                    vaccGrp1 = CPT;
                }

                if (CPT.equals("90471") || CPT.equals("90472")) {
                    vaccGrp2 = CPT;
                }

                if (vaccGrp1 != null && vaccGrp2 != null) {
                    ErrorMsgs.add("System identifies medicare and commercial vaccine admins <b>[" + vaccGrp1 + "]</b> & <b>[" + vaccGrp2 + "]</b> alongside which is incorrect by coding point of view. please review coding before filing the claim\n");
                    break;
                }

                if (!findAddOnCode(CPT, Charge_ProcedureCodes)) {//O(n^2)
                    ErrorMsgs.add("Add On Code <b>[" + CPT + "]</b> cannot be billed without Primary Codes \n");
                }

                r = isNotAllowed_NCCI(CPT, Charge_ProcedureCodes);//O(n^2)
                result = r == null ? null : r.split("~");
                if (result != null) {
                    ErrorMsgs.add("Procedure Code <b>[" + result[0] + "]</b> is <b>not allowed</b> with <b>[" + result[1] + "]</b> in the Billing Guideline CCI. \n");
                }
            }

            if (Gender.equals("F")) {
                for (String ICD :
                        ICDs) {
                    if (genderspecificdiagnosisRepository.validateGenderICDs(ICD.replace(".", ""), "Male") > 0) {
                        ErrorMsgs.add("<b>Male ICD [" + ICD + "]</b> cannot be applied to <b>Female</b> Patient \n");
                    }
                }

                for (String CPT :
                        Charge_ProcedureCodes) {
                    if (genderspecificcptIcdRepository.validateGenderCPTs(CPT, "MALE") > 0) {
                        ErrorMsgs.add("<b>Male Procedure [" + CPT + "]</b> cannot be applied to <b>Female</b> Patient \n");
                    }
                }

            } else {

                for (String ICD :
                        ICDs) {
                    if (genderspecificdiagnosisRepository.validateGenderICDs(ICD.replace(".", ""), "Female") > 0) {
                        ErrorMsgs.add("<b>Female ICD [" + ICD + "]</b> cannot be applied to <b>Male</b> Patient  \n");
                    }
                }

                for (String CPT :
                        Charge_ProcedureCodes) {
                    if (genderspecificcptIcdRepository.validateGenderCPTs(CPT, "FEMALE") > 0) {
                        ErrorMsgs.add("<b>Female Procedure [" + CPT + "]</b> cannot be applied to <b>Male</b> Patient \n");
                    }
                }

            }

            for (String ICD :
                    ICDs) {
                if (!validAgeICDs(ICD, getAge(LocalDate.parse(_DOB)))) {
                    ErrorMsgs.add("<b> ICD [" + ICD + "]</b> cannot be applied to <b>Age : " + getAge(LocalDate.parse(_DOB)) + "</b>  \n");
                }
            }

            for (String ICD :
                    ICDs) {
                for (String CPT :
                        Charge_ProcedureCodes) {//O(n^2)
                    r = NCD_CPT_ICD_Denial(CPT, ICD);
                    result = r == null ? null : r.split("~");
                    if (result != null) {
                        ErrorMsgs.add("ICD <b>[" + result[0] + "]</b> is <b>Denied</b> with Procedure <b>[" + result[1] + "]</b> \n");
                    }


                    r = NCD_CPT_ICD_Med(CPT, ICD);
//                    //system.out.println("r ** "+r);
                    result = r == null ? null : r.split("~");
                    if (result != null) {
                        ErrorMsgs.add("<b>Medical Neccesity </b> is <b>required</b> when ICD <b>[" + result[0] + "]</b> is billed with Procedure <b>[" + result[1] + "]</b> \n");
                    }


                }
            }

            if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA"))
                    || ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB")))
                    || ((PriFillingIndicator.equals("16") || SecFillingIndicator.equals("16")))
                    || getAge(LocalDate.parse(_DOB)) >= 65) {
                for (String ICD :
                        ICDs) {
                    for (String CPT :
                            Charge_ProcedureCodes) {//O(n^2)
                        r = LCD_CPT_ICD_Denial(CPT, ICD);
                        //system.out.println("LCD_CPT_ICD_Denial ** " + r);
                        result = r == null ? null : r.split("~");
                        if (result != null) {
                            ErrorMsgs.add("Per <b>LCD</b> Articles , ICD <b>[" + result[0] + "]</b> is <b>Denied</b> with Procedure <b>[" + result[1] + "]</b> \n");
                        }

                        if ((PriFillingIndicator.equals("CI") || SecFillingIndicator.equals("CI"))) {
                            if (cPTRepository.find_CPT_between_Ranges("99201", "99215", CPT) > 0 && iCDRepository.find_ICD_between_Ranges("Z0000", "Z139", ICD) == 0) {
                                ErrorMsgs.add("<b>Well visit ICD : [" + ICD + "]</b> is not allowed with  <b>Procedure : [" + CPT + "]</b> \n");
                            }
                        }


                    }
                }


//                for (String CPT :
//                        Charge_ProcedureCodes) {
//
//                    r = LCD_CPT_ICD_MUST(conn, CPT, ICDs);
//                    //system.out.println("LCD_CPT_ICD_MUST ** " + r);
//                    if (r != null) {
//                        ErrorMsgs.add("Per <b>LCD</b> Articles , Supporting ICD for  Procedure <b>[" + r + "]</b> is <b>missing</b>  \n");
//                    }
//                }
            }

            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && (PrimaryinsuranceDetailsById.getPayerID().equals("87726") || PrimaryinsuranceDetailsById.getPayerID().equals("39026") || PrimaryinsuranceDetailsById.getPayerID().equals("13551") || PrimaryinsuranceDetailsById.getPayerID().equals("61101"))) {
                if (iCDRepository.validateSequelaCode(ICDA) > 0) {
                    ErrorMsgs.add("<b> SEQUELA ICD : [" + ICDA + "] </b> Can’t Be Billed As The Primary, First Listed, Or Principal Diagnosis On A Claim, Nor Can It Be The Only Diagnosis On A Claim, Please Check Coding Before Billing Out The Claim \n");
                }
            } else if (ICDs.size() == 1) {
                if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && (PrimaryinsuranceDetailsById.getPayerID().equals("87726") || PrimaryinsuranceDetailsById.getPayerID().equals("39026") || PrimaryinsuranceDetailsById.getPayerID().equals("13551") || PrimaryinsuranceDetailsById.getPayerID().equals("61101"))) {
                    if (iCDRepository.validateSequelaCode(ICDs.get(0)) > 0) {
                        ErrorMsgs.add("<b> SEQUELA ICD : [" + ICDs.get(0) + "] </b> Can’t Be Billed As The Primary, First Listed, Or Principal Diagnosis On A Claim, Nor Can It Be The Only Diagnosis On A Claim, Please Check Coding Before Billing Out The Claim \n");
                    }
                }
            }

            int xx = 0;
            for (String ICD :
                    ICDs) {

                if (xx == 0) {
                    xx++;
                    continue;
                }

//                //system.out.println("ICD -->> "+ICD);
                if (iCDRepository.find_ICD_between_Ranges(addChar("Z01810", '.', 3), addChar("Z01818", '.', 3), ICD) > 0) {
                    ErrorMsgs.add("<b> ICD [" + ICD + "]</b> may only be used at <b>Primary</b> Diagnosis Position \n");
                    break;
                }

            }


            for (String CPT :
                    Charge_ProcedureCodes_WITHOUT_MODIFIER) {

                r = Mod_Is_MUST_NCCI(CPT, Charge_ProcedureCodes_WITHOUT_MODIFIER); //O(n^2)
                result = r == null ? null : r.split("~");
                if (result != null) {
                    ErrorMsgs.add("Procedure Code <b>[" + result[0] + "]</b> is <b>inconsistent</b> with <b>[" + result[1] + "]</b> in the Billing Guideline CCI Modifier is allowed but not found \n");
                }

            }

            rulesList = gettingRulesFormatted(claim, rulesList, ErrorMsgs);


            Instant end = Instant.now();
            Duration ExecutionTime = Duration.between(start, end);
            if (ErrorMsgs.size() != 0)
                System.out.println(ErrorMsgs + "~" + ExecutionTime);
            else {
                System.out.println("1");
            }

            ErrorMsgs.add(String.valueOf(ErrorMsgs.size()));

        } catch (NumberFormatException | ParseException e) {
            ErrorMsgs.add("EXCEPTION occurs in SCRUBBER : " + new RuntimeException(e).getMessage());
//            return ErrorMsgs;
            throw new RuntimeException(e);
        }


        return rulesList;
    }

    public List<ScrubberRulesDto> gettingRulesFormatted(Claiminfomaster claim, List<ScrubberRulesDto> rulesList, List<String> ErrorMsgs) {
        if (ErrorMsgs.size() == 0) {
            List<ClaimAudittrail> rules = claimAudittrailRepository.findByClaimNoAndAction(claim.getClaimNumber(), "FIRED");
            for (ClaimAudittrail rule :
                    rules) {
                ScrubberRulesDto scrubberRulesDto = new ScrubberRulesDto();
                scrubberRulesDto.setId(rule.getId());
                scrubberRulesDto.setDescription(rule.getRuleText());
                rulesList.add(scrubberRulesDto);
            }
            return rulesList;
        }
        ErrorMsgs.replaceAll(s -> s.replace("\n", "").replace("\r", "").replaceAll(" +", " "));

        List<String> removedErrors = claimAudittrailRepository.findByClaimNoAndAction_NQ(claim.getClaimNumber(), "REMOVED");

        for (String e :
                removedErrors) {

            for (String errorMsg :
                    ErrorMsgs) {//O(n^2)
                if (errorMsg.equals(e)) {
                    ErrorMsgs.remove(e);
                    break;
                }
            }

        }


        claimAudittrailRepository.deleteClaimAudittrailByClaimNoAndAction(claim.getClaimNumber(), "FIRED");

        for (String error :
                ErrorMsgs) {
            ScrubberRulesDto scrubberRulesDto = new ScrubberRulesDto();
            scrubberRulesDto.setId(insertClaimAuditTrails(claim, error, "FIRED").getId());
            scrubberRulesDto.setDescription(error);
            rulesList.add(scrubberRulesDto);
        }

        return rulesList;
    }

    @Transactional
    @Override
    public List<?> scrubberInst(Claiminfomaster claim, int... changeDetected) {
        Instant start = Instant.now();
        List<ScrubberRulesDto> rulesList = new ArrayList<>();
        List<String> ErrorMsgs = new ArrayList<>();


        if (changeDetected.length==0 || changeDetected[0] == 0) {
            rulesList = gettingRulesFormatted(claim, rulesList, ErrorMsgs);
            if (rulesList.size() > 0) return rulesList;
        }
        claim = claimServiceInst.filterChargesWrtStatus(claim);

        String ReasonVisit = "";
        String PatientFirstName = "";
        String SSN = "";
        String PatientLastName = "";
        String DOB = "";
        String _DOB = "";
        String Gender = "";
        String City = "";
        String State = "";
        String StateCode = "";
        String ZipCode = "";
        int SelfPayChk = 0;
        String ClaimCreateDate = "";


        String Freq = "";
        String OrigClaim = "";

        String PriFillingIndicator = "";
        String SecFillingIndicator = "";

        String PriInsuranceName = "";
        String PriInsuranceNameId = "";
        String MemId = "";
        String PolicyType = "";
        String GrpNumber = "";
        String SecondaryInsurance = "";
        String SecondaryInsuranceId = "";
        String SecondaryInsuranceMemId = "";
        String SecondaryInsuranceGrpNumber = "";


        String ClientAddress = "";
        String ClientCity = "";
        String ClientTaxID = "";
        String ClientState = "";
        String ClientZipCode = "";
        String ClientPhone = "";
        String ClientNPI = "";
        String CLIA_number = "";
        String TaxanomySpecialty = "";


        String Payer_Address = "";
        String Payer_City = "";
        String Payer_State = "";
        String Payer_Zip = "";


        String PatientRelationtoPrimary = "";
        String PatientRelationtoSec = "";

        String PriInsuredName = "";
        String PriInsuredLastName = "";
        String PriInsuredFirstName = "";

        String SecInsuredName = "";
        String SecInsuredLastName = "";
        String SecInsuredFirstName = "";

        String DOS = "";


        String BillingProviderLastName = "";
        String BillingProviderFirstName = "";
        String BillingProviderNPI = "";
        String BillingProvider_Taxonomy = "";
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


        String[] Taxonomy = {"213ES0131X", "213EG0000X", "213EP1101X", "213EP0504X", "213ER0200X", "213ES0000X"};
        List<String> TaxonomyList = Arrays.asList(Taxonomy);
        String[] Taxonomy_for_InitialTreatment = {"111N00000X", "111NI0013X", "111NI0900X", "111NN0400X", "111NN1001X",
                "111NP0017X", "111NR0200X", "111NR0400X", "111NS0005X", "111NT0100X", "111NX0100X", "111NX0800X"};
        List<String> Taxonomy_for_InitialTreatment_List = Arrays.asList(Taxonomy_for_InitialTreatment);

        ArrayList<String> ICDs = new ArrayList<>();


        if (!isEmpty(claim.getClaiminformationcode().getPrincipalDiagInfoCodes())) {
            if (iCDRepository.validateICD(claim.getClaiminformationcode().getPrincipalDiagInfoCodes()) == 0) {
                ErrorMsgs.add("<b>Principal Diagnosis</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n~" + claim.getClaiminformationcode().getPrincipalDiagInfoCodes());
            }
            ICDs.add(claim.getClaiminformationcode().getPrincipalDiagInfoCodes());
        }
        if (!isEmpty(claim.getClaiminformationcode().getAdmittingDiagInfoCodes())) {
            if (iCDRepository.validateICD(claim.getClaiminformationcode().getAdmittingDiagInfoCodes()) == 0) {
                ErrorMsgs.add("<b>Admitting Diagnosis</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10\n~" + claim.getClaiminformationcode().getAdmittingDiagInfoCodes());
            }
            ICDs.add(claim.getClaiminformationcode().getAdmittingDiagInfoCodes());
        }

        claim.getClaiminfocodeextcauseinj().stream().filter(x -> !ICDs.contains(x)).forEach(x -> {
            ICDs.add(x.getCode());
            if (iCDRepository.validateICD(x.getCode()) == 0) {
                ErrorMsgs.add("<b>External Cause of Injury code [" + x.getCode() + "]</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10~codeextcauseinj~" + x.getCode() + "~" + x.getId());
            }
        });

        claim.getClaiminfocodereasvisit().stream().filter(x -> !ICDs.contains(x)).forEach(x -> {
            ICDs.add(x.getCode());
            if (iCDRepository.validateICD(x.getCode()) == 0) {
                ErrorMsgs.add("<b>Patient Reason For Visit code [" + x.getCode() + "]</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10~codereasvisit~" + x.getCode() + "~" + x.getId());
            }
        });

        claim.getClaiminfocodeothdiag().stream().filter(x -> !ICDs.contains(x)).forEach(x -> {
            ICDs.add(x.getCode());
            if (iCDRepository.validateICD(x.getCode()) == 0) {
                ErrorMsgs.add("<b>Other Diagnosis code [" + x.getCode() + "]</b> USED DOESNOT MATCH WITH Centers for Medicare & Medicaid Services (CMS). PLEASE USE THIS LINK FOR CORRECT ICD CODE: https://www.cms.gov/Medicare/Coding/ICD10~codeothdiag~" + x.getCode() + "~" + x.getId());
            }
        });

        try {
            if (!isEmpty(claim.getClientId())) {
                ClientDTO clientDetailsById = externalService.getClientDetailsById(claim.getClientId());
                ClientAddress = clientDetailsById.getAddress();
                ClientCity = clientDetailsById.getCity();
                ClientTaxID = clientDetailsById.getTaxid();
                ClientState = clientDetailsById.getState();
                ClientZipCode = clientDetailsById.getZipCode();
                ClientPhone = clientDetailsById.getPhone();
                ClientNPI = clientDetailsById.getNpi();
                TaxanomySpecialty = clientDetailsById.getTaxanomySpecialty();
            }

        } catch (Exception e) {
            ErrorMsgs.add("EXCEPTION in getting client details : " + new RuntimeException(e).getMessage());
            return ErrorMsgs;
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
                City = patientDetailsById.getCity();
                State = patientDetailsById.getState();
                ZipCode = patientDetailsById.getZipCode();
                ClaimCreateDate = String.valueOf(ZonedDateTime.now().format(ClaimCreateDateAndDOSformatter));
                PriInsuredFirstName = patientDetailsById.getPriInsurerFirstName();
                PriInsuredLastName = patientDetailsById.getPriInsurerLastName();
                PatientRelationtoPrimary = patientDetailsById.getPatientRelationtoPrimary();
                SecInsuredFirstName = patientDetailsById.getSecInsurerFirstName();
                SecInsuredLastName = patientDetailsById.getSecInsurerLastName();
                PatientRelationtoSec = patientDetailsById.getPatientRelationshiptoSecondry();
                ReasonVisit = patientDetailsById.getReasonVisit();

            } catch (Exception e) {
                ErrorMsgs.add("EXCEPTION in getting patient details : " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }


        if (!isEmpty(claim.getRenderingProvider())) {
            try {
                RenderingProviderDetailsById = externalService.getDoctorDetailsById(Long.parseLong(claim.getRenderingProvider()));

                RenderingProvidersLastName = RenderingProviderDetailsById.getDoctorsLastName().toUpperCase();
                RenderingProvidersFirstName = RenderingProviderDetailsById.getDoctorsFirstName().toUpperCase();
                RenderingProvidersNPI = RenderingProviderDetailsById.getNpi();
                RenderingProviders_Taxonomy = RenderingProviderDetailsById.getTaxonomySpecialty();
            } catch (NumberFormatException e) {
                ErrorMsgs.add("EXCEPTION in getting rendering provider details" + new RuntimeException(e).getMessage());
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
            } catch (NumberFormatException e) {
                ErrorMsgs.add("EXCEPTION in getting billing provider details" + new RuntimeException(e).getMessage());
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
            } catch (NumberFormatException e) {
                ErrorMsgs.add("EXCEPTION in getting referring provider details" + new RuntimeException(e).getMessage());
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
            } catch (NumberFormatException e) {
                ErrorMsgs.add("EXCEPTION in getting ordering provider details " + new RuntimeException(e).getMessage());
                return ErrorMsgs;
            }
        }


        if (PatientRelationtoPrimary.compareTo("Self") != 0) {
            if (isEmpty(PriInsuredFirstName) && isEmpty(PriInsuredLastName)) {
                ErrorMsgs.add("<b>Primary Insurer Name</b> is <b>Missing</b>\n");
            }
        }

        if (!isEmpty(DOB) && !isEmpty(DOS)) {
            if (isInValidDate(DOB, DOS)) {
                ErrorMsgs.add("DOS cannot be prior to the DOB, correction is required");
            }
        }

        if (!isEmpty(BillingProvider_Taxonomy)) {
            if (isInValidTaxonomy(BillingProvider_Taxonomy)) {
                ErrorMsgs.add("Billing Provider <b>Taxonomy Code</b> is  <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>Taxonomy Code</b> is <b>Missing</b>\n");
        }

        final String NPI_REGEX = "[0-9]{10}";

        if (!isEmpty(BillingProviderNPI)) {
            if (BillingProviderNPI.matches(NPI_REGEX)) {
                if (!isValid(BillingProviderNPI))
                    ErrorMsgs.add("Billing Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                            "* Length of the NPI should be 10 digits\"> ");
            } else {
                ErrorMsgs.add("Billing Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                        "* Length of the NPI should be 10 digits\"> ");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>NPI</b> is <b>Missing</b>\n");
        }

        final String Name_REGEX = "^([A-Z0-9 ]{2,100})$";
        if (!isEmpty(BillingProviderLastName)) {
            if (!BillingProviderLastName.matches(Name_REGEX)) {
                ErrorMsgs.add("Billing Provider <b>LastName</b> is <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>LastName</b> is <b>Missing</b>\n");
        }

        if (!isEmpty(BillingProviderFirstName)) {
            if (!BillingProviderFirstName.matches(Name_REGEX)) {
                ErrorMsgs.add("Billing Provider <b>FirstName</b> is <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>FirstName</b> is <b>Missing</b>\n");
        }

        if (!isEmpty(ClientAddress)) {
            if (isInValidAddress(ClientAddress)) {
                ErrorMsgs.add("Billing Provider <b>Address/b> Cannot be <b>PO BOX</b> Address. Only Physical Address Is <b>Allowed</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>Address</b> is <b>Missing</b>\n");
        }


        if (!isEmpty(ClientZipCode)) {
            if (ClientZipCode.contains("-")) {
                String[] ClientZipCodes = ClientZipCode.split("-");
                if (isInValidZipCode(ClientState, ClientZipCodes[0])) {
                    ErrorMsgs.add("Billing Provider <b>ZipCode</b> does not match with <b>State</b>\n");
                }
            } else {
                if (isInValidZipCode(ClientState, ClientZipCode)) {
                    ErrorMsgs.add("Billing Provider <b>ZipCode</b> does not match with <b>State</b>\n");
                }
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>ZipCode</b> is <b>Missing</b>\n");
        }

        final String CITY_REGEX = "^([0-9, A-Z, a-z]{2,30})$";
        if (!isEmpty(ClientCity)) {
            if (!ClientCity.matches(CITY_REGEX)) {
                ErrorMsgs.add("Billing Provider <b>City</b> is <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>City</b> is <b>Missing</b>\n");
        }


        final String BillingProvider_TaxID_LENGTH_REGEX = "([0-9]{9})";
        final String BillingProvider_TaxID_SEQUENCE_REGEX = "(?=012345678|987654321|098765432|000000000|111111111|222222222|333333333|444444444|555555555|666666666|777777777|888888888|999999999|123456789).{9}";
        if (!isEmpty(ClientTaxID)) {
            if (ClientTaxID.matches(BillingProvider_TaxID_LENGTH_REGEX)) {
                if (ClientTaxID.matches(BillingProvider_TaxID_SEQUENCE_REGEX)) {
                    ErrorMsgs.add("Billing Provider <b>Tax ID</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Billing Provider <b>Tax ID</b> is <b>InValid</b>\n");
            }
        } else {
            ErrorMsgs.add("Billing Provider <b>Tax ID</b> is <b>Missing</b>\n");
        }

        InsuranceDTO PrimaryinsuranceDetailsById = new InsuranceDTO();
        try {
            if (!isEmpty(claim.getPriInsuranceNameId())) {
                PrimaryinsuranceDetailsById = externalService.getInsuranceDetailsById(claim.getPriInsuranceNameId());
                PriFillingIndicator = PrimaryinsuranceDetailsById.getClaimIndicator_P();
                Payer_Address = PrimaryinsuranceDetailsById.getAddress();
                Payer_City = PrimaryinsuranceDetailsById.getCity();
                Payer_State = PrimaryinsuranceDetailsById.getState();
                Payer_Zip = PrimaryinsuranceDetailsById.getZip();

            }
        } catch (NumberFormatException e) {
            ErrorMsgs.add("EXCEPTION in getting Primary Insurance Details" + new RuntimeException(e).getMessage());
            return ErrorMsgs;
        }

        InsuranceDTO SecondaryinsuranceDetailsById = new InsuranceDTO();
        try {
            if (!isEmpty(claim.getSecondaryInsuranceId())) {
                SecondaryinsuranceDetailsById = externalService.getInsuranceDetailsById(claim.getSecondaryInsuranceId());
                SecFillingIndicator = SecondaryinsuranceDetailsById.getClaimIndicator_P();
            }
        } catch (NumberFormatException e) {
            ErrorMsgs.add("EXCEPTION in getting Secondary Insurance Details" + new RuntimeException(e).getMessage());
            return ErrorMsgs;
        }


        try {


            if (isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && isEmpty(SecondaryinsuranceDetailsById.getPayerID())) {
                ErrorMsgs.add("<b>Insurance </b> is <b>Missing</b> \n");
            }


            if (!isEmpty(PriFillingIndicator) && (PriFillingIndicator.equals("MB") || PriFillingIndicator.equals("MA"))) {

                //                if (PatientRelationshipCode.compareTo("18") != 0) {
                //                    ErrorMsgs.add("<b>Subscriber Relationship</b>  must be   <b>Self</b>\n");
                //                }

                if (!isEmpty(ReferringProviderNPI) && !isEmpty(BillingProviderNPI)) {

                    if (orderreferringRepository.validateOrderingOrRefferingProvider(ReferringProviderLastName, ReferringProviderNPI) == 0) {
                        ErrorMsgs.add("<b>Referring provider </b> is <b>Invalid</b> \n");
                    }

                }


                if (!isEmpty(OrderingProvidersNPI) && !isEmpty(BillingProviderNPI)) {
                    if (orderreferringRepository.validateOrderingOrRefferingProvider(OrderingProvidersLastName, OrderingProvidersNPI) == 0) {
                        ErrorMsgs.add("<b>Ordering provider </b> is <b>Invalid</b> \n");
                    }
                }
            }


            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && (PrimaryinsuranceDetailsById.getPayerID().equals("84146") || PrimaryinsuranceDetailsById.getPayerID().equals("31114"))) {
                if (PatientRelationtoPrimary.compareTo("Self") != 0 || PatientRelationtoSec.compareTo("Self") != 0) {
                    ErrorMsgs.add("<b>Subscriber Relationship</b>  must be   <b>Self</b>\n");
                }
            }

            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && (PriFillingIndicator.equals("MB") || PriFillingIndicator.equals("MA") || PriFillingIndicator.equals("MC"))) {
                if (PrimaryinsuranceDetailsById.getPayerID().equals("DNC00")) {
                    if (!isEmpty(GrpNumber)) {
                        ErrorMsgs.add("Subscriber <b>group number</b> not allowed for <b>Medicare</b> and <b>Medicaid</b> \n");
                    }
                }
            }

            if (!isEmpty(PatientLastName)) {
                if (!PatientLastName.matches(Name_REGEX)) {
                    ErrorMsgs.add("Subscriber  <b>LastName</b> is <b>InValid</b>\n");
                }

                if (PatientLastName.equalsIgnoreCase("TEST") || PatientLastName.equalsIgnoreCase("DEMO")) {
                    ErrorMsgs.add("THE PATIENT SEEMS TO BE A TEST PATIENT . DO NOT SUBMIT CLAIM WITHOUT DUE VERIFICATION \n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>LastName</b> is <b>Missing</b>\n");
            }

            if (!isEmpty(PatientFirstName)) {
                if (!PatientFirstName.matches(Name_REGEX)) {
                    ErrorMsgs.add("Subscriber <b>FirstName</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>FirstName</b> is <b>Missing</b>\n");
            }


            final String MemberID_REGEX777 = "^([V]{1})+([0-9]{8})$";//payerid 77073

            if (!isEmpty(claim.getMemId()) && PrimaryinsuranceDetailsById.getPayerID().equals("77073")) {
                if (!claim.getMemId().matches(MemberID_REGEX777)) {
                    ErrorMsgs.add("subscriber id for VNS choice should start with “V” proceeded with 8 digits numeric\n");
                }
            }

            final String MemberID_REGEX = "^(123456789|^(TEST)|^[0]{2,}|^[1]{2,}|^[2]{2,}|^[3]{2,}|^[4]{2,}|^[5]{2,}|^[6]{2,}|^[7]{2,}|^[8]{2,}|^[9]{2,})$";
            final String MemberID_REGEX999 = "^[^@!$\\-\\/+*.,<>&%#]*$";

//                    MemId
            if (!isEmpty(claim.getMemId())) {
                if (claim.getMemId().matches(MemberID_REGEX) || !claim.getMemId().matches(MemberID_REGEX999)) {
                    ErrorMsgs.add("Subscriber <b>Member-ID</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>Member-ID</b> is <b>Missing</b>\n");
            }

            if (!isEmpty(ZipCode)) {
                if (isInValidZipCode(State, ZipCode)) {
                    ErrorMsgs.add("Subscriber <b>ZipCode</b> does not match with <b>State</b>\n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>ZipCode</b> is <b>Missing</b>\n");
            }

            if (!isEmpty(City)) {
                if (!City.matches(CITY_REGEX)) {
                    ErrorMsgs.add("Subscriber <b>City</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Subscriber <b>City</b> is <b>Missing</b>\n");
            }

            String SubscriberDOB = "19261111";
            //system.out.println("DOB -> " + DOB);
            if (!isEmpty(DOB) && !isEmpty(ClaimCreateDate)) {
                if (Integer.parseInt(DOB) > Integer.parseInt(ClaimCreateDate)) {
                    ErrorMsgs.add("Subscriber <b>DOB</b> is <b>InValid</b>\n");
                }
            }

            final String MemberID_REGEX000 = "^(YUB|YUX|XOJ|XOD|ZGJ|ZGD|YIJ|YID|YDJ|YDL)+([A-Z0-9])*$";//payerid 77073
            if (!isEmpty(DOS)) {
                if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID())
                        && (PrimaryinsuranceDetailsById.getPayerID().equals("00621") || PrimaryinsuranceDetailsById.getPayerID().equals("00840") || PrimaryinsuranceDetailsById.getPayerID().equals("84980") || PrimaryinsuranceDetailsById.getPayerID().equals("00790"))
                        && !isEmpty(claim.getMemId())
                        && Integer.parseInt(DOS) > 20170101) {
                    if (claim.getMemId().toUpperCase().matches(MemberID_REGEX000)) {
                        ErrorMsgs.add("Medicare Advantage Claim need to be resubmitter with <b>PayerID : 66006</b> \n");
                    }
                }
            }

            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && PrimaryinsuranceDetailsById.getPayerID().equals("66006") && !isEmpty(claim.getMemId()) && Integer.parseInt(DOS) > 20170101) {
                if (claim.getMemId().startsWith("YUB") || claim.getMemId().startsWith("YUX") || claim.getMemId().startsWith("XOJ") || claim.getMemId().startsWith("XOD") || claim.getMemId().startsWith("ZGJ") ||
                        claim.getMemId().startsWith("ZGD") || claim.getMemId().startsWith("YIJ") || claim.getMemId().startsWith("YID") || claim.getMemId().startsWith("YDJ") || claim.getMemId().startsWith("YDL")) {
                    ErrorMsgs.add("Medicare Advantage Claim need to be resubmitter with <b>PayerID : 66006</b> when <b>secondary</b> insurance is <b>Medicare</b>\n");
                }
            }


            if (!isEmpty(PriFillingIndicator) && PriFillingIndicator.equals("BL") && !isEmpty(claim.getMemId())) {
                if (claim.getMemId().startsWith("TFC") || claim.getMemId().startsWith("CUX") || claim.getMemId().startsWith("DSN") || claim.getMemId().startsWith("FMW") || claim.getMemId().startsWith("HAR") ||
                        claim.getMemId().startsWith("IPW") || claim.getMemId().startsWith("KER") || claim.getMemId().startsWith("NUZ") || claim.getMemId().startsWith("NVC") || claim.getMemId().startsWith("XNE")
                        || claim.getMemId().startsWith("XNH") || claim.getMemId().startsWith("XNJ") || claim.getMemId().startsWith("XNN") || claim.getMemId().startsWith("XNV")) {
                    if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && !PrimaryinsuranceDetailsById.getPayerID().equals("00611"))
                        ErrorMsgs.add(" <b>Subscriber ID</b> belongs to <b>Regence Blue Shield</b> please submit the claim to  <b>Payer ID : 00611</b>\n");
                }
                //                else if (!claimDto.getMemId().startsWith("TFC") && !claimDto.getMemId().startsWith("CUX") && !claimDto.getMemId().startsWith("DSN") && !claimDto.getMemId().startsWith("FMW") && !claimDto.getMemId().startsWith("HAR") ||
                //                        !claimDto.getMemId().startsWith("IPW") && !claimDto.getMemId().startsWith("KER") && !claimDto.getMemId().startsWith("NUZ") && !claimDto.getMemId().startsWith("NVC") && !claimDto.getMemId().startsWith("XNE")
                //                                && !claimDto.getMemId().startsWith("XNH") && !claimDto.getMemId().startsWith("XNJ") && !claimDto.getMemId().startsWith("XNN") && !claimDto.getMemId().startsWith("XNV")) {
                //                    if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && !PrimaryinsuranceDetailsById.getPayerID().equals("00054"))
                //                        ErrorMsgs.add(" <b>Subscriber ID</b> belongs to <b>Blue Cross Of Idaho</b> please submit the claim to  <b>Payer ID : 00054</b>\n");
                //                }
                else if (!isEmpty(claim.getMemId()) && (claim.getMemId().startsWith("YLS89") || claim.getMemId().startsWith("NYC"))) {
                    ErrorMsgs.add(" This Claim Does Not Belongs To <b>BCBS</b> ,Patient’s Policy Covers Only Hospital Benefits, Please Confirm Insurance For Medical Benefits.\n");
                }
            }


            if (!isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && PrimaryinsuranceDetailsById.getPayerID().equals("87726")) {
                if (!isEmpty(claim.getMemId()) && claim.getMemId().startsWith("7") && (claim.getMemId().length() == 7)) {
                    ErrorMsgs.add(" <b>Subscriber ID</b> belongs to <b>Student Resources Insurance </b> please submit the claim to  <b>Payer ID : 74227</b>\n");
                }
            }


            if (!isEmpty(Payer_Zip)) {
                if (isInValidZipCode(Payer_State, Payer_Zip)) {
                    ErrorMsgs.add("Payer <b>ZipCode</b> does not match with <b>State</b>\n");
                }
            } else {
                ErrorMsgs.add("Payer <b>ZipCode</b> is <b>Missing</b>\n");
            }

            if (!isEmpty(Payer_City)) {
                if (!Payer_City.matches(CITY_REGEX)) {
                    ErrorMsgs.add("Payer <b>City</b> is <b>InValid</b>\n");
                }
            } else {
                ErrorMsgs.add("Payer <b>City</b> is <b>Missing</b>\n");
            }


            String Related_Causes_Code = null;

            if (!isEmpty(claim.getClaimadditionalinfo().getAutoAccidentAddInfo()) && claim.getClaimadditionalinfo().getAutoAccidentAddInfo().equals("1")) {
                Related_Causes_Code = "AA";
            } else if (!isEmpty(claim.getClaimadditionalinfo().getEmploymentStatusAddInfo()) && claim.getClaimadditionalinfo().getEmploymentStatusAddInfo().equals("1")) {
                Related_Causes_Code = "EM";
            } else if (!isEmpty(claim.getClaimadditionalinfo().getOtherAccidentAddInfo()) && claim.getClaimadditionalinfo().getOtherAccidentAddInfo().equals("1")) {
                Related_Causes_Code = "OA";
            }


            if (!isEmpty(ReferringProviderFirstName)) {
                if (!isEmpty(ReferringProviderNPI)) {
                    if (ReferringProviderNPI.matches(NPI_REGEX)) {
                        if (!isValid(ReferringProviderNPI))
                            ErrorMsgs.add("Referring Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                    "* Length of the NPI should be 10 digits\"> ");
                    } else {
                        ErrorMsgs.add("Referring Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                "* Length of the NPI should be 10 digits\"> ");
                    }
                } else {
                    ErrorMsgs.add("Referring Provider <b>NPI</b> is <b>Missing</b>\n");
                }


                if (!isEmpty(ReferringProviderLastName)) {
                    if (!ReferringProviderLastName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Referring Provider <b>LastName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Referring Provider <b>LastName</b> is <b>Missing</b>\n");
                }

                if (!isEmpty(ReferringProviderFirstName)) {
                    if (!ReferringProviderFirstName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Referring Provider <b>FirstName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Referring Provider <b>FirstName</b> is <b>Missing</b>\n");
                }


                if (PrimaryinsuranceDetailsById.getPayerID().equals("11315") || PrimaryinsuranceDetailsById.getPayerID().equals("87726")) {
                    if (ReferringProviderNPI.equals(BillingProviderNPI)) {
                        ErrorMsgs.add("Referring Provider  is <b>Missing/InValid</b>\n");
                    }
                }
            }

            if (!isEmpty(RenderingProvidersFirstName)) {


                // 2310B RENDERING PROVIDER NAME
                //                Loop loop_2310B = loop_2300.addChild("2300");

                if (!isEmpty(RenderingProvidersNPI)) {
                    if (RenderingProvidersNPI.matches(NPI_REGEX)) {
                        if (!isValid(RenderingProvidersNPI))
                            ErrorMsgs.add("Rendering Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                    "* Length of the NPI should be 10 digits\"> ");


                        if (!isEmpty(ClientNPI)) {
                            if (ClientNPI.equals(RenderingProvidersNPI)) {
                                ErrorMsgs.add("Rendering Provider and Service Location <b> NPI</b> cannot be  <b>Same</b>\n");
                            }
                        }

                        //                        if (!isEmpty(BillingProviderNPI)) {
                        //                            if (BillingProviderNPI.equals(RenderingProvidersNPI)) {
                        //                                ErrorMsgs.add("Rendering Provider and Billing Provider <b> NPI</b> cannot be  <b>Same</b>\n");
                        //                            }
                        //                        }

                    } else {
                        ErrorMsgs.add("Rendering Provider <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                "* Length of the NPI should be 10 digits\"> ");
                    }
                } else {
                    ErrorMsgs.add("Rendering Provider <b>NPI</b> is <b>Missing</b>\n");
                }

                if (!isEmpty(RenderingProvidersLastName)) {
                    if (!RenderingProvidersLastName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Rendering Provider <b>LastName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Rendering Provider <b>LastName</b> is <b>Missing</b>\n");
                }

                if (!isEmpty(RenderingProvidersFirstName)) {
                    if (!RenderingProvidersFirstName.matches(Name_REGEX)) {
                        ErrorMsgs.add("Rendering Provider <b>FirstName</b> is <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Rendering Provider <b>FirstName</b> is <b>Missing</b>\n");
                }


                //                loop_2310B.addSegment("NM1*82*1*" + RenderingProvidersLastName + "*" + RenderingProvidersFirstName + "****XX*" + RenderingProvidersNPI);//NM1 Rendering PROVIDER
                //                String RenderingProviders_Taxonomy = "225100000X";

                if (!isEmpty(RenderingProviders_Taxonomy)) {
                    if (isInValidTaxonomy(RenderingProviders_Taxonomy)) {
                        ErrorMsgs.add("Rendering Provider <b>Taxonomy Code</b> is  <b>InValid</b>\n");
                    }
                } else {
                    ErrorMsgs.add("Rendering Provider <b>Taxonomy Code</b> is <b>Missing</b>\n");
                }
                //                loop_2310B.addSegment("PRV*PE*PXC*" + RenderingProviders_Taxonomy); //PRV Rendering Provider SPECIALTY


            }

            if (!isEmpty(ClientNPI)) {
                if (ClientNPI.matches(NPI_REGEX)) {
                    if (!isValid(ClientNPI))
                        ErrorMsgs.add("Client <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                                "* Length of the NPI should be 10 digits\"> ");
                } else {
                    ErrorMsgs.add("Client <b>NPI</b> is <b>InValid</b><span data-toggle=\"tooltip\" title=\"Mentioned below might be the causes\n* NPI should contain Numeric Characters \n" +
                            "* Length of the NPI should be 10 digits\"> ");
                }

                if (!isEmpty(BillingProviderNPI)) {
                    if (ClientNPI.equals(BillingProviderNPI)) {
                        ErrorMsgs.add("<b>Billing Provider NPI </b> and <b>Client NPI</b> cannot be <b>same</b> \n");
                    }
                }
            } else {
                ErrorMsgs.add("Client  <b>NPI</b> is <b>Missing</b>\n");
            }


            Double totalcharges = 0.0;
            String POS = "";
            ArrayList<String> POS_list_AMBULANCE_CLAIMS = new ArrayList<String>(Arrays.asList("21", "51", "61", "11", "12", "13", "22", "31", "32", "65"));
            ArrayList<String> POS_list = new ArrayList<String>(Arrays.asList("41", "42", "99"));
            ArrayList<String> POS_list_radiology = new ArrayList<String>(Arrays.asList("21", "22", "23", "24", "26", "31", "34", "41", "42", "51", "52", "53", "56", "61"));
            ArrayList<String> ProcedureCodes_List = new ArrayList<String>(Arrays.asList("99221", "99222", "99223"));
            ArrayList<String> ProcedureCodes_List_A_S_DATES_r1 = new ArrayList<String>(Arrays.asList("99221", "99222", "99223"));
            ArrayList<String> ProcedureCodes_List_A_S_DATES_r2 = new ArrayList<String>(Arrays.asList("99231", "99232", "99233"));
            ArrayList<String> Charge_ProcedureCodes = new ArrayList<String>();
            ArrayList<String> Charge_ProcedureCodes_WITHOUT_MODIFIER = new ArrayList<String>();
            ArrayList<String> Charge_VaccineCodes = new ArrayList<String>();
            ArrayList<String> WELL_VISIT_ICDs = new ArrayList<String>(Arrays.asList("Z00.00", "Z00.01", "Z00.110", "Z00.111", "Z00.121", "Z00.129", "Z01.411",
                    "Z01.419", "Z30.015", "Z30.016", "Z30.44", "Z30.45"));
            ArrayList<String> WELL_VISIT_CPTs = new ArrayList<String>(Arrays.asList("G0402", "G0438", "G0439"));
            ArrayList<String> COVID_CPTS = new ArrayList<String>(Arrays.asList("91300", "91301", "0001A", "0002A", "0011A", "0012A", "91302", "91303", "0021A", "0022A", "0031A", "0003A", "0013A", "M0201", "99401", "0041A", "0042A", "0051A", "0052A", "0053A", "0054A", "0071A",
                    "0072A", "91304", "91305", "91307", "91306", "0064A", "0004A", "0034A"));

            ArrayList<String> COVID_CPTS_TJ = new ArrayList<String>(Arrays.asList("90471", "0001A", "0002A", "0003A", "0004A", "0011A", "0012A", "0013A", "0021A", "0022A", "0031A", "0034A", "0041A", "0042A", "0051A", "0052A", "0053A", "0054A", "0064A", "0071A", "0072A", "90662", "90672", "90674", "90682", "90685", "90686", "90687", "90688", "90694", "90756",
                    "90653", "91300", "91301", "91302", "91303", "91304", "91305", "91306", "91307"));

            ArrayList<String> ProcedureCodeModifier = new ArrayList<String>(Arrays.asList("A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "GY", "GZ", "H9", "HA", "HB", "HC", "HD", "HE", "HF", "HG", "HH", "HI", "HJ", "HK", "HL", "HM", "HN", "HO", "HP", "HQ", "HR", "HS", "HT", "HU", "HV", "HW", "HX", "HY", "HZ", "SA",
                    "SB", "SC", "SD", "SE", "SH", "SJ", "SK", "SL", "ST", "SU", "SV", "TD", "TE", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TP", "TQ", "TR", "TS", "TW", "U1", "U2", "U3", "U4", "U5", "U6", "U7", "U8", "U9", "UA", "UB", "UC", "UD"));


            String modifier = "";
            String DXPointer = "";
            String MeasurementCode = null;
            String ProcedureCode = null;
            String revCode = "";
            String NDC_REGEX = "^[0-9A-Z]{11}$";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String Units = null;
            String Amount = null;
            String ServiceFromDate = null;
            String ServiceToDate = null;
            String mod1 = null;
            String mod2 = null;
            String mod3 = null;
            String mod4 = null;
            String MajorSurgeryDOS = null;
            String EnMSurgeryDOS = null;
            boolean isENMProcedureCode = false;
            boolean isMajorSurgeryProcedureCode = false;
            boolean addCovidMsg = false;
            boolean addAdminCodeMsg = false;
            boolean is_91303 = false;
            boolean errorMsgAdded = false;
            boolean isENM_SurgeryProcedureCode = false;


            for (int i = 0; i < claim.getClaimchargesinfo().size(); i++) {
                modifier = "";
                Units = String.valueOf(claim.getClaimchargesinfo().get(i).getUnits());
                Amount = String.valueOf(claim.getClaimchargesinfo().get(i).getAmount());//ChargesInput[i][12];
                ProcedureCode = claim.getClaimchargesinfo().get(i).getHCPCSProcedure();//ChargesInput[i][2];
                mod1 = claim.getClaimchargesinfo().get(i).getMod1();//ChargesInput[i][5];
                mod2 = claim.getClaimchargesinfo().get(i).getMod2();//ChargesInput[i][6];
                mod3 = claim.getClaimchargesinfo().get(i).getMod3();//ChargesInput[i][7];
                mod4 = claim.getClaimchargesinfo().get(i).getMod4();//ChargesInput[i][8];
                revCode = claim.getClaimchargesinfo().get(i).getRevCode();

                if (!isEmpty(ProcedureCode)) {
                    if (cPTRepository.validateCPT(ProcedureCode) == 0) {
                        ErrorMsgs.add("<b>HCPCS [" + ProcedureCode + "]</b> USED DOESNOT MATCH WITH American Medical Association (AMA). PLEASE USE THIS LINK FOR CORRECT HCPCS CODE: https://www.cms.gov/medicare/fraud-and-abuse/physicianselfreferral/list_of_codes\n");
                    }
                } else {
                    ErrorMsgs.add("<b>HCPCS</b> is Missing. It cannot be null or empty\n");
                }

                if (!isEmpty(revCode)) {
                    if (revenueCodeRepository.validateRevCode(revCode) == 0) {
                        ErrorMsgs.add("<b>REVENUE CODE</b> USED DOESNOT MATCH WITH NATIONAL UNIFORM BILLING COMMITTEE (NUBC). PLEASE USE THIS LINK FOR CORRECT REVENUE CODE: https://med.noridianmedicare.com/web/jfa/topics/claim-submission/revenue-codes\n");
                    }
                } else {
                    ErrorMsgs.add("<b>REVENUE CODE</b> is Missing. It cannot be null or empty\n");
                }


                if (enmprocedureRepository.validateE_N_M_ProceduresCodes(ProcedureCode) == 1) {//isValid_E_N_M_ProceduresCodes(conn, ProcedureCode)) {
                    if (!isEmpty(mod1)) {
                        if (enmNotallowedModifierRepository.validateE_N_M_Modifiers(mod1) > 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod1 + " ]</b> is <b>not allowed </b> with E&M Procedure code <b>[ " + ProcedureCode + " ]</b>\n");
                        }
                    }
                    if (!isEmpty(mod2)) {
                        if (enmNotallowedModifierRepository.validateE_N_M_Modifiers(mod2) > 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod2 + " ]</b> is <b>not allowed </b> with E&M Procedure code <b>[ " + ProcedureCode + " ]</b>\n");
                        }
                    }
                    if (!isEmpty(mod3)) {
                        if (enmNotallowedModifierRepository.validateE_N_M_Modifiers(mod3) > 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod3 + " ]</b> is <b>not allowed </b> with E&M Procedure code <b>[ " + ProcedureCode + " ]</b>\n");
                        }
                    }
                    if (!isEmpty(mod4)) {
                        if (enmNotallowedModifierRepository.validateE_N_M_Modifiers(mod4) > 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod4 + " ]</b> is <b>not allowed </b> with E&M Procedure code <b>[ " + ProcedureCode + " ]</b>\n");
                        }
                    }
                }


                if (anesthesiacodeRepository.validateAnesthesiaCodes(ProcedureCode) == 1) {
                    MeasurementCode = "MJ";
                    if (!isEmpty(mod1)) {//mod1
                        modifier += ":" + mod1; //SV101-3
                        if (Units.equals("1") && mod1.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }

                        if (anesthesiamodifierRepository.validateAnesthesiaModifier(mod1) == 0) {//isValidAnesthesiaModifier(conn, mod1)) {
                            ErrorMsgs.add("Modifier <b>[ " + mod1 + " ]</b> is <b>not allowed </b> <b>Anesthesia Modifier</b>  is only allowed with  <b>Anesthesia Procedures [" + ProcedureCode + "]</b>\n");
                        }


                    }
                    if (!isEmpty(mod2)) {//mod2
                        modifier += ":" + mod2;
                        if (Units.equals("1") && mod2.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }

                        if (anesthesiamodifierRepository.validateAnesthesiaModifier(mod2) == 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod2 + " ]</b> is <b>not allowed </b> <b>Anesthesia Modifier</b>  is only allowed with  <b>Anesthesia Procedures [" + ProcedureCode + "]</b>\n");
                        }
                    }
                    if (!mod3.equals("")) {//mod3
                        modifier += ":" + mod3;
                        if (Units.equals("1") && mod3.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }


                        if (anesthesiamodifierRepository.validateAnesthesiaModifier(mod3) == 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod3 + " ]</b> is <b>not allowed </b>b>Anesthesia Modifier</b>  is only allowed with  <b>Anesthesia Procedures [" + ProcedureCode + "]</b>\n");
                        }
                    }
                    if (!mod4.equals("")) {//mod4
                        modifier += ":" + mod4;
                        if (Units.equals("1") && mod4.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }


                        if (anesthesiamodifierRepository.validateAnesthesiaModifier(mod4) == 0) {
                            ErrorMsgs.add("Modifier <b>[ " + mod4 + " ]</b> is <b>not allowed </b> <b>Anesthesia Modifier</b>  is only allowed with  <b>Anesthesia Procedures [" + ProcedureCode + "]</b>\n");
                        }
                    }


                } else {
                    MeasurementCode = "UN";
                    if (!isEmpty(mod1)) {//mod1
                        modifier += ":" + mod1; //SV101-3
                        if (claim.getClientId() == 32) {
                            if (Units.equals("1") && mod1.equals("50")) {
                                ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                            }
                        }
                    }
                    if (!isEmpty(mod2)) {//mod2
                        modifier += ":" + mod2;
                        if (Units.equals("1") && mod2.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }
                    }
                    if (!isEmpty(mod3)) {//mod3
                        modifier += ":" + mod3;
                        if (Units.equals("1") && mod3.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }
                    }
                    if (!isEmpty(mod4)) {//mod4
                        modifier += ":" + mod4;
                        if (Units.equals("1") && mod4.equals("50")) {
                            ErrorMsgs.add("Units must be greater than <b>ONE</b> when a modifier of <b>50</b> is used.\n");
                        }
                    }
                }


                //                if (!isValidCLIACodes(conn, ProcedureCode)) {
                //                    if (modifier.contains("QW")) {
                //                        ErrorMsgs.add("<b>QW Modifier</b>  is not allowed with  <b>Procedure : [" + ProcedureCode + "]</b>\n");
                //                    }
                //                }


                //                //system.out.println("MODIFIER ->> " + modifier);
                if (!isEmpty(modifier) && (modifier.contains("59") && (modifier.contains("XE") || modifier.contains("XP") || modifier.contains("XS") || modifier.contains("XU")))) {
                    ErrorMsgs.add("<b>distinct procedural services modifiers</b> and <b>59</b> is incorrect to report of same line-item , please remove one of them\n");
                }


                if (consultantprocedureRepository.validateConsultantProceduresCodes(ProcedureCode) == 1) {//isValidConsultantProceduresCodes(conn, ProcedureCode)) {
                    if (isEmpty(ReferringProviderFirstName) && isEmpty(ReferringProviderLastName)) {
                        ErrorMsgs.add("<b>Referring provider</b>  is required with  <b>Consultation Procedure : [" + ProcedureCode + "]</b>\n");
                    }

                    if (!isEmpty(claim.getMemId()) && claim.getMemId().startsWith("MC") && !isEmpty(PrimaryinsuranceDetailsById.getPayerID()) && PrimaryinsuranceDetailsById.getPayerID().equals("27094")) {
                        ErrorMsgs.add(" Patient has <b>Medicare</b> insurance type , We Cannot Bill <b>Consultation Codes</b> To This Plan.\n");
                    }
                }


                if (enmprocedureRepository.validateE_N_M_ProceduresCodes(ProcedureCode) == 1 && !errorMsgAdded) {//isValid_E_N_M_ProceduresCodes(conn, ProcedureCode) && !errorMsgAdded) {

                    ////system.out.println(" isValid_E_N_M_ProceduresCodes ProcedureCode -> " + ProcedureCode);

                    if (isENM_SurgeryProcedureCode) {
                        ErrorMsgs.add("<b> E&M Procedure </b> and  <b> E&M Surgery Codes </b> cannot be billed together on same DOS\n");
                        isENM_SurgeryProcedureCode = false;
                        isENMProcedureCode = false;
                        errorMsgAdded = true;
                    } else
                        isENMProcedureCode = true;
                }


                if (enmsurgerycodeRepository.validateE_N_M_Surgery_ProceduresCodes(ProcedureCode) == 1 && !errorMsgAdded) {
                    ////system.out.println("isValid_E_N_M_Surgery_ProceduresCodes ProcedureCode -> " + ProcedureCode);
                    if (isENMProcedureCode) {
                        ErrorMsgs.add("<b> E&M Procedure </b> and  <b> E&M Surgery Codes </b> cannot be billed together on same DOS\n");
                        isENM_SurgeryProcedureCode = false;
                        isENMProcedureCode = false;
                        errorMsgAdded = true;
                    } else
                        isENM_SurgeryProcedureCode = true;
                }


                if (espdtprocedureRepository.validateEPSDT_ProceduresCodes(ProcedureCode) == 1 && (PriFillingIndicator.equals("MC") || SecFillingIndicator.equals("MC"))
                        && (getAge(LocalDate.parse(_DOB)) < 21) && modifier.contains("EP")) {
                    ErrorMsgs.add("eligible medicaid recipient for <b>EPSDT</b> services  are less than <b>21 year</b> of age. patient  age is not appropriate to bill this service please remove modifier <b>EP</b>.\n");
                }

                if (espdtprocedureRepository.validateEPSDT_ProceduresCodes(ProcedureCode) == 1 && (!PriFillingIndicator.equals("MC") || !SecFillingIndicator.equals("MC"))) {
                    ErrorMsgs.add(" <b>EPSDT</b> Code is used it may be used on <b>MEDICAID</b> claims only \n");
                }

                if ((modifier.contains("GV") || modifier.contains("GW")) && (!PriFillingIndicator.equals("MB") || !SecFillingIndicator.equals("MB"))) {
                    ErrorMsgs.add("modifier <b> GW/GV </b> indicates hospice services, please file claim to <b>medicare</b> or remove the modifier\n");
                }

                if (ProcedureCodes_List.contains(ProcedureCode)) {
                    if (Integer.parseInt(Units) > 1) {
                        ErrorMsgs.add("Hospital Admission Service For Cpt <b> [" + ProcedureCode + "] </b> Should Always Billed As <b>One</b> Unit \n");
                    }

                }

                if (ProcedureCode.equals("G0447")) {
                    //                            if()

                }

                if (ProcedureCodes_List_A_S_DATES_r1.contains(ProcedureCode) || ProcedureCodes_List_A_S_DATES_r2.contains(ProcedureCode)) {
                    if (claim.getClaimadditionalinfo().getHospitalizedFromDateAddInfo().equals(claim.getClaimadditionalinfo().getHospitalizedToDateAddInfo())) {
                        ErrorMsgs.add("Procedure Code <b>[" + ProcedureCode + "]</b> is inconsistent  \n");
                    }
                }

                if (cliaRepository.validateCLIACodes(ProcedureCode) == 1 && (modifier.contains("24") || modifier.contains("25"))) {
                    ErrorMsgs.add("<b>Modifier [ 24 , 25 ]</b> is <b>Not</b> allowed with  procedure <b>[" + ProcedureCode + "]</b> \n");
                }


                if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA")) ||
                        ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB"))) ||
                        ((PriFillingIndicator.equals("16") || SecFillingIndicator.equals("16")))) {

                    if (cliaRepository.validateCLIACodes(ProcedureCode) == 1 && !modifier.contains("QW")) {
                        ErrorMsgs.add("CLIA required procedure <b>[" + ProcedureCode + "]</b> found, Procedure may require <b>QW</b> modifier  \n");
                    }
                    //                    else if (modifier.contains("QW") && !isValidCLIACodes(conn, ProcedureCode)) {
                    //                        ErrorMsgs.add("<b>QW</b> modifier found, Modifier may require <b>CLIA</b>procedure code  \n");
                    //                    }


                    if (dmeHcpcRepository.validateDMEProceduresCodes(ProcedureCode) == 1) {
                        if (isEmpty(OrderingProvidersFirstName) && isEmpty(OrderingProvidersLastName)) {
                            ErrorMsgs.add("<b>Ordering provider</b>  is required with  <b>DME Procedure : [" + ProcedureCode + "]</b>\n");
                        }
                    }


                    //                            if ((ICDA.equals("Z23") || ICDB.equals("Z23") || ICDC.equals("Z23") ||
                    //                                    ICDD.equals("Z23") || ICDE.equals("Z23") || ICDF.equals("Z23")
                    //                                    || ICDG.equals("Z23") || ICDH.equals("Z23") || ICDI.equals("Z23")
                    //                                    || ICDJ.equals("Z23") || ICDK.equals("Z23") || ICDL.equals("Z23"))) {
                    //                                if (ProcedureCode.equals("G0008") || ProcedureCode.equals("G0009") || ProcedureCode.equals("G0010")) {
                    //                                    if(!containsValidRelativeAdminCode(conn,ProcedureCode,Charge_ProcedureCodes,"Z23")){
                    //                                        ErrorMsgs.add("<b>Bill Vaccine Code : "+getRespectiveVaccineCode(conn,ProcedureCode,"Z23").toString()+"</b>  is required to bill  <b>Relative Admin Code : [" + ProcedureCode + "]</b>\n");
                    //                                    }
                    //                                }
                    //
                    //
                    //                            }


                }


                if (pqrscodeRepository.validatePQRSCodes(ProcedureCode) == 1 && Double.parseDouble(Amount) != 0) {
                    ErrorMsgs.add("<b>PQRS : [" + ProcedureCode + "]</b> Procedure found, <b>Amount</b> should be <b>equal</b> to 0 \n");
                } else if (Double.parseDouble(Amount) <= 0) {
                    ErrorMsgs.add(" <b>Amount</b> should be <b>greater</b> than 0 for Procedure [" + ProcedureCode + "]\n");
                }


                if (ndcCptCrosswalkRepository.validateNDC_Code(ProcedureCode) > 0) {
                    if (claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo() == null) {//ChargesInput[i][14].compareTo("NULL") == 0) {
                        ErrorMsgs.add("<b>NDC information</b> is <b>missing</b> for Procedure <b>[" + ProcedureCode + "]</b>. it must be used when reporting for <b>drug</b>\n");
                    } else {
                        //                    obj = mapper.readValue(ChargesInput[i][14], JsonNode.class);
                        if (claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().getDrugCode().equals("")) {
                            ErrorMsgs.add("<b>National Drug Code</b> is <b>missing</b> for Procedure <b>[" + ProcedureCode + "]</b> . it must be used when reporting for <b>drug</b>\n");
                        } else {
                            if (!claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().getDrugCode().replace("/", "").matches(NDC_REGEX) || ndcCptCrosswalkRepository.validateNDC_Code_Format(ProcedureCode, claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().getDrugCode()) == 0) {
                                ErrorMsgs.add("<b>National Drug Code</b> is <b>inValid</b> for Procedure <b>[" + ProcedureCode + "]</b>\n");
                            }
                        }
                        if (claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().getDrugUnit().equals("")) {
                            ErrorMsgs.add("<b>National Drug Quantity</b> is <b>missing</b> for Procedure <b>[" + ProcedureCode + "]</b> . it must be used when reporting for <b>drug</b>\n");
                        }
                    }
                }


                if ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB"))) {
                    if (cPTRepository.find_CPT_between_Ranges("99381", "99397", ProcedureCode) > 0 || cPTRepository.find_CPT_between_Ranges("90460", "90461", ProcedureCode) > 0) {
                        ErrorMsgs.add("<b>Well visit CPT/Vaccine</b> is not allowed by  <b>MEDICARE</b> \n");
                    }
                }


                if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA"))
                        || ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB")))) {
                    if (icodeRepository.validateI_Codes(ProcedureCode) == 1) {
                        ErrorMsgs.add("Procedure Code <b>[" + ProcedureCode + "]</b>  is  <b>I HCPCS Code</b> which is not payable by <b>Medicare</b>\n");
                    }
                }


                if (ReasonVisit.toUpperCase().contains("CORONA VIRUS") || ReasonVisit.toUpperCase().contains("COVID")) {
                    //                    if (!COVID_CPTS.contains(ProcedureCode) && !addCovidMsg) {
                    //                        ErrorMsgs.add("Please Bill COVID-19 codes <b>" + COVID_CPTS.toString() + "</b>  and  create separate charge for rest of the CPT Codes\n");
                    //                        addCovidMsg = true;
                    //                    }

                    if (ProcedureCode.equals("91303") && !is_91303) {
                        is_91303 = true;
                    }

                    if (!Arrays.asList("91300", "91301", "91302", "0031A").contains(ProcedureCode) && is_91303 && !addAdminCodeMsg) {
                        ErrorMsgs.add("<b>Admin Code " + Arrays.asList("91300", "91301", "91302", "0031A").toString() + " </b>  is  missing with  <b>Vaccine [91303]</b>\n");
                        addAdminCodeMsg = true;
                    }

                    if (COVID_CPTS_TJ.contains(ProcedureCode) && !modifier.contains("TJ")) {
                        ErrorMsgs.add("Per COVID Guidelines,  <b>Modifier : TJ </b>  is  required with CPT <b>[" + ProcedureCode + "]</b>\n");
                    }
                }




                if (PriFillingIndicator.equals("MA") || PriFillingIndicator.equals("MB")) {
                    if (ProcedureCodeModifier.contains(mod1) || ProcedureCodeModifier.contains(mod2) || ProcedureCodeModifier.contains(mod3) || ProcedureCodeModifier.contains(mod4)) {
                        ErrorMsgs.add("<b>Procedure Code Modifier</b> for Services rendered is <b>InValid</b> \n");
                    }
                }


                //                //system.out.println("Modifier ->> "+modifier);
                if (modifier.equals("")) {
                    Charge_ProcedureCodes_WITHOUT_MODIFIER.add(ProcedureCode);
                    //                    //system.out.println("Charge_ProcedureCodes_WITHOUT_MODIFIER");
                }


                Charge_ProcedureCodes.add(ProcedureCode);
            }

            if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA"))
                    || ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB")))) {
                for (String CPT :
                        Charge_ProcedureCodes) {
                    if (CPT.startsWith("S")) {
                        ErrorMsgs.add("<b>Procedure Code [" + CPT + "]</b> is <b>InValid</b> for <b>MEDICARE</b> \n");
                    }
                }
            }


            String vaccGrp1 = null, vaccGrp2 = null, r = null;
            String[] result = null;

            for (String CPT :
                    Charge_ProcedureCodes) {
                if (CPT.equals("G0009") || CPT.equals("G0010") || CPT.equals("G0008")) {
                    vaccGrp1 = CPT;
                }

                if (CPT.equals("90471") || CPT.equals("90472")) {
                    vaccGrp2 = CPT;
                }

                if (vaccGrp1 != null && vaccGrp2 != null) {
                    ErrorMsgs.add("System identifies medicare and commercial vaccine admins <b>[" + vaccGrp1 + "]</b> & <b>[" + vaccGrp2 + "]</b> alongside which is incorrect by coding point of view. please review coding before filing the claim\n");
                    break;
                }

                if (!findAddOnCode(CPT, Charge_ProcedureCodes)) {//O(n^2)
                    ErrorMsgs.add("Add On Code <b>[" + CPT + "]</b> cannot be billed without Primary Codes \n");
                }

                r = isNotAllowed_NCCI(CPT, Charge_ProcedureCodes);//O(n^2)
                result = r == null ? null : r.split("~");
                if (result != null) {
                    ErrorMsgs.add("Procedure Code <b>[" + result[0] + "]</b> is <b>not allowed</b> with <b>[" + result[1] + "]</b> in the Billing Guideline CCI. \n");
                }
            }

            if (Gender.equals("F")) {
                for (String ICD :
                        ICDs) {
                    if (genderspecificdiagnosisRepository.validateGenderICDs(ICD.replace(".", ""), "Male") > 0) {
                        ErrorMsgs.add("<b>Male ICD [" + ICD + "]</b> cannot be applied to <b>Female</b> Patient \n");
                    }
                }

                for (String CPT :
                        Charge_ProcedureCodes) {
                    if (genderspecificcptIcdRepository.validateGenderCPTs(CPT, "MALE") > 0) {
                        ErrorMsgs.add("<b>Male Procedure [" + CPT + "]</b> cannot be applied to <b>Female</b> Patient \n");
                    }
                }

            } else {

                for (String ICD :
                        ICDs) {
                    if (genderspecificdiagnosisRepository.validateGenderICDs(ICD.replace(".", ""), "Female") > 0) {
                        ErrorMsgs.add("<b>Female ICD [" + ICD + "]</b> cannot be applied to <b>Male</b> Patient  \n");
                    }
                }

                for (String CPT :
                        Charge_ProcedureCodes) {
                    if (genderspecificcptIcdRepository.validateGenderCPTs(CPT, "FEMALE") > 0) {
                        ErrorMsgs.add("<b>Female Procedure [" + CPT + "]</b> cannot be applied to <b>Male</b> Patient \n");
                    }
                }

            }

            for (String ICD :
                    ICDs) {
                if (!validAgeICDs(ICD, getAge(LocalDate.parse(_DOB)))) {
                    ErrorMsgs.add("<b> ICD [" + ICD + "]</b> cannot be applied to <b>Age : " + getAge(LocalDate.parse(_DOB)) + "</b>  \n");
                }
            }

            for (String ICD :
                    ICDs) {
                for (String CPT :
                        Charge_ProcedureCodes) {//O(n^2)
                    r = NCD_CPT_ICD_Denial(CPT, ICD);
                    result = r == null ? null : r.split("~");
                    if (result != null) {
                        ErrorMsgs.add("ICD <b>[" + result[0] + "]</b> is <b>Denied</b> with Procedure <b>[" + result[1] + "]</b> \n");
                    }


                    r = NCD_CPT_ICD_Med(CPT, ICD);
//                    //system.out.println("r ** "+r);
                    result = r == null ? null : r.split("~");
                    if (result != null) {
                        ErrorMsgs.add("<b>Medical Neccesity </b> is <b>required</b> when ICD <b>[" + result[0] + "]</b> is billed with Procedure <b>[" + result[1] + "]</b> \n");
                    }


                }
            }

            if ((PriFillingIndicator.equals("MA") || SecFillingIndicator.equals("MA"))
                    || ((PriFillingIndicator.equals("MB") || SecFillingIndicator.equals("MB")))
                    || ((PriFillingIndicator.equals("16") || SecFillingIndicator.equals("16")))
                    || getAge(LocalDate.parse(_DOB)) >= 65) {
                for (String ICD :
                        ICDs) {
                    for (String CPT :
                            Charge_ProcedureCodes) {//O(n^2)
                        r = LCD_CPT_ICD_Denial(CPT, ICD);
                        //system.out.println("LCD_CPT_ICD_Denial ** " + r);
                        result = r == null ? null : r.split("~");
                        if (result != null) {
                            ErrorMsgs.add("Per <b>LCD</b> Articles , ICD <b>[" + result[0] + "]</b> is <b>Denied</b> with Procedure <b>[" + result[1] + "]</b> \n");
                        }

                        if ((PriFillingIndicator.equals("CI") || SecFillingIndicator.equals("CI"))) {
                            if (cPTRepository.find_CPT_between_Ranges("99201", "99215", CPT) > 0 && iCDRepository.find_ICD_between_Ranges("Z0000", "Z139", ICD) == 0) {
                                ErrorMsgs.add("<b>Well visit ICD : [" + ICD + "]</b> is not allowed with  <b>Procedure : [" + CPT + "]</b> \n");
                            }
                        }


                    }
                }


//                for (String CPT :
//                        Charge_ProcedureCodes) {
//
//                    r = LCD_CPT_ICD_MUST(conn, CPT, ICDs);
//                    //system.out.println("LCD_CPT_ICD_MUST ** " + r);
//                    if (r != null) {
//                        ErrorMsgs.add("Per <b>LCD</b> Articles , Supporting ICD for  Procedure <b>[" + r + "]</b> is <b>missing</b>  \n");
//                    }
//                }
            }


            int xx = 0;
            for (String ICD :
                    ICDs) {

                if (xx == 0) {
                    xx++;
                    continue;
                }

//                //system.out.println("ICD -->> "+ICD);
                if (iCDRepository.find_ICD_between_Ranges(addChar("Z01810", '.', 3), addChar("Z01818", '.', 3), ICD) > 0) {
                    ErrorMsgs.add("<b> ICD [" + ICD + "]</b> may only be used at <b>Primary</b> Diagnosis Position \n");
                    break;
                }

            }


            for (String CPT :
                    Charge_ProcedureCodes_WITHOUT_MODIFIER) {

                r = Mod_Is_MUST_NCCI(CPT, Charge_ProcedureCodes_WITHOUT_MODIFIER); //O(n^2)
                result = r == null ? null : r.split("~");
                if (result != null) {
                    ErrorMsgs.add("Procedure Code <b>[" + result[0] + "]</b> is <b>inconsistent</b> with <b>[" + result[1] + "]</b> in the Billing Guideline CCI Modifier is allowed but not found \n");
                }

            }

            rulesList = gettingRulesFormatted(claim, rulesList, ErrorMsgs);


            Instant end = Instant.now();
            Duration ExecutionTime = Duration.between(start, end);
            if (ErrorMsgs.size() != 0)
                System.out.println(ErrorMsgs + "~" + ExecutionTime);
            else {
                System.out.println("1");
            }

            ErrorMsgs.add(String.valueOf(ErrorMsgs.size()));

        } catch (NumberFormatException e) {
            ErrorMsgs.add("EXCEPTION occurs in SCRUBBER : " + new RuntimeException(e).getMessage());
//            return ErrorMsgs;
            throw new RuntimeException(e);
        }


        return rulesList;
    }

    private ClaimAudittrail insertClaimAuditTrails(Claiminfomaster claim, String description, String action) {
        return claimAudittrailService.createAuditTrail(ClaimAudittrail.builder()
                .claimNo(claim.getClaimNumber())
                .claimType(String.valueOf(claim.getClaimType() == null ? 1 : claim.getClaimType()))
                .userID(claim.getCreatedBy())
                .userIP(claim.getCreatedIP())
                .clientID(String.valueOf(claim.getClientId()))
                .userIP(claim.getCreatedIP())
                .action(action)
                .ruleText(description).build());
    }

    private String Mod_Is_MUST_NCCI(String cpt, ArrayList<String> charge_procedureCodes) {
        boolean isAllowed = false;
        String cpt1 = null, cpt2 = null;

        for (String Proc :
                charge_procedureCodes) {

            if (Proc.equals(cpt)) continue;

            if (cciModMustRepository.Mod_Is_MUST_NCCI(Proc, cpt) > 0) {
                isAllowed = true;
                cpt1 = Proc;
                cpt2 = cpt;
                break;
            }

        }


        if (isAllowed) {
            return cpt1 + "~" + cpt2;
        }
        return null;
    }


    private String LCD_CPT_ICD_Denial(String cpt, String ICD) {
        boolean CPT_Found = articleXHcpcCodeRepository.validateArticleXHcpcCodeCPT(cpt) > 0;


        if (CPT_Found) {

            if (articleXHcpcCodeRepository.validateArticleXHcpcCodeCPT_X_ArticleXHcpcCode_X_ArticleXIcd10Noncovered(cpt, ICD) > 0) {
                //system.out.println("LCD_CPT_ICD_Denial QUERY ->> " + ps.toString());
                return ICD + "~" + cpt;
            }

        }

        return null;
    }

    private String NCD_CPT_ICD_Med(String cpt, String ICD) {
        boolean CPT_Found = false;
        String[] Range = null;

        if (ncdCptGroupRepository.validateNcdCptGroupCPT(cpt) > 0) {
            CPT_Found = true;
        }

        if (CPT_Found) {

            if (ncdCptGroupRepository.validateNcdCptGroupXNcdxcptxicd_Med(cpt, ICD.replace(".", ""), 3) > 0) {
                return ICD + "~" + cpt;
            }

            List<String> ncdCptGroupXNcdxcptxicd = ncdCptGroupRepository.getNcdCptGroupXNcdxcptxicd(cpt, 3);
            for (String icd :
                    ncdCptGroupXNcdxcptxicd) {
                Range = icd.split("-");
                if (iCDRepository.find_ICD_between_Ranges(Range[0], Range[1], ICD) > 0) {
                    return ICD + "~" + cpt;
                }
            }


        }

        return null;
    }

    private String NCD_CPT_ICD_Denial(String cpt, String ICD) {
        boolean CPT_Found = false;
        String[] Range = null;


        if (ncdCptGroupRepository.validateNcdCptGroupCPT(cpt) > 0) {
            CPT_Found = true;
        }


        if (CPT_Found) {

            if (ncdCptGroupRepository.validateNcdCptGroupXNcdxcptxicd_Denial(cpt, ICD.replace(".", ""), 2) > 0) {
                return ICD + "~" + cpt;
            }

            List<String> ncdCptGroupXNcdxcptxicd = ncdCptGroupRepository.getNcdCptGroupXNcdxcptxicd(cpt, 2);
            for (String icd :
                    ncdCptGroupXNcdxcptxicd) {
                Range = icd.split("-");
                if (iCDRepository.find_ICD_between_Ranges(Range[0], Range[1], ICD) > 0) {
                    return ICD + "~" + cpt;
                }
            }


        }

        return null;
    }


    private boolean validAgeICDs(String ICD, int age) {
        boolean isValid = agewiseicdRepository.validateAgeICDs(ICD.replace(".", "")) > 0;

        if (isValid) {
            isValid = agewiseicdRepository.validateAgeICDswithUpperAndLowerLimit(ICD.replace(".", ""), age) > 0;
        } else {
            return true;
        }


        return isValid;
    }

    private String isNotAllowed_NCCI(String cpt, ArrayList<String> charge_procedureCodes) {
        boolean isNotAllowed = false;
        String cpt1 = null, cpt2 = null;

        for (String Proc :
                charge_procedureCodes) {

            if (Proc.equals(cpt)) continue;


            if (cciNotAllowedRepository.isNotAllowed_NCCI(Proc, cpt) > 0) {
                isNotAllowed = true;
                cpt1 = Proc;
                cpt2 = cpt;
                break;
            }

        }
        if (isNotAllowed) {
            return cpt1 + "~" + cpt2;
        }
        return null;
    }


    private boolean findAddOnCode(String cpt, ArrayList<String> charge_procedureCodes) {
        boolean AddOnCodefound = false;
        boolean PrimaryCodefound = false;

        if (addoncodeRepository.validateAddOnCodes(cpt) > 0) {
            AddOnCodefound = true;
        }


        if (AddOnCodefound) {
            for (String Proc :
                    charge_procedureCodes) {

                if (Proc.equals(cpt)) continue;

                if (addoncodeRepository.validateAddOnCodesAndPrimaryCodes(Proc, cpt) > 0) {
                    PrimaryCodefound = true;
                    break;
                }
            }
        } else {
            return true;
        }

        return PrimaryCodefound;
    }


    private boolean containsValidRelativeAdminCode(String Code, ArrayList<String> charge_procedureCodes, String ICD) {
        boolean isValid = false;
        for (String chargeProcedure : charge_procedureCodes) {
            if (mcrvaccinerulecodeRepository.validateRelativeAdminCode(chargeProcedure, ICD, Code) > 0) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }


    private boolean isBMI_ICD(final String Code) {
        return iCDRepository.validateBMIICD(Code) > 0;
    }


    private boolean isInValidTaxonomy(String taxonomy) {
        return wpctaxonomyRepository.validateTaxonomy(taxonomy) > 1;
    }

    private static boolean isValid(final String npi) {
        //Picked last digit for Matching check digit at the end
        final char lastDigit = npi.charAt(9);
        //contains doubled digits and unaffected digits
        final List<Integer> alternateDigits = alternativeDigitsDoubled(npi.substring(0, 9));
        int sum = 0;
        //Adding all numerals
        for (final Integer num : alternateDigits) sum += sumOfDigits(num);
        //Add constant 24 as mentioned in algo
        final int total = sum + 24;
        //Picked unitPlaceDigit of total
        final int unitPlaceDigit = total % 10;
        //Subtract from next higher number ending in zero
        final int checkDigit = (unitPlaceDigit != 0) ? (10 - unitPlaceDigit) : unitPlaceDigit;
        return Character.getNumericValue(lastDigit) == checkDigit;
    }

    private static List<Integer> alternativeDigitsDoubled(final String str) {
        final List<Integer> numerals = new ArrayList<>();
        for (int i = 0; i < str.length(); ++i)
            //doubled every alternate digit
            if (i % 2 == 0) numerals.add(2 * Character.getNumericValue(str.charAt(i)));
                //added unaffected digits
            else numerals.add(Character.getNumericValue(str.charAt(i)));
        return numerals;
    }

    private static int sumOfDigits(int num) {
        int sum = 0;
        //Breaking number into single Digits and Adding them
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    private boolean isInValidAddress(String address) {
        return address.startsWith("PO BOX") ||
                address.startsWith("POST") ||
                address.startsWith("BOX");
    }

    private boolean isInValidZipCode(String state, String zipCode) {
        boolean isInValid = true;
        ResultSet rset = null;

        if (zipCode.length() >= 5) {
            zipCode = zipCode.substring(0, 5);
        }

        isInValid = zipcodefacilityRepository.validateZipAndState(zipCode, state) > 0;

        if (isInValid)
            isInValid = zipcodelibraryRepository.validateZipAndState(zipCode, state) > 0;

        return isInValid;
    }

    public String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }

    public static int getAge(LocalDate dob) {
        LocalDate curDate = LocalDate.now();
        return Period.between(dob, curDate).getYears();
    }

}
