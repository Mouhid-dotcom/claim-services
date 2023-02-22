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
    String fullName;
    String npi;
    String phone;
    String state;
    String taxid;
    String taxanomySpecialty;
    String zipCode;
}