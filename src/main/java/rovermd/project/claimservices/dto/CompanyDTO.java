package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    Integer id;
    String organization;
    String tel;
    String email;
    String address;
    String city;
    String state;
    String zipCode;
}