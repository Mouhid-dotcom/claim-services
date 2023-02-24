package rovermd.project.claimservices.entity.claimMaster;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modifiercodes")
@NoArgsConstructor
@Setter
@Getter

//Modifiers
public class MOD {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "Code")
    private String code;

    @Column(name = "Description")
    private String description;

    @Column(name = "Status")
    private Integer status;

}
