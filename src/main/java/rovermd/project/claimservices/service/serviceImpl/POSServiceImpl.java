package rovermd.project.claimservices.service.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rovermd.project.claimservices.dto.POSDto;
import rovermd.project.claimservices.entity.POS;
import rovermd.project.claimservices.repos.POSRepository;
import rovermd.project.claimservices.service.POSService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class POSServiceImpl implements POSService {
    @Autowired
    private POSRepository posRepo;

    @Override
    public List<POSDto> getPOS() {
        List<POS> tosList = posRepo.findPOSByStatusEquals("1");
        return tosList.stream().map(this::posToDto).collect(Collectors.toList());
    }

    private POSDto posToDto(POS pos) {
        POSDto posDto = new POSDto();
        BeanUtils.copyProperties(pos, posDto);
        return posDto;
    }
}
