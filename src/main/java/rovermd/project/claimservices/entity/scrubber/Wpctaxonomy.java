package rovermd.project.claimservices.entity.scrubber;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "wpctaxonomy")
public class Wpctaxonomy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "Codes", length = 100)
    private String codes;

    @Size(max = 255)
    @Column(name = "Groupings")
    private String groupings;

    @Size(max = 255)
    @Column(name = "Classification")
    private String classification;

    @Size(max = 255)
    @Column(name = "Specialization")
    private String specialization;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

}