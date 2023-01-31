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
@Table(name = "espdtprocedures")
public class Espdtprocedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "ESPDT", length = 100)
    private String espdt;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private Instant createdDate;

}