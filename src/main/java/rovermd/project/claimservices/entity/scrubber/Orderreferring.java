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
@Table(name = "orderreferring")
public class Orderreferring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "NPI", length = 100)
    private String npi;

    @Size(max = 255)
    @Column(name = "LastName")
    private String lastName;

    @Size(max = 255)
    @Column(name = "FirstName")
    private String firstName;

    @Size(max = 2)
    @Column(name = "PartB", length = 2)
    private String partB;

    @Size(max = 2)
    @Column(name = "DME", length = 2)
    private String dme;

    @Size(max = 2)
    @Column(name = "HHA", length = 2)
    private String hha;

    @Size(max = 2)
    @Column(name = "PMD", length = 2)
    private String pmd;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

}