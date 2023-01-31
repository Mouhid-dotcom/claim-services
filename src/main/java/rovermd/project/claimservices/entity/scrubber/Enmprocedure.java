package rovermd.project.claimservices.entity.scrubber;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "enmprocedures")
public class Enmprocedure {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @NotNull
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "CPT")
    private String cpt;

    @Size(max = 255)
    @Column(name = "Description")
    private String description;

    @Column(name = "Status")
    private Integer status;

    @Size(max = 255)
    @Column(name = "CreatedBy")
    private String createdBy;

}