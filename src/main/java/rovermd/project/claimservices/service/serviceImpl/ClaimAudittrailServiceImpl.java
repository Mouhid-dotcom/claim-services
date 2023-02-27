package rovermd.project.claimservices.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rovermd.project.claimservices.dto.ClaimAudittrailDto;
import rovermd.project.claimservices.entity.claimMaster.ClaimAudittrail;
import rovermd.project.claimservices.repos.claimMaster.ClaimAudittrailRepository;
import rovermd.project.claimservices.service.ClaimAudittrailService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimAudittrailServiceImpl implements ClaimAudittrailService {

    @Autowired
    private ClaimAudittrailRepository claimAudittrailRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ClaimAudittrail createAuditTrail(ClaimAudittrail claimAudittrail) {
        return claimAudittrailRepository.save(claimAudittrail);
    }

    @Override
    public List<ClaimAudittrailDto> findAllByClaimNumber(String ClaimNumber) {
        List<ClaimAudittrail> claimAudittrailList = claimAudittrailRepository.findByClaimNo(ClaimNumber);
        return claimAudittrailList.stream().map(this::claimToDto).collect(Collectors.toList());
    }


    private ClaimAudittrailDto claimToDto(ClaimAudittrail claimAudittrail) {
        return modelMapper.map(claimAudittrail, ClaimAudittrailDto.class);
    }
}
