package rovermd.project.claimservices.entity.scrubber;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

/**
 * Mapping for DB view
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Immutable
@Table(name = "cci_not_allowed")
public class CciNotAllowed {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "CPT_1")
    private String cpt1;

    @Size(max = 255)
    @Column(name = "CPT_2")
    private String cpt2;

    @Size(max = 255)
    @Column(name = "Modifier")
    private String modifier;

    @Size(max = 255)
    @Column(name = "Deletion")
    private String deletion;

}