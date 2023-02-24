package rovermd.project.claimservices.service.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rovermd.project.claimservices.dto.ClaimStatusDto;
import rovermd.project.claimservices.entity.claimMaster.ClaimStatus;
import rovermd.project.claimservices.repos.ClaimStatusRepository;
import rovermd.project.claimservices.service.ClaimStatusService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimStatusServiceImpl implements ClaimStatusService {

    @Autowired
    private ClaimStatusRepository claimStatusRepo;

    @Override
    public List<ClaimStatusDto> getClaimStatus() {
        List<ClaimStatus> claimStatusList = claimStatusRepo.findClaimStatusByStatusEquals("1");
        return claimStatusList.stream().map(this::claimStatusToDto).collect(Collectors.toList());
    }

    private ClaimStatusDto claimStatusToDto(ClaimStatus claimStatus) {
        ClaimStatusDto claimStatusDto= new ClaimStatusDto();
        BeanUtils.copyProperties(claimStatus,claimStatusDto);
        return claimStatusDto;
    }
}
