package rovermd.project.claimservices.entity.claimMaster;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "toscodes")
@NoArgsConstructor
@Setter
@Getter


//Type Of Service
public class TOS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "Code")
    private String code;

    @Column(name = "Description")
    private String description;

    @Column(name = "status")
    private Integer status;

}
