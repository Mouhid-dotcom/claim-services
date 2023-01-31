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
@Table(name = "zipcodelibrary")
public class Zipcodelibrary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "Status")
    private Integer status;

    @Size(max = 255)
    @Column(name = "ZIPCode")
    private String zIPCode;

    @Size(max = 255)
    @Column(name = "Type")
    private String type;

    @Size(max = 255)
    @Column(name = "City")
    private String city;

    @Size(max = 255)
    @Column(name = "State")
    private String state;

    @Size(max = 255)
    @Column(name = "StateName")
    private String stateName;

}