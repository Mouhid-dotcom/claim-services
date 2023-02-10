package rovermd.project.claimservices.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.dto.ScrubberRulesDto;
import rovermd.project.claimservices.dto.copyClaim.institutional.ClaiminfomasterInstDto_CopyClaim;
import rovermd.project.claimservices.dto.copyClaim.professional.ClaiminfomasterProfDto_CopyClaim;
import rovermd.project.claimservices.dto.institutional.*;
import rovermd.project.claimservices.dto.viewSingleClaim.institutional.ClaiminfomasterInstDto_ViewSingleClaim;
import rovermd.project.claimservices.entity.*;
import rovermd.project.claimservices.exception.ResourceNotFoundException;
import rovermd.project.claimservices.repos.*;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimServiceInstitutional;
import rovermd.project.claimservices.service.ClaimServiceSrubber;
import rovermd.project.claimservices.service.ExternalService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ClaimServiceInstitutionalImpl implements ClaimServiceInstitutional {

    @Autowired
    private ClaiminfomasterRepository claimRepo;

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
    private ExternalService masterDefService;

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

        Claiminfomaster claim = claimRepo.findById(claimId).orElse(new Claiminfomaster());
        System.out.println("HERE");

        List<ScrubberRulesDto> rulesList = new ArrayList<>();

//        System.out.println(claim);
        if (claim.getClaimchargesinfo() == null) {
            System.out.println("IF");

            String claimNumber = claimRepo.getNewClaimNumber();
            claim.setClaimNumber("CI-" + claimNumber);
            //need to have patient Name , MRN , acct-no , phNumber , email, address , dos ,  physicianIdx wrt to visit
            // primary insurance name , primary memberId , primary grp Number , patients relationship to primary
            // secondary insurance name , secondary memberId , secondary grp Number , patients relationship to secondary
        } else {
            System.out.println("HERE");

            claim = filterChargesWrtStatus(claim);

            claiminfomasterInstDto_viewSingleClaim = claimToDto_ViewSingleClaim(claim);

            String InsuranceName = claim.getPriInsuranceNameId();
            InsuranceName = !isEmpty(InsuranceName) ? masterDefService.getInsuranceDetailsById(InsuranceName).getPayerName() : null;
            claiminfomasterInstDto_viewSingleClaim.setPriInsuranceName(
                    InsuranceName
            );

            InsuranceName = claim.getSecondaryInsuranceId();
            InsuranceName = !isEmpty(InsuranceName) ? masterDefService.getInsuranceDetailsById(InsuranceName).getPayerName() : null;
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

            if (claim.getScrubbed() == 1) {
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
        }


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

            IntStream.range(0, claimClaimchargesinfo.size())
                    .filter(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo() != null)
                    .forEach(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().setClaimchargesinfo(claim.getClaimchargesinfo().get(i)));

            IntStream.range(0, claimClaimchargesinfo.size())
                    .forEach(i -> claim.getClaimchargesinfo().get(i).setClaiminfomaster(claim));

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

            }

            List<?> scrubber = claimServiceSrubber.scrubberInst(claim);

            if (scrubber.size() > 0) {
                claim.setScrubbed(1);
            } else {
                claim.setScrubbed(0);
            }

            save = claimRepo.save(claim);
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
    public ClaiminfomasterInstDto updateClaim(ClaiminfomasterInstDto claimDTO, String remoteAddr) {
        List<ClaimchargesinfoDto> existingClaimChargesinfoDTO;
        List<ClaimchargesinfoDto> newClaimChargesinfoDTO;

        Claiminfomaster claim = filterChargesWrtStatus(claimRepo.findById(claimDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimDTO.getId())));
        ClaiminfomasterInstDto existingClaimDTO = filterChargesWrtStatus_DTO(claimToDto(claim));

        existingClaimChargesinfoDTO = existingClaimDTO.getClaimchargesinfo();
        newClaimChargesinfoDTO = claimDTO.getClaimchargesinfo();


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

            }


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

        modelMapper.map(claimDTO, claim);

        claim.setCreatedBy("Mouhid");
        claim.setCreatedIP(remoteAddr);

        claim.getClaimadditionalinfo().setClaiminfomaster(claim);

        if (claim.getClaiminformationcode() != null) {
            claim.getClaiminformationcode().setClaiminfomaster(claim);
        }

        List<Claimchargesinfo> claimClaimchargesinfo = claim.getClaimchargesinfo();
        IntStream.range(0, claimClaimchargesinfo.size())
                .filter(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo() != null)
                .forEach(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().setClaimchargesinfo(claim.getClaimchargesinfo().get(i)));

        List<?> scrubber = claimServiceSrubber.scrubberInst(claim);

        if (scrubber.size() > 0) {
            claim.setScrubbed(1);
        } else {
            claim.setScrubbed(0);
        }

        Claiminfomaster save = claimRepo.save(claim);

        return claimToDto(save);
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

    static boolean isEmpty(final String str) {
        return (str == null) || (str.length() <= 0);
    }


    static boolean isEmpty(Object str) {
        return (str == null);
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
