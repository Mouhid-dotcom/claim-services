package rovermd.project.claimservices.dto.cms1500;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO_CMS1500 {
    String address;
    String city;
    String profName;
    String profNpi;
    String phone;
    String state;
    String profTaxId;
    String taxanomySpecialty;
    String zipCode;
}