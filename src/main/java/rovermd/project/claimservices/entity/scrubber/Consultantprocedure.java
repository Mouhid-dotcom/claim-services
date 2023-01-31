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
@Table(name = "consultantprocedures")
public class Consultantprocedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CPTCode", length = 50)
    private String cPTCode;

    @Size(max = 255)
    @Column(name = "ShortDescription")
    private String shortDescription;

    @Size(max = 255)
    @Column(name = "TypeOfService")
    private String typeOfService;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

}