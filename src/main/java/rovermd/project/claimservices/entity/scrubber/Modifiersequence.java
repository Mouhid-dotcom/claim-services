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
@Table(name = "modifiersequence")
public class Modifiersequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 3)
    @Column(name = "`Order`", length = 3)
    private String order;

    @Size(max = 3)
    @Column(name = "ModifierCode", length = 3)
    private String modifierCode;

    @Size(max = 255)
    @Column(name = "`Group`")
    private String group;

}