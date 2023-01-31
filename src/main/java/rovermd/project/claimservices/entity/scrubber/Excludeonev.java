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
@Table(name = "excludeonev")
public class Excludeonev {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "Code", length = 100)
    private String code;

    @Size(max = 100)
    @Column(name = "Excluded_Code", length = 100)
    private String excludedCode;

    @Size(max = 2)
    @Column(name = "ExcludeOne", length = 2)
    private String excludeOne;

}