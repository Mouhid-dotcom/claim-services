package rovermd.project.claimservices.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rovermd.project.claimservices.dto.ClaimInfoMaster_List;
import rovermd.project.claimservices.dto.ScrubberRulesDto;
import rovermd.project.claimservices.dto.SuccessMsg;
import rovermd.project.claimservices.dto.copyClaim.institutional.ClaiminfomasterInstDto_CopyClaim;
import rovermd.project.claimservices.dto.copyClaim.professional.ClaiminfomasterProfDto_CopyClaim;
import rovermd.project.claimservices.dto.professional.ClaimadditionalinfoDto;
import rovermd.project.claimservices.dto.professional.ClaimambulancecodeDto;
import rovermd.project.claimservices.dto.professional.ClaimchargesinfoDto;
import rovermd.project.claimservices.dto.professional.ClaiminfomasterProfDto;
import rovermd.project.claimservices.dto.viewSingleClaim.professional.ClaimchargesinfoDto_ViewSingleClaim;
import rovermd.project.claimservices.dto.viewSingleClaim.professional.ClaiminfomasterProfDto_ViewSingleClaim;
import rovermd.project.claimservices.entity.*;
import rovermd.project.claimservices.exception.ResourceNotFoundException;
import rovermd.project.claimservices.repos.*;
import rovermd.project.claimservices.service.ClaimAudittrailService;
import rovermd.project.claimservices.service.ClaimServiceProfessional;
import rovermd.project.claimservices.service.ClaimServiceSrubber;
import rovermd.project.claimservices.service.ExternalService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ClaimServiceProfessionalImpl implements ClaimServiceProfessional {

    @Autowired
    private ClaiminfomasterRepository claimRepo;

    @Autowired
    private TOSRepository tOSRepository;

    @Autowired
    private POSRepository pOSRepository;

    @Autowired
    private CPTRepository cPTRepository;

    @Autowired
    private ClaimStatusRepository claimStatusRepository;


    @Autowired
    private ClaimAudittrailService claimAudittrailService;

    @Autowired
    private ExternalService externalService;

    @Autowired
    private ClaimServiceSrubber claimServiceSrubber;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MODRepository mODRepository;
    @Autowired
    private ClaimAudittrailRepository claimAudittrailRepository;


    @Override
    public ClaiminfomasterProfDto_ViewSingleClaim getClaimById(Integer claimId) {

        ObjectNode cptCodeAndDescription = null;
        ObjectNode modCodeAndDescription = null;
        ObjectNode StatusCodeAndDescription = null;
        ObjectNode tosCodeAndDescription = null;
        ObjectNode posCodeAndDescription = null;


        ClaiminfomasterProfDto_ViewSingleClaim claiminfomasterProfDto_viewSingleClaim = null;
        Claiminfomaster claim = claimRepo.findById(claimId).orElse(new Claiminfomaster());
        List<ScrubberRulesDto> rulesList = new ArrayList<>();

        if (claim.getClaimchargesinfo() == null) {
            String claimNumber = claimRepo.getNewClaimNumber();
            claim.setClaimNumber("CP-" + claimNumber);
            //need to have patient Name , MRN , acct-no , phNumber , email, address , dos ,  physicianIdx wrt to visit
            // primary insurance name , primary memberId , primary grp Number , patients relationship to primary
            // secondary insurance name , secondary memberId , secondary grp Number , patients relationship to secondary
        } else {

            claim = filterChargesWrtStatus(claim);

            claiminfomasterProfDto_viewSingleClaim = claimToDto_ViewSingleClaim(claim);

            String InsuranceName = claim.getPriInsuranceNameId();
            InsuranceName = !isEmpty(InsuranceName) ? externalService.getInsuranceDetailsById(InsuranceName).getPayerName() : null;
            claiminfomasterProfDto_viewSingleClaim.setPriInsuranceName(
                    InsuranceName
            );

            InsuranceName = claim.getSecondaryInsuranceId();
            InsuranceName = !isEmpty(InsuranceName) ? externalService.getInsuranceDetailsById(InsuranceName).getPayerName() : null;
            claiminfomasterProfDto_viewSingleClaim.setSecondaryInsurance(
                    InsuranceName
            );

            for (int i = 0; i < claim.getClaimchargesinfo().size(); i++) {

                if (!isEmpty(claim.getClaimchargesinfo().get(i))) {

                    cptCodeAndDescription = getCodeAndDescriptionForCPT(claim.getClaimchargesinfo().get(i).getHCPCSProcedure());
                    claiminfomasterProfDto_viewSingleClaim.getClaimchargesinfo().get(i).setHCPCSProcedure(
                            cptCodeAndDescription
                    );


                    posCodeAndDescription = getCodeAndDescriptionForPOS(claim.getClaimchargesinfo().get(i).getPos());
                    claiminfomasterProfDto_viewSingleClaim.getClaimchargesinfo().get(i).setPos(
                            posCodeAndDescription
                    );

                    tosCodeAndDescription = getCodeAndDescriptionForTOS(claim.getClaimchargesinfo().get(i).getTos());
                    claiminfomasterProfDto_viewSingleClaim.getClaimchargesinfo().get(i).setTos(
                            tosCodeAndDescription
                    );

                    modCodeAndDescription = getCodeAndDescriptionForMod(claim.getClaimchargesinfo().get(i).getMod1());
                    claiminfomasterProfDto_viewSingleClaim.getClaimchargesinfo().get(i).setMod1(
                            modCodeAndDescription
                    );

                    modCodeAndDescription = getCodeAndDescriptionForMod(claim.getClaimchargesinfo().get(i).getMod2());
                    claiminfomasterProfDto_viewSingleClaim.getClaimchargesinfo().get(i).setMod2(
                            modCodeAndDescription
                    );

                    modCodeAndDescription = getCodeAndDescriptionForMod(claim.getClaimchargesinfo().get(i).getMod3());
                    claiminfomasterProfDto_viewSingleClaim.getClaimchargesinfo().get(i).setMod3(
                            modCodeAndDescription
                    );

                    modCodeAndDescription = getCodeAndDescriptionForMod(claim.getClaimchargesinfo().get(i).getMod4());
                    claiminfomasterProfDto_viewSingleClaim.getClaimchargesinfo().get(i).setMod4(
                            modCodeAndDescription
                    );


                    StatusCodeAndDescription = getCodeAndDescriptionForChargeStatus(claim.getClaimchargesinfo().get(i).getChargesStatus());
                    claiminfomasterProfDto_viewSingleClaim.getClaimchargesinfo().get(i).setChargesStatus(
                            StatusCodeAndDescription
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
                claiminfomasterProfDto_viewSingleClaim.setScrubber(rulesList);
            }


        }

        return claiminfomasterProfDto_viewSingleClaim;
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


    private ObjectNode getCodeAndDescriptionForTOS(String tos) {
        String desc = tOSRepository.findTOSByCode(tos).getDescription();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objNode = mapper.createObjectNode();
        objNode.put("code", tos);
        objNode.put("description", !isEmpty(desc) ? desc : "");
        return objNode;
    }

    private ObjectNode getCodeAndDescriptionForPOS(String pos) {
        String desc = pOSRepository.findPOSByCode(pos).getDescription();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objNode = mapper.createObjectNode();
        objNode.put("code", pos);
        objNode.put("description", !isEmpty(desc) ? desc : "");
        return objNode;
    }


    private ObjectNode getCodeAndDescriptionForCPT(String cpt) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objNode = mapper.createObjectNode();
        List<CPT> cptByCPTCode = cPTRepository.findCPTByCPTCode(cpt);

        if (!isEmpty(cptByCPTCode) && cptByCPTCode.size() > 0) {
            String desc = cptByCPTCode.get(0).getCPTDescription();
            objNode.put("code", cpt);
            objNode.put("description", !isEmpty(desc) ? desc : "");
        } else {
            objNode.put("code", cpt);
            objNode.put("description", "");
        }

        return objNode;
    }

    @Override
    public ClaiminfomasterInstDto_CopyClaim copyClaim(Integer claimID) {
        Claiminfomaster claim = claimRepo.findById(claimID).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimID));

        ClaiminfomasterInstDto_CopyClaim claiminfomasterInstDto = claimToDto_InstCopyClaim(claim);
        ClaiminfomasterProfDto_CopyClaim claiminfomasterProfDto = claimToDto_ProfCopyClaim(claim);

        modelMapper.map(claiminfomasterProfDto,
                claiminfomasterInstDto);
        String claimNumber = claimRepo.getNewClaimNumber();
        claiminfomasterInstDto.setClaimNumber("CI-" + claimNumber);

        return claiminfomasterInstDto;
    }

    @Override
    public List<ClaimInfoMaster_List> getAllClaims(String key) {

        List<Object[]> claimList = key == null ? claimRepo.getListOfCreatedClaims() : claimRepo.getListOfCreatedClaimsFiltered(key);

        List<ClaimInfoMaster_List> claimLList = new ArrayList<>();

        for (Object[] objects : claimList) {
            ClaimInfoMaster_List claimInfoMaster_List = new ClaimInfoMaster_List();

            claimInfoMaster_List.setType(objects[0]);
            claimInfoMaster_List.setClaimNo(objects[1]);
            claimInfoMaster_List.setPatientName(objects[2]);
            claimInfoMaster_List.setDateOfService(objects[3]);
            claimInfoMaster_List.setTotalCharges(objects[4]);
            claimInfoMaster_List.setBalance(objects[5]);
            claimInfoMaster_List.setStatus(objects[6]);

            claimLList.add(claimInfoMaster_List);

        }
        return claimLList;//.stream().map(this::claimToDto_List).collect(Collectors.toList());
    }

    @Override
    public List<ClaimInfoMaster_List> getAllClaimsByPatRegIDAndVisitId(Integer patRegID,Integer visitID) {

        List<Object[]> claimList =  claimRepo.getListOfCreatedClaimsFilteredByPatRegIDORVisitID(patRegID,visitID) ;

        List<ClaimInfoMaster_List> claimLList = new ArrayList<>();

        for (Object[] objects : claimList) {
            ClaimInfoMaster_List claimInfoMaster_List = new ClaimInfoMaster_List();

            claimInfoMaster_List.setType(objects[0]);
            claimInfoMaster_List.setClaimNo(objects[1]);
            claimInfoMaster_List.setPatientName(objects[2]);
            claimInfoMaster_List.setDateOfService(objects[3]);
            claimInfoMaster_List.setTotalCharges(objects[4]);
            claimInfoMaster_List.setBalance(objects[5]);
            claimInfoMaster_List.setStatus(objects[6]);

            claimLList.add(claimInfoMaster_List);

        }
        return claimLList;//.stream().map(this::claimToDto_List).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public SuccessMsg createClaim(ClaiminfomasterProfDto claimDTO, String remoteAddr) {
        Claiminfomaster save = null;
        List<ClaimchargesinfoDto> newClaimChargesinfoDTO = claimDTO.getClaimchargesinfo();

        Claiminfomaster claim;

        try {
            claim = dtoToClaim(claimDTO);

            String claimNumber = claimRepo.getNewClaimNumber();
            claim.setClaimNumber("CP-" + claimNumber);
            claim.setCreatedBy("Mouhid");
            claim.setCreatedIP(remoteAddr);

            claim.getClaimadditionalinfo().setClaiminfomaster(claim);

            if (claim.getClaimambulancecode() != null) {
                claim.getClaimambulancecode().setClaiminfomaster(claim);
            }

            List<Claimchargesinfo> claimClaimchargesinfo = claim.getClaimchargesinfo();

            IntStream.range(0, claimClaimchargesinfo.size())
                    .filter(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo() != null)
                    .forEach(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().setClaimchargesinfo(claim.getClaimchargesinfo().get(i)));

            IntStream.range(0, claimClaimchargesinfo.size())
                    .forEach(i -> claim.getClaimchargesinfo().get(i).setClaiminfomaster(claim));


            //check it once as every charge contains ICDs in their object
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcda(), "ICD A ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdb(), "ICD B ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdc(), "ICD C ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdd(), "ICD D ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcde(), "ICD E ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdf(), "ICD F ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdg(), "ICD G ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdh(), "ICD H ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdi(), "ICD I ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdj(), "ICD J ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdk(), "ICD K ", claim);
            compareICDs(null, newClaimChargesinfoDTO.get(0).getIcdl(), "ICD L ", claim);


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
                        isEmpty(claimchargesinfoDto.getPos()) ? null : pOSRepository.findById(Long.valueOf(claimchargesinfoDto.getPos())).get().getDescription(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "POS"
                );

                compareClaimChargesInfoAttr(
                        null,
                        isEmpty(claimchargesinfoDto.getTos()) ? null : tOSRepository.findById(Long.valueOf(claimchargesinfoDto.getTos())).get().getDescription(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "TOS"
                );


                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getDXPointer(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "DX Pointer"
                );

                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getServiceFromDate(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "Service date [FROM]"
                );


                compareClaimChargesInfoAttr(
                        null,
                        claimchargesinfoDto.getServiceToDate(),
                        claim,
                        claimchargesinfoDto.getHCPCSProcedure(),
                        "Service date [TO]"
                );


            }

            List<?> scrubber = claimServiceSrubber.scrubberProf(claim);

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

        SuccessMsg successMsg = new SuccessMsg();
        successMsg.setMessage("Claim Saved Successfully");
        successMsg.setStatuscode("OK");
        successMsg.setClaimId(save.getId());
        successMsg.setClaimNo(save.getClaimNumber());


//        return claimToDto(save);
        return successMsg;
    }

    @Transactional
    @Override
    public ClaiminfomasterProfDto updateClaim(ClaiminfomasterProfDto claimDTO, String remoteAddr) {
        List<ClaimchargesinfoDto> existingClaimChargesinfoDTO;
        List<ClaimchargesinfoDto> newClaimChargesinfoDTO;

        Claiminfomaster claim = filterChargesWrtStatus(claimRepo.findById(claimDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimDTO.getId())));
        ClaiminfomasterProfDto existingClaimDTO = filterChargesWrtStatus_DTO(claimToDto(claim));


        existingClaimChargesinfoDTO = existingClaimDTO.getClaimchargesinfo();
        newClaimChargesinfoDTO = claimDTO.getClaimchargesinfo();


        if (!existingClaimChargesinfoDTO.equals(newClaimChargesinfoDTO)) {

            for (ClaimchargesinfoDto claimchargesinfoDto : newClaimChargesinfoDTO) {
                if (claimchargesinfoDto.getId() == null) {
                    insertClaimAuditTrails(claim,
                            "CPT : [" + claimchargesinfoDto.getHCPCSProcedure() + "] is added",
                            "ADDED");
                }
            }

            //check it once as every charge contains ICDs in their object
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcda(), newClaimChargesinfoDTO.get(0).getIcda(), "ICD A ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdb(), newClaimChargesinfoDTO.get(0).getIcdb(), "ICD B ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdc(), newClaimChargesinfoDTO.get(0).getIcdc(), "ICD C ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdd(), newClaimChargesinfoDTO.get(0).getIcdd(), "ICD D ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcde(), newClaimChargesinfoDTO.get(0).getIcde(), "ICD E ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdf(), newClaimChargesinfoDTO.get(0).getIcdf(), "ICD F ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdg(), newClaimChargesinfoDTO.get(0).getIcdg(), "ICD G ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdh(), newClaimChargesinfoDTO.get(0).getIcdh(), "ICD H ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdi(), newClaimChargesinfoDTO.get(0).getIcdi(), "ICD I ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdj(), newClaimChargesinfoDTO.get(0).getIcdj(), "ICD J ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdk(), newClaimChargesinfoDTO.get(0).getIcdk(), "ICD K ", claim);
            compareICDs(existingClaimChargesinfoDTO.get(0).getIcdl(), newClaimChargesinfoDTO.get(0).getIcdl(), "ICD L ", claim);


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
                            isEmpty(existingClaimChargesinfoDTO.get(i).getPos()) ? null : pOSRepository.findById(Long.valueOf(existingClaimChargesinfoDTO.get(i).getPos())).get().getDescription(),
                            isEmpty(newClaimChargesinfoDTO.get(i).getPos()) ? null : pOSRepository.findById(Long.valueOf(newClaimChargesinfoDTO.get(i).getPos())).get().getDescription(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "POS"
                    );

                    compareClaimChargesInfoAttr(
                            isEmpty(existingClaimChargesinfoDTO.get(i).getTos()) ? null : tOSRepository.findById(Long.valueOf(existingClaimChargesinfoDTO.get(i).getTos())).get().getDescription(),
                            isEmpty(newClaimChargesinfoDTO.get(i).getTos()) ? null : tOSRepository.findById(Long.valueOf(newClaimChargesinfoDTO.get(i).getTos())).get().getDescription(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "TOS"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getDXPointer(),
                            newClaimChargesinfoDTO.get(i).getDXPointer(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "DX Pointer"
                    );

                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getServiceFromDate(),
                            newClaimChargesinfoDTO.get(i).getServiceFromDate(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "Service date [FROM]"
                    );


                    compareClaimChargesInfoAttr(
                            existingClaimChargesinfoDTO.get(i).getServiceToDate(),
                            newClaimChargesinfoDTO.get(i).getServiceToDate(),
                            claim,
                            newClaimChargesinfoDTO.get(i).getHCPCSProcedure(),
                            "Service date [TO]"
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

        if (claim.getClaimambulancecode() != null) {
            claim.getClaimambulancecode().setClaiminfomaster(claim);
        }

        List<Claimchargesinfo> claimClaimchargesinfo = claim.getClaimchargesinfo();
        IntStream.range(0, claimClaimchargesinfo.size())
                .filter(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo() != null)
                .forEach(i -> claim.getClaimchargesinfo().get(i).getClaimchargesotherinfo().setClaimchargesinfo(claim.getClaimchargesinfo().get(i)));

        List<?> scrubber = claimServiceSrubber.scrubberProf(claim);

        if (scrubber.size() > 0) {
            claim.setScrubbed(1);
        } else {
            claim.setScrubbed(0);
        }

        Claiminfomaster save = claimRepo.save(claim);

//        claim


        return claimToDto(save);
    }

    private ClaiminfomasterProfDto filterChargesWrtStatus_DTO(ClaiminfomasterProfDto claim) {

        claim.setClaimchargesinfo(
                claim.getClaimchargesinfo()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaimchargesinfoDto.class))
                        .collect(Collectors.toList())
        );

        return claim;
    }

    private ClaiminfomasterProfDto_ViewSingleClaim filterChargesWrtStatus_DTO(ClaiminfomasterProfDto_ViewSingleClaim claim) {

        claim.setClaimchargesinfo(
                claim.getClaimchargesinfo()
                        .stream()
                        .filter(x -> x.getStatus() == 1)
                        .map(x -> modelMapper.map(x, ClaimchargesinfoDto_ViewSingleClaim.class))
                        .collect(Collectors.toList())
        );

        return claim;
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

        return claim;
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

    private void compareICDs(final String existingICD, final String newICD, final String icdPrefix, final Claiminfomaster claim) {
        if (isEmpty(existingICD) && !isEmpty(newICD)) {
            insertClaimAuditTrails(claim,
                    icdPrefix + " : " + newICD + " is added",
                    "ADDED");
        } else if (isEmpty(newICD) && !isEmpty(existingICD)) {
            insertClaimAuditTrails(claim,
                    icdPrefix + " : " + existingICD + " is removed",
                    "REMOVED");
        } else if (!isEmpty(newICD) && !isEmpty(existingICD)) {
            if (existingICD.compareTo(newICD) != 0) {
                insertClaimAuditTrails(claim,
                        icdPrefix + " is changed from " + existingICD + " to " + newICD,
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

    private void insertClaimAuditTrails(Claiminfomaster claim, String description, String action) {
        claimAudittrailService.createAuditTrail(ClaimAudittrail.builder()
                .claimNo(claim.getClaimNumber())
                .claimType(String.valueOf(claim.getClaimType() == null ? 2 : claim.getClaimType()))
                .userID(claim.getCreatedBy())
                .userIP(claim.getCreatedIP())
                .clientID(String.valueOf(claim.getClientId()))
                .userIP(claim.getCreatedIP())
                .action(action)
                .ruleText(description).build());
    }


    private ClaiminfomasterProfDto_CopyClaim claimToDto_ProfCopyClaim(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterProfDto_CopyClaim.class);
    }

    private ClaiminfomasterInstDto_CopyClaim claimToDto_InstCopyClaim(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterInstDto_CopyClaim.class);
    }


    private Claiminfomaster dtoToClaim_manually(ClaiminfomasterProfDto claimDTO) {
        Claiminfomaster claiminfomaster = new Claiminfomaster();
        List<Claimchargesinfo> claimchargesinfo = new ArrayList<>();
        Claimambulancecode claimambulancecode = new Claimambulancecode();
        Claimadditionalinfo claimadditionalinfo = new Claimadditionalinfo();

        BeanUtils.copyProperties(claimDTO, claiminfomaster);

        if (claimDTO.getClaimchargesinfo() != null) {
            IntStream.range(0, claimDTO.getClaimchargesinfo().size()).forEach(i -> {
                Claimchargesinfo Claimchargesinfo = new Claimchargesinfo();
                BeanUtils.copyProperties(claimDTO.getClaimchargesinfo().get(i), Claimchargesinfo);
                claimchargesinfo.add(Claimchargesinfo);
            });
            claiminfomaster.setClaimchargesinfo(claimchargesinfo);
        }

        if (claimDTO.getClaimadditionalinfo() != null) {
            BeanUtils.copyProperties(claimDTO.getClaimadditionalinfo(), claimadditionalinfo);
            claiminfomaster.setClaimadditionalinfo(claimadditionalinfo);
        }

        if (claimDTO.getClaimambulancecode() != null) {
            BeanUtils.copyProperties(claimDTO.getClaimambulancecode(), claimambulancecode);
            claiminfomaster.setClaimambulancecode(claimambulancecode);
        }


        return claiminfomaster;
    }


    private ClaiminfomasterProfDto ClaimToDto_manually(Claiminfomaster claim) {
        ClaiminfomasterProfDto claiminfomasterDto = new ClaiminfomasterProfDto();
        List<ClaimchargesinfoDto> claimchargesinfoDto = new ArrayList<>();
        ClaimambulancecodeDto claimambulancecodeDto = new ClaimambulancecodeDto();
        ClaimadditionalinfoDto claimadditionalinfoDto = new ClaimadditionalinfoDto();


        BeanUtils.copyProperties(claim, claiminfomasterDto);

        if (claim.getClaimchargesinfo() != null) {
            IntStream.range(0, claim.getClaimchargesinfo().size()).forEach(i -> {
                ClaimchargesinfoDto ClaimchargesinfoDto = new ClaimchargesinfoDto();
                BeanUtils.copyProperties(claim.getClaimchargesinfo().get(i), ClaimchargesinfoDto);
                claimchargesinfoDto.add(ClaimchargesinfoDto);
            });
            claiminfomasterDto.setClaimchargesinfo(claimchargesinfoDto);
        }

        if (claim.getClaimadditionalinfo() != null) {
            BeanUtils.copyProperties(claim.getClaimadditionalinfo(), claimadditionalinfoDto);
            claiminfomasterDto.setClaimadditionalinfo(claimadditionalinfoDto);
        }

        if (claim.getClaimambulancecode() != null) {
            BeanUtils.copyProperties(claim.getClaimambulancecode(), claimambulancecodeDto);
            claiminfomasterDto.setClaimambulancecode(claimambulancecodeDto);
        }


        return claiminfomasterDto;
    }

    private ClaiminfomasterProfDto claimToDto(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterProfDto.class);
    }

    private ClaimInfoMaster_List claimToDto_List(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaimInfoMaster_List.class);
    }

    private ClaiminfomasterProfDto_ViewSingleClaim claimToDto_ViewSingleClaim(Claiminfomaster claim) {
        return modelMapper.map(claim, ClaiminfomasterProfDto_ViewSingleClaim.class);
    }

    private Claiminfomaster dtoToClaim(ClaiminfomasterProfDto claiminfomasterDto) {
        return modelMapper.map(claiminfomasterDto, Claiminfomaster.class);
    }


}
