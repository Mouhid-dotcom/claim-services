package rovermd.project.claimservices;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RoverMdClaimServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoverMdClaimServicesApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mp = new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mp;
    }
}
