package rovermd.project.claimservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    String facilityId;
    String address;
    String chargeMasterTableName;
    String city;
    String directoryName;
    String fullName;
    String npi;
    String phone;
    String state;
    String taxid;
    String taxanomySpecialty;
    String zipCode;
    String dbname;
    String directory1;
    String doctype;
    String name;
    String remotedirectory;
    String activeIndex;
    String tablename;
    String profNpi;
    String profTaxId;

    String profName;
}