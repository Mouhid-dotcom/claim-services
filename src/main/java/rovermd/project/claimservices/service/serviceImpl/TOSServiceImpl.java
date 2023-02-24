package rovermd.project.claimservices.service.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rovermd.project.claimservices.dto.TOSDto;
import rovermd.project.claimservices.entity.claimMaster.TOS;
import rovermd.project.claimservices.repos.TOSRepository;
import rovermd.project.claimservices.service.TOSService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TOSServiceImpl implements TOSService {

    @Autowired
    private TOSRepository tosRepo;


    @Override
    public List<TOSDto> getTOS() {
        List<TOS> tosList = tosRepo.findTOSByStatusEquals("1");
        return tosList.stream().map(this::tosToDto).collect(Collectors.toList());
    }

    private TOSDto tosToDto(TOS tos) {
        TOSDto tosDto= new TOSDto();
        BeanUtils.copyProperties(tos,tosDto);
        return tosDto;
    }


}
