package rovermd.project.claimservices.entity.scrubber;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ndc_cpt_crosswalk")
public class NdcCptCrosswalk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "Status")
    private Integer status;

    @Size(max = 255)
    @Column(name = "HCPCS_Code")
    private String hcpcsCode;

    @Size(max = 255)
    @Column(name = "ErrorMsg")
    private String errorMsg;

    @Size(max = 255)
    @Column(name = "ShortDescriptor")
    private String shortDescriptor;

    @Size(max = 255)
    @Column(name = "LabellerName")
    private String labellerName;

    @Size(max = 255)
    @Column(name = "NDC")
    private String ndc;

    @Size(max = 255)
    @Column(name = "DrugName")
    private String drugName;

    @Size(max = 255)
    @Column(name = "HCPCS_Dosage")
    private String hcpcsDosage;

    @Column(name = "PKG_Size")
    private Double pkgSize;

    @Column(name = "PKG_Qty")
    private Integer pkgQty;

    @Size(max = 255)
    @Column(name = "BillUnits")
    private String billUnits;

    @Size(max = 255)
    @Column(name = "BillUnits_PKG")
    private String billunitsPkg;

}