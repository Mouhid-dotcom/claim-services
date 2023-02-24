package rovermd.project.claimservices.entity.claimMaster;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "claim_status_list")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClaimStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descname")
    private String Descname;

    @Column(name = "status")
    private Integer status;


}
