package rovermd.project.claimservices.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.dto.*;
import rovermd.project.claimservices.dto.copyClaim.institutional.ClaiminfomasterInstDto_CopyClaim;
import rovermd.project.claimservices.dto.copyClaim.professional.ClaiminfomasterProfDto_CopyClaim;
import rovermd.project.claimservices.dto.institutional.*;
import rovermd.project.claimservices.dto.ub04.*;
import rovermd.project.claimservices.dto.viewSingleClaim.institutional.ClaiminfomasterInstDto_ViewSingleClaim;
import rovermd.project.claimservices.entity.claimMaster.*;
import rovermd.project.claimservices.entity.paymentPosting.ClaimLedgerChargesEntries;
import rovermd.project.claimservices.exception.ResourceNotFoundException;
import rovermd.project.claimservices.repos.claimMaster.*;
import rovermd.project.claimservices.repos.paymentPosting.ClaimLedgerChargesEntriesRepository;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimServiceInstitutional;
import rovermd.project.claimservices.service.ClaimServiceSrubber;
import rovermd.project.claimservices.service.ExternalService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static rovermd.project.claimservices.util.UtilityHelper.isEmpty;

@Service
public class ClaimServiceInstitutionalImpl implements ClaimServiceInstitutional {

    @Autowired
    private ClaiminfomasterRepository claimRepo;

    @Autowired
    private ClaimLedgerChargesEntriesRepository claimLedgerChargesEntriesRepository;

    @Autowired
    private ClaimchargesinfoRepository claimchargesinfoRepository;

    @Autowired
    private ClaiminfocodeconditioncodeRepository claiminfocodeconditioncodeRepository;

    @Autowired
    private ClaiminfooccuranceRepository claiminfooccuranceRepository;

    @Autowired
    private ClaiminfocodeoccspanRepository claiminfocodeoccspanRepository;

    @Autowired
    private ClaiminfocodevaluecodeRepository claiminfocodevaluecodeRepository;

    @Autowired
    private ClaimadditionalinfoRepository claimadditionalinfoRepository;


    @Autowired
    private ClaimStatusRepository claimStatusRepository;

    @Autowired
    private MODRepository mODRepository;


    @Autowired
    private CPTRepository cPTRepository;

    @Autowired
    private RevenueCodeRepository revenueCodeRepository;

    @Autowired
    private ICDRepository iCDRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClaimAudittrailService claimAudittrailService;

    @Autowired
    private ExternalService externalService;

    @Autowired
    private ClaimServiceSrubber claimServiceSrubber;

    @Autowired
    private ClaimAudittrailRepository claimAudittrailRepository;


    @Override
    public ClaiminfomasterInstDto_ViewSingleClaim getClaimById(Integer claimId) {
        System.out.println("HERE");

        ObjectNode cptCodeAndDescription = null;
        ObjectNode modCodeAndDescription = null;
        ObjectNode StatusCodeAndDescription = null;
        ObjectNode RevCodeAndDescription = null;
        String claimInfocodeextcauseinjDesc = null;
        String claiminfocodereasvisitDesc = null;
        String claiminfocodeothdiagDesc = null;
        String claiminfocodeothprocedureDesc = null;
        String claiminfocodeoccspanDesc = null;
        String claiminfooccuranceDesc = null;
        String claiminfocodevaluecodeDesc = null;
        String claiminfocodeconditioncodeDesc = null;

        ClaiminfomasterInstDto_ViewSingleClaim claiminfomasterInstDto_viewSingleClaim = null;

        Claiminfomaster claim = claimRepo.findById(claimId).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));
        System.out.println("HERE");

        List<ScrubberRulesDto> rulesList = new ArrayList<>();



            System.out.println("HERE");

            claim = filterChargesWrtStatus(claim);

            claiminfomasterInstDto_viewSingleClaim = claimToDto_ViewSingleClaim(claim);

            String InsuranceName = claim.getPriInsuranceNameId();
            InsuranceName = !isEmpty(InsuranceName) ? externalService.getInsuranceDetailsById(InsuranceName).getPayerName() : null;
            claiminfomasterInstDto_viewSingleClaim.setPriInsuranceName(
                    InsuranceName
            );

            InsuranceName = claim.getSecondaryInsuranceId();
            InsuranceName = !isEmpty(InsuranceName) ? externalService.getInsuranceDetailsById(InsuranceName).getPayerName() : null;
            claiminfomasterInstDto_viewSingleClaim.setSecondaryInsurance(
                    InsuranceName
            );


            for (int i = 0; i < claim.getClaimchargesinfo().size(); i++) {

                if (!isEmpty(claim.getClaimchargesinfo().get(i))) {

                    cptCodeAndDescription = getCodeAndDescriptionForCPT(claim.getClaimchargesinfo().get(i).getHCPCSProcedure());
                    claiminfomasterInstDto_viewSingleClaim.getClaimchargesinfo().get(i).setHCPCSProcedure(
                            cptCodeAndDescription
                    );


                    modCodeAndDescription = getCodeAndDescriptionForMod(claim.getClaimchargesinfo().get(i).getMod1());
                    claiminfomasterInstDto_viewSingleClaim.getClaimchargesinfo().get(i).setMod1(
                            modCodeAndDescription
                    );

                    modCodeAndDescription = getCodeAndDescriptionForMod(claim.getClaimchargesinfo().get(i).getMod2());
                    claiminfomasterInstDto_viewSingleClaim.getClaimchargesinfo().get(i).setMod2(
                            modCodeAndDescription
                    );

                    modCodeAndDescription = getCodeAndDescriptionForMod(claim.getClaimchargesinfo().get(i).getMod3());
                    claiminfomasterInstDto_viewSingleClaim.getClaimchargesinfo().get(i).setMod3(
                            modCodeAndDescription
                    );

                    modCodeAndDescription = getCodeAndDescriptionForMod(claim.getClaimchargesinfo().get(i).getMod4());
                    claiminfomasterInstDto_viewSingleClaim.getClaimchargesinfo().get(i).setMod4(
                            modCodeAndDescription
                    );


                    StatusCodeAndDescription = getCodeAndDescriptionForChargeStatus(claim.getClaimchargesinfo().get(i).getChargesStatus());
                    claiminfomasterInstDto_viewSingleClaim.getClaimchargesinfo().get(i).setChargesStatus(
                            StatusCodeAndDescription
                    );

                    RevCodeAndDescription = getCodeAndDescriptionForRevCode(claim.getClaimchargesinfo().get(i).getRevCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaimchargesinfo().get(i).setRevCode(
                            RevCodeAndDescription
                    );


                }

            }


            for (int i = 0; i < claim.getClaiminfocodeextcauseinj().size(); i++) {
                if (!isEmpty(claim.getClaiminfocodeextcauseinj().get(i))) {
                    claimInfocodeextcauseinjDesc = getDescriptionForICD(claim.getClaiminfocodeextcauseinj().get(i).getCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaiminfocodeextcauseinj().get(i).setDescription(
                            claimInfocodeextcauseinjDesc
                    );
                }
            }

            for (int i = 0; i < claim.getClaiminfocodereasvisit().size(); i++) {
                if (!isEmpty(claim.getClaiminfocodereasvisit().get(i))) {

                    claiminfocodereasvisitDesc = getDescriptionForICD(claim.getClaiminfocodereasvisit().get(i).getCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaiminfocodereasvisit().get(i).setDescription(
                            claiminfocodereasvisitDesc
                    );
                }
            }


            for (int i = 0; i < claim.getClaiminfocodeothdiag().size(); i++) {
                if (!isEmpty(claim.getClaiminfocodeothdiag().get(i))) {
                    claiminfocodeothdiagDesc = getDescriptionForICD(claim.getClaiminfocodeothdiag().get(i).getCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaiminfocodeothdiag().get(i).setDescription(
                            claiminfocodeothdiagDesc
                    );
                }
            }

            for (int i = 0; i < claim.getClaiminfocodeothprocedure().size(); i++) {
                if (!isEmpty(claim.getClaiminfocodeothprocedure().get(i))) {
                    claiminfocodeothprocedureDesc = getDescriptionForICD(claim.getClaiminfocodeothprocedure().get(i).getCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaiminfocodeothprocedure().get(i).setDescription(
                            claiminfocodeothprocedureDesc
                    );
                }
            }

            for (int i = 0; i < claim.getClaiminfocodeoccspan().size(); i++) {
                if (!isEmpty(claim.getClaiminfocodeoccspan().get(i))) {
                    claiminfocodeoccspanDesc = getDescriptionForICD(claim.getClaiminfocodeoccspan().get(i).getCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaiminfocodeoccspan().get(i).setDescription(
                            claiminfocodeoccspanDesc
                    );
                }
            }

            for (int i = 0; i < claim.getClaiminfooccurance().size(); i++) {
                if (!isEmpty(claim.getClaiminfooccurance().get(i))) {
                    claiminfooccuranceDesc = getDescriptionForICD(claim.getClaiminfooccurance().get(i).getCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaiminfooccurance().get(i).setDescription(
                            claiminfooccuranceDesc
                    );
                }
            }

            for (int i = 0; i < claim.getClaiminfocodevaluecode().size(); i++) {
                if (!isEmpty(claim.getClaiminfocodevaluecode().get(i))) {
                    claiminfocodevaluecodeDesc = getDescriptionForICD(claim.getClaiminfocodevaluecode().get(i).getCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaiminfocodevaluecode().get(i).setDescription(
                            claiminfocodevaluecodeDesc
                    );
                }
            }

            for (int i = 0; i < claim.getClaiminfocodeconditioncode().size(); i++) {
                if (!isEmpty(claim.getClaiminfocodeconditioncode().get(i))) {
                    claiminfocodeconditioncodeDesc = getDescriptionForICD(claim.getClaiminfocodeconditioncode().get(i).getCode());
                    claiminfomasterInstDto_viewSingleClaim.getClaiminfocodeconditioncode().get(i).setDescription(
                            claiminfocodeconditioncodeDesc
                    );
                }
            }

            if (claim.getScrubbed() == 0) {
                List<ClaimAudittrail> fired = claimAudittrailRepository.findByClaimNoAndAction(claim.getClaimNumber(), "FIRED");
                for (ClaimAudittrail error :
                        fired) {
                    ScrubberRulesDto scrubberRulesDto = new ScrubberRulesDto();
                    scrubberRulesDto.setId(error.getId());
                    scrubberRulesDto.setDescription(error.getRuleText());
                    rulesList.add(scrubberRulesDto);
                }
                claiminfomasterInstDto_viewSingleClaim.setScrubber(rulesList);
            }

            claimAudittrailService.createAuditTrail(ClaimAudittrail.builder()
                    .claimNo(claim.getClaimNumber())
                    .clientID(String.valueOf(claim.getClientId()))
                    .action("OPENED")
                    .ruleText("CLAIM OPENED").build());


        return claiminfomasterInstDto_viewSingleClaim;
    }

    private ObjectNode getCodeAndDescriptionForCPT(String cpt) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objNode = mapper.createObjectNode();
        List<CPT> cpts = cPTRepository.findCPTByCPTCode(cpt);

        if (!isEmpty(cpts) && cpts.size() > 0) {
            String desc = cpts.get(0).getCPTDescription();
            objNode.put("code", cpt);
            objNode.put("description", !isEmpty(desc) ? desc : "");
        } else {
            objNode.put("code", cpt);
            objNode.put("description", "");
        }

        return objNode;
    }

    private String getDescriptionForICD(String icd) {
        List<ICD> icds = iCDRepository.findICDByICD(icd);
        if (!isEmpty(icds) && icds.size() > 0) {
            return icds.get(0).getDescription();
        } else {
            return "";
        }
    }

    private ObjectNode getCodeAndDescriptionForRevCode(String revCode) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objNode = mapper.createObjectNode();
        List<RevenueCode> revCodeByRevCode = revenueCodeRepository.findRevenueCodeByRevCode(revCode);

        if (!isEmpty(revCodeByRevCode) && revCodeByRevCode.size() > 0) {
            String desc = revCodeByRevCode.get(0).getRevDescription();
            objNode.put("code", revCode);
            objNode.put("description", !isEmpty(desc) ? desc : "");
        } else {
            objNode.put("code", revCode);
            objNode.put("description", "");
        }

        return objNode;
    }

    private ObjectNode getCodeAndDescriptionForMod(String mod) {
        if (!isEmpty(mod)) {
            String desc = mODRepository.findMODByCode(mod).getDescription();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objNode = mapper.createObjectNode();
            objNode.put("code", mod);
            objNode.put("description", !isEmpty(desc) ? desc : "");
            return objNode;
        } else {
            return null;
        }
    }

    private ObjectNode getCodeAndDescriptionForChargeStatus(String chargeStatus) {
        if (!isEmpty(chargeStatus)) {
            String desc = claimStatusRepository.findById(Long.valueOf(chargeStatus)).get().getDescname();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objNode = mapper.createObjectNode();
            objNode.put("code", chargeStatus);
            objNode.put("description", !isEmpty(desc) ? desc : "");
            return objNode;
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    Claiminfomaster filterChargesWrtStatus(Claiminfomaster claim) {

        claim.setClaimchargesinfo(
                claim.getClaimchargesinfo()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claimchargesinfo.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodeextcauseinj(
                claim.getClaiminfocodeextcauseinj()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claiminfocodeextcauseinj.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodereasvisit(
                claim.getClaiminfocodereasvisit()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claiminfocodereasvisit.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodeothdiag(
                claim.getClaiminfocodeothdiag()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claiminfocodeothdiag.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodeothprocedure(
                claim.getClaiminfocodeothprocedure()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claiminfocodeothprocedure.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodeoccspan(
                claim.getClaiminfocodeoccspan()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claiminfocodeoccspan.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfooccurance(
                claim.getClaiminfooccurance()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claiminfooccurance.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodevaluecode(
                claim.getClaiminfocodevaluecode()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claiminfocodevaluecode.class))
                        .collect(Collectors.toList())
        );


        claim.setClaiminfocodeconditioncode(
                claim.getClaiminfocodeconditioncode()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, Claiminfocodeconditioncode.class))
                        .collect(Collectors.toList())
        );

        return claim;
    }

    private ClaiminfomasterInstDto filterChargesWrtStatus_DTO(ClaiminfomasterInstDto claim) {

        claim.setClaimchargesinfo(
                claim.getClaimchargesinfo()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaimchargesinfoDto.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodeextcauseinj(
                claim.getClaiminfocodeextcauseinj()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaiminfocodeextcauseinjDto.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodereasvisit(
                claim.getClaiminfocodereasvisit()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaiminfocodereasvisitDto.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodeothdiag(
                claim.getClaiminfocodeothdiag()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaiminfocodeothdiagDto.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodeothprocedure(
                claim.getClaiminfocodeothprocedure()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaiminfocodeothprocedureDto.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodeoccspan(
                claim.getClaiminfocodeoccspan()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaiminfocodeoccspanDto.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfooccurance(
                claim.getClaiminfooccurance()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaiminfooccuranceDto.class))
                        .collect(Collectors.toList())
        );

        claim.setClaiminfocodevaluecode(
                claim.getClaiminfocodevaluecode()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaiminfocodevaluecodeDto.class))
                        .collect(Collectors.toList())
        );


        claim.setClaiminfocodeconditioncode(
                claim.getClaiminfocodeconditioncode()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaiminfocodeconditioncodeDto.class))
                        .collect(Collectors.toList())
        );

        return claim;
    }

    @Override
    public ClaiminfomasterProfDto_CopyClaim copyClaim(Integer claimID) {
        Claiminfomaster claim = claimRepo.findById(claimID).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimID));

        ClaiminfomasterInstDto_CopyClaim claiminfomasterInstDto = claimToDto_InstCopyClaim(claim);
        ClaiminfomasterProfDto_CopyClaim claiminfomasterProfDto = claimToDto_ProfCopyClaim(claim);

        modelMapper.map(claiminfomasterInstDto,
                claiminfomasterProfDto);
        String claimNumber = claimRepo.getNewClaimNumber();
        claiminfomasterProfDto.setClaimNumber("CP-" + claimNumber);

        return claiminfomasterProfDto;
    }

    @Override
    public List<ClaiminfomasterInstDto> getAllClaims() {
        List<Claiminfomaster> claimList = claimRepo.findAllByClaimType(1);
        return claimList.stream().map(this::claimToDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ClaiminfomasterInstDto createClaim(ClaiminfomasterInstDto claimDTO, String remoteAddr) {
        Claiminfomaster save = null;
        Double totalChargeswrtClaim = 0.00;
        try {
            List<ClaimchargesinfoDto> newClaimChargesinfoDTO = claimDTO.getClaimchargesinfo();
            Claiminfomaster claim = dtoToClaim(claimDTO);
            claim.setCreatedIP(remoteAddr);

            String claimNumber = claimRepo.getNewClaimNumber();
            claim.setClaimNumber("CI-" + claimNumber);


            claim.getClaimadditionalinfo().setClaiminfomaster(claim);

            if (claim.getClaiminformationcode() != null) {
                claim.getClaiminformationcode().setClaiminfomaster(claim);
            }

            List<Claimchargesinfo> claimClaimchargesinfo = claim.getClaimchargesinfo();

            IntStream.range(0, claim.getClaimchargesinfo().size())
                    .forEach(i -> claim.getClaimchargesinfo().get(i).setClaiminfomaster(claim));

            IntStream.range(0, claimClaimchargesinfo.size())
                    .filter(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo() != null)
                    .forEach(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().setClaimchargesinfo(claim.getClaimchargesinfo().get(i)));

            IntStream.range(0, claim.getClaiminfocodereasvisit().size())
                    .forEach(i -> claim.getClaiminfocodereasvisit().get(i).setClaiminfomaster(claim));

            IntStream.range(0, claim.getClaiminfocodeconditioncode().size())
                    .forEach(i -> claim.getClaiminfocodeconditioncode().get(i).setClaiminfomaster(claim));

            IntStream.range(0, claim.getClaiminfocodevaluecode().size())
                    .forEach(i -> claim.getClaiminfocodevaluecode().get(i).setClaiminfomaster(claim));

            IntStream.range(0, claim.getClaiminfooccurance().size())
                    .forEach(i -> claim.getClaiminfooccurance().get(i).setClaiminfomaster(claim));

            IntStream.range(0, claim.getClaiminfocodeoccspan().size())
                    .forEach(i -> claim.getClaiminfocodeoccspan().get(i).setClaiminfomaster(claim));

            IntStream.range(0, claim.getClaiminfocodeothprocedure().size())
                    .forEach(i -> claim.getClaiminfocodeothprocedure().get(i).setClaiminfomaster(claim));

            IntStream.range(0, claim.getClaiminfocodeothdiag().size())
                    .forEach(i -> claim.getClaiminfocodeothdiag().get(i).setClaiminfomaster(claim));

            IntStream.range(0, claim.getClaiminfocodeextcauseinj().size())
                    .forEach(i -> claim.getClaiminfocodeextcauseinj().get(i).setClaiminfomaster(claim));


            for (ClaimchargesinfoDto claimchargesinfoDto : newClaimChargesinfoDTO) {

                compareCPT(null,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        claim
                );

                compareClaimChargesInfoAttr(
                        null,
                        isEmpty(claimchargesinfoDto.getChargesStatus()) ? null : claimStatusRepository.findById(Long.valueOf(claimchargesinfoDto.getChargesStatus())).get().getDescname(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "status"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getMod1(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "MOD 1"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getMod2(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "MOD 2"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getMod3(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "MOD 3"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getMod4(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "MOD 4"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getAmount() == null ? null : String.valueOf(claimchargesinfoDto.getAmount()),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "Amount"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getUnitPrice() == null ? null : String.valueOf(claimchargesinfoDto.getUnitPrice()),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "Units Price"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getUnits() == null ? null : String.valueOf(claimchargesinfoDto.getUnits()),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "Units"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getServiceDate(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "Service date"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getRevCode(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "Revenue code"
                );
                if (claimchargesinfoDto.getStatus() == 1)
                    totalChargeswrtClaim += claimchargesinfoDto.getAmount();
            }
            claim.setTotalCharges(totalChargeswrtClaim);

            List<?> scrubber = claimServiceSrubber.scrubberInst(claim);

            if (scrubber.size() > 0) {
                claim.setScrubbed(0);//failed by scrubber
            } else {
                claim.setScrubbed(1);//passed by scrubber
            }

            claim.setTotalCharges(totalChargeswrtClaim);

            save = claimRepo.save(claim);

            for(Claimchargesinfo x:save.getClaimchargesinfo()){
                ClaimLedgerChargesEntries claimLedgerChargesEntries = new ClaimLedgerChargesEntries();
                claimLedgerChargesEntries.setClaimNumber(save.getClaimNumber());
                claimLedgerChargesEntries.setClaimIdx(save.getId());
                claimLedgerChargesEntries.setCharges(x.getHCPCSProcedure());
                claimLedgerChargesEntries.setAmount(x.getAmount());
                claimLedgerChargesEntries.setChargeIdx(x.getId());
                claimLedgerChargesEntries.setTransactionType("Cr");
                claimLedgerChargesEntries.setStatus(x.getStatus());
                if (x.getStatus() != 1)
                    claimLedgerChargesEntries.setDeleted(1);
                claimLedgerChargesEntriesRepository.save(claimLedgerChargesEntries);
            }

            insertClaimAuditTrails(claim,
                    "CLAIM SAVED",
                    "CREATED");


        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }


        return claimToDto(save);
    }

    @Transactional
    @Override
    public ClaiminfomasterInstDto updateClaim(ClaiminfomasterInstDto latestClaimDTO, String remoteAddr) {
        List<ClaimchargesinfoDto> existingClaimChargesinfoDTO;
        List<ClaimchargesinfoDto> newClaimChargesinfoDTO;
        Double totalChargeswrtClaim = 0.00;


        Claiminfomaster claim = filterChargesWrtStatus(claimRepo.findById(latestClaimDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", latestClaimDTO.getId())));
        ClaiminfomasterInstDto existingClaimDTO = filterChargesWrtStatus_DTO(claimToDto(claim));

        existingClaimChargesinfoDTO = existingClaimDTO.getClaimchargesinfo();
        newClaimChargesinfoDTO = latestClaimDTO.getClaimchargesinfo();


        if (!existingClaimChargesinfoDTO.equals(newClaimChargesinfoDTO)) {

            for (ClaimchargesinfoDto claimchargesinfoDto : newClaimChargesinfoDTO) {
                if (claimchargesinfoDto.getId() == null) {
                    claimAudittrailService.createAuditTrail(ClaimAudittrail.builder()
                            .claimNo(claim.getClaimNumber())
                            .claimType(String.valueOf(claim.getClaimType()))
                            .userID(claim.getCreatedBy())
                            .userIP(claim.getCreatedIP())
                            .clientID(String.valueOf(claim.getClientId()))
                            .userIP(claim.getCreatedIP())
                            .action("CREATED")
                            .ruleText("CPT : " + claimchargesinfoDto.getHCPCSProcedure() + " ADDED").build());
                }
            }


            for (int i = 0; i < existingClaimChargesinfoDTO.size(); i++) {
                if (!existingClaimChargesinfoDTO.get(i).equals(newClaimChargesinfoDTO.get(i))) {

                    compareCPT(existingClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            claim
                    );


                    if (existingClaimChargesinfoDTO.get(i).getStatus().compareTo(newClaimChargesinfoDTO.get(i).getStatus()) != 0) {
                        insertClaimAuditTrails(claim,
                                "CPT : " + existingClaimChargesinfoDTO.get(i).getHCPCSProcedure() + " is removed ",
                                "REMOVED");
                    }


                    compareClaimChargesInfoAttr(
                            isEmpty(existingClaimChargesinfoDTO.get(i).getChargesStatus()) ? null : claimStatusRepository.findById(Long.valueOf(existingClaimChargesinfoDTO.get(i).getChargesStatus())).get().getDescname(),
                            isEmpty(newClaimChargesinfoDTO.get(i).getChargesStatus()) ? null : claimStatusRepository.findById(Long.valueOf(newClaimChargesinfoDTO.get(i).getChargesStatus())).get().getDescname(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "status"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getMod1(),
                            newClaimChargesinfoDTO.get(i).getMod1(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "MOD 1"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getMod2(),
                            newClaimChargesinfoDTO.get(i).getMod2(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "MOD 2"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getMod3(),
                            newClaimChargesinfoDTO.get(i).getMod3(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "MOD 3"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getMod4(),
                            newClaimChargesinfoDTO.get(i).getMod4(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "MOD 4"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getAmount() == null ? null : String.valueOf(existingClaimChargesinfoDTO.get(i).getAmount()),
                            newClaimChargesinfoDTO.get(i).getAmount() == null ? null : String.valueOf(newClaimChargesinfoDTO.get(i).getAmount()),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "Amount"
                    );

                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getUnitPrice() == null ? null : String.valueOf(existingClaimChargesinfoDTO.get(i).getUnitPrice()),
                            newClaimChargesinfoDTO.get(i).getUnitPrice() == null ? null : String.valueOf(newClaimChargesinfoDTO.get(i).getUnitPrice()),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "Units Price"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getUnits() == null ? null : String.valueOf(existingClaimChargesinfoDTO.get(i).getUnits()),
                            newClaimChargesinfoDTO.get(i).getUnits() == null ? null : String.valueOf(newClaimChargesinfoDTO.get(i).getUnits()),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "Units"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getServiceDate(),
                            newClaimChargesinfoDTO.get(i).getServiceDate(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "Service date"
                    );

                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getRevCode(),
                            newClaimChargesinfoDTO.get(i).getRevCode(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "Revenue code"
                    );

                    if (isEmpty(existingClaimChargesinfoDTO.get(i).getClaimchargesotherinfo()) && !isEmpty(newClaimChargesinfoDTO.get(i).getClaimchargesotherinfo())) {
                        insertClaimAuditTrails(claim,
                                "Drug Info added against CPT : [" + newClaimChargesinfoDTO.get(i).getHCPCSProcedure() + "] ",
                                "ADDED");
                    } else if (isEmpty(newClaimChargesinfoDTO.get(i).getClaimchargesotherinfo()) && !isEmpty(existingClaimChargesinfoDTO.get(i).getClaimchargesotherinfo())) {
                        insertClaimAuditTrails(claim,
                                "Drug Info removed against CPT : [" + existingClaimChargesinfoDTO.get(i).getHCPCSProcedure() + "] ",
                                "REMOVED");
                    } else if (!isEmpty(newClaimChargesinfoDTO.get(i).getClaimchargesotherinfo()) && !isEmpty(existingClaimChargesinfoDTO.get(i).getClaimchargesotherinfo())) {
                        if (!existingClaimChargesinfoDTO.get(i).getClaimchargesotherinfo().equals(newClaimChargesinfoDTO.get(i).getClaimchargesotherinfo())) {
                            insertClaimAuditTrails(claim,
                                    "Drug Info updated against CPT : [" + newClaimChargesinfoDTO.get(i).getHCPCSProcedure() + "] ",
                                    "UPDATED");
                        }
                    }


                }

                if (newClaimChargesinfoDTO.get(i).getStatus() == 1)
                    totalChargeswrtClaim += newClaimChargesinfoDTO.get(i).getAmount();
            }
            claim.setTotalCharges(totalChargeswrtClaim);


            claimAudittrailService.createAuditTrail(ClaimAudittrail.builder()
                    .claimNo(claim.getClaimNumber())
                    .claimType(String.valueOf(claim.getClaimType()))
                    .userID(claim.getCreatedBy())
                    .userIP(claim.getCreatedIP())
                    .clientID(String.valueOf(claim.getClientId()))
                    .userIP(claim.getCreatedIP())
                    .action("UPDATED")
                    .ruleText("CLAIM UPDATED").build());

        }

        modelMapper.map(latestClaimDTO, claim);

        claim.setCreatedBy("Mouhid");
        claim.setCreatedIP(remoteAddr);

        claim.getClaimadditionalinfo().setClaiminfomaster(claim);

        if (claim.getClaiminformationcode() != null) {
            claim.getClaiminformationcode().setClaiminfomaster(claim);
        }

        List<Claimchargesinfo> claimClaimchargesinfo = claim.getClaimchargesinfo();

        IntStream.range(0, claim.getClaimchargesinfo().size())
                .forEach(i -> claim.getClaimchargesinfo().get(i).setClaiminfomaster(claim));

        IntStream.range(0, claimClaimchargesinfo.size())
                .filter(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo() != null)
                .forEach(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().setClaimchargesinfo(claim.getClaimchargesinfo().get(i)));

        IntStream.range(0, claim.getClaiminfocodereasvisit().size())
                .forEach(i -> claim.getClaiminfocodereasvisit().get(i).setClaiminfomaster(claim));

        IntStream.range(0, claim.getClaiminfocodeconditioncode().size())
                .forEach(i -> claim.getClaiminfocodeconditioncode().get(i).setClaiminfomaster(claim));

        IntStream.range(0, claim.getClaiminfocodevaluecode().size())
                .forEach(i -> claim.getClaiminfocodevaluecode().get(i).setClaiminfomaster(claim));

        IntStream.range(0, claim.getClaiminfooccurance().size())
                .forEach(i -> claim.getClaiminfooccurance().get(i).setClaiminfomaster(claim));

        IntStream.range(0, claim.getClaiminfocodeoccspan().size())
                .forEach(i -> claim.getClaiminfocodeoccspan().get(i).setClaiminfomaster(claim));

        IntStream.range(0, claim.getClaiminfocodeothprocedure().size())
                .forEach(i -> claim.getClaiminfocodeothprocedure().get(i).setClaiminfomaster(claim));

        IntStream.range(0, claim.getClaiminfocodeothdiag().size())
                .forEach(i -> claim.getClaiminfocodeothdiag().get(i).setClaiminfomaster(claim));

        IntStream.range(0, claim.getClaiminfocodeextcauseinj().size())
                .forEach(i -> claim.getClaiminfocodeextcauseinj().get(i).setClaiminfomaster(claim));

        List<?> scrubber = claimServiceSrubber.scrubberInst(claim, existingClaimDTO.equals(latestClaimDTO) ? 0 : 1);


        if (scrubber.size() > 0) {
            claim.setScrubbed(0);//failed by scrubber
        } else {
            claim.setScrubbed(1);//passed by scrubber
        }

        Claiminfomaster save = claimRepo.save(claim);

        return claimToDto(save);
    }

    @Override
    public UB04DTO ub04(Integer claimId) {
        Claiminfomaster claim = claimRepo.findById(claimId).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));
        claim = filterChargesWrtStatus(claim);

        ObjectNode RevCodeAndDescription = null;
        ObjectNode attendingProviderDetail = null;
        ObjectNode operatingProviderDetail = null;
        ClaiminfomasterInstDto_ViewSingleClaim claiminfomasterInstDto_viewSingleClaim = null;

        UB04DTO ubo4 = new UB04DTO();
        ClaiminfomasterInstDto_UB04 claiminfomasterInstDtoUb04 = claimToDto_UB04(claim);
        ubo4.setClaiminfomasterInstDto(claiminfomasterInstDtoUb04);

        if (!isEmpty(claim.getAttendingProvider())) {
            try {
                DoctorDTO doctorDetail = externalService.getDoctorDetailsById(Long.parseLong(claim.getAttendingProvider()));
                claiminfomasterInstDtoUb04.setAttendingProvider(doctorDetail);
            } catch (Exception e) {
                throw new ResourceNotFoundException("Attending Provider","Id",Long.parseLong(claim.getAttendingProvider()));
            }
        }

        if (!isEmpty(claim.getOperatingProvider())) {
            try {
                DoctorDTO doctorDetail = externalService.getDoctorDetailsById(Long.parseLong(claim.getOperatingProvider()));
                claiminfomasterInstDtoUb04.setOperatingProvider(doctorDetail);
            } catch (Exception e) {
                throw new ResourceNotFoundException("Operating Provider","Id",Long.parseLong(claim.getOperatingProvider()));
            }
        }



        if (!isEmpty(claim.getClientId())) {
            try {
                ClientDTO_UB04 clientDetailsById = externalService.getClientDetailsById_UB04(claim.getClientId());
                ubo4.setClientDTO(clientDetailsById);
            } catch (Exception e) {
                throw new ResourceNotFoundException("Client", "Id", claim.getClientId());
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
                ubo4.setPatientDto(patientDetailsById);

            } catch (Exception e) {
                throw new ResourceNotFoundException("Patient", "Id", claim.getPatientRegId());
            }
        }

        try {
            CompanyDTO companyDetailsById = externalService.getCompanyDetailsById(1);
            ubo4.setCompanyDTO(companyDetailsById);
        } catch (Exception e) {
            throw new ResourceNotFoundException("CompanyCredential", "Id", 1);
        }


        try {
            InsuranceDTO PrimaryinsuranceDetailsById = externalService.getInsuranceDetailsById(String.valueOf(902));
            ubo4.setInsuranceDTO(PrimaryinsuranceDetailsById);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Insurance", "Id", 902);
        }

        try {
            for (int i = 0; i < claim.getClaimchargesinfo().size(); i++) {

                if (!isEmpty(claim.getClaimchargesinfo().get(i))) {

                    RevCodeAndDescription = getCodeAndDescriptionForRevCode(claim.getClaimchargesinfo().get(i).getRevCode());
                    claiminfomasterInstDtoUb04.getClaimchargesinfo().get(i).setRevCode(
                            RevCodeAndDescription
                    );

                }

            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("ClaimChargesInfo", "ClaimInfoMasterId", claimId);
        }
//
//        try {
//            List<ClaiminfocodeconditioncodeDto_UB04> conditionCodes = claiminfocodeconditioncodeRepository.findByClaiminfomasterId(claimId).stream().map(this::claimInfocodeconditioncodeToDto).collect(Collectors.toList());
//            ubo4.setClaiminfocodeconditioncodeDto(conditionCodes);
//        } catch (Exception e) {
//            throw new ResourceNotFoundException("Claiminfocodeconditioncode", "ClaimInfoMasterId", claimId);
//        }
//
//        try {
//            List<ClaiminfooccuranceDto_UB04> occuranceCodes = claiminfooccuranceRepository.findByClaiminfomasterId(claimId).stream().map(this::claiminfooccuranceToDto).collect(Collectors.toList());
//            ubo4.setClaiminfooccurances(occuranceCodes);
//        } catch (Exception e) {
//            throw new ResourceNotFoundException("Claiminfocodeconditioncode", "ClaimInfoMasterId", claimId);
//        }
//
//        try {
//            List<ClaiminfocodeoccspanDto_UBO4> occuranceSpanCode = claiminfocodeoccspanRepository.findByClaiminfomasterId(claimId).stream().map(this::claiminfocodeoccspanToDto).collect(Collectors.toList());
//            ubo4.setClaiminfocodeoccspan(occuranceSpanCode);
//        } catch (Exception e) {
//            throw new ResourceNotFoundException("Claiminfocodeoccspan", "ClaimInfoMasterId", claimId);
//        }
//
//        try {
//            List<ClaiminfocodevaluecodeDto_UB04> valueCodes = claiminfocodevaluecodeRepository.findByClaiminfomasterId(claimId).stream().map(this::claiminfovaluecodeToDto).collect(Collectors.toList());
//            ubo4.setClaiminfocodevaluecode(valueCodes);
//        } catch (Exception e) {
//            throw new ResourceNotFoundException("Claiminfocodevaluecode", "ClaimInfoMasterId", claimId);
//        }
//
//
//        try {
//            ClaimadditionalinfoDto claimadditionalinfoDto = claimadditionalinfoToDto(claimadditionalinfoRepository.findByClaiminfomasterId(claimId));
//            ubo4.setClaimadditionalinfoDto(claimadditionalinfoDto);
//        } catch (Exception e) {
//            throw new ResourceNotFoundException("ClaimAdditionalInfo", "ClaimInfoMasterId", claimId);
//        }

        return ubo4;
    }

    private ClaimadditionalinfoDto claimadditionalinfoToDto(Claimadditionalinfo claimadditionalinfo) {
        return modelMapper.map(claimadditionalinfo, ClaimadditionalinfoDto.class);
    }

    private void compareClaimChargesInfoAttr(String existingAttr, String newAttr, Claiminfomaster claim, String hcpcsProcedure, String title) {
        if (isEmpty(existingAttr) && !isEmpty(newAttr)) {
            insertClaimAuditTrails(claim,
                    "CPT : [" + hcpcsProcedure + "] " + title + " [" + newAttr + "] is added",
                    "ADDED");
        } else if (isEmpty(newAttr) && !isEmpty(existingAttr)) {
            insertClaimAuditTrails(claim,
                    "CPT : [" + hcpcsProcedure + "] " + title + " [" + existingAttr + "] is removed",
                    "REMOVED");
        } else if (!isEmpty(newAttr) && !isEmpty(existingAttr)) {
            if (existingAttr.compareTo(newAttr) != 0) {
                insertClaimAuditTrails(claim,
                        "CPT : [" + hcpcsProcedure + "] " + title + " changed  from [" + existingAttr + "] to [" + newAttr + "]",
                        "UPDATED");
            }
        }
    }

    private ClaimchargesinfoDto_UB04 claimChargesInfoToDto(Claimchargesinfo charge) {
        return modelMapper.map(charge, ClaimchargesinfoDto_UB04.class);
    }

    private ClaiminfocodeconditioncodeDto_UB04 claimInfocodeconditioncodeToDto(Claiminfocodeconditioncode conditioncode) {
        return modelMapper.map(conditioncode, ClaiminfocodeconditioncodeDto_UB04.class);
    }

    private ClaiminfooccuranceDto_UB04 claiminfooccuranceToDto(Claiminfooccurance occuranceCode) {
        return modelMapper.map(occuranceCode, ClaiminfooccuranceDto_UB04.class);
    }

    private ClaiminfocodeoccspanDto_UBO4 claiminfocodeoccspanToDto(Claiminfocodeoccspan occuranceSpanCode) {
        return modelMapper.map(occuranceSpanCode, ClaiminfocodeoccspanDto_UBO4.class);
    }

    private ClaiminfocodevaluecodeDto_UB04 claiminfovaluecodeToDto(Claiminfocodevaluecode valueCode) {
        return modelMapper.map(valueCode, ClaiminfocodevaluecodeDto_UB04.class);
    }

    private void compareCPT(String existingAttr, String newAttr, Claiminfomaster claim) {

        if (isEmpty(existingAttr) && !isEmpty(newAttr)) {
            insertClaimAuditTrails(claim,
                    "CPT : [" + newAttr + "] is added",
                    "ADDED");
        } else if (isEmpty(newAttr) && !isEmpty(existingAttr)) {
            insertClaimAuditTrails(claim,
                    "CPT : [" + existingAttr + "] is removed",
                    "REMOVED");
        } else if (!isEmpty(newAttr) && !isEmpty(existingAttr)) {
            if (existingAttr.compareTo(newAttr) != 0) {
                insertClaimAuditTrails(claim,
                        "CPT : [" + existingAttr + "] changed into [" + newAttr + "]",
                        "UPDATED");
            }
        }

    }

    private void insertClaimAuditTrails(Claiminfomaster claim, String description, String action) {
        claimAudittrailService.createAuditTrail(ClaimAudittrail.builder()
                .claimNo(claim.getClaimNumber())
                .claimType(String.valueOf(claim.getClaimType() == null ? 1 : claim.getClaimType()))
                .userID(claim.getCreatedBy())
                .userIP(claim.getCreatedIP())
                .clientID(String.valueOf(claim.getClientId()))
                .userIP(claim.getCreatedIP())
                .action(action)
                .ruleText(description).build());
    }


    private ClaiminfomasterInstDto claimToDto(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterInstDto.class);
    }

    private ClaiminfomasterInstDto_UB04 claimToDto_UB04(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterInstDto_UB04.class);
    }


    private ClaiminfomasterProfDto_CopyClaim claimToDto_ProfCopyClaim(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterProfDto_CopyClaim.class);
    }

    private ClaiminfomasterInstDto_CopyClaim claimToDto_InstCopyClaim(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterInstDto_CopyClaim.class);
    }

    private ClaiminfomasterInstDto_ViewSingleClaim claimToDto_ViewSingleClaim(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterInstDto_ViewSingleClaim.class);
    }

    private Claiminfomaster dtoToClaim(ClaiminfomasterInstDto claiminfomasterDto) {
        return modelMapper.map(claiminfomasterDto, Claiminfomaster.class);
    }

}
