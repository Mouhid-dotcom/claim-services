package rovermd.project.claimservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ICDMaster")
@NoArgsConstructor
@Setter
@Getter

//International Classification of Diseases
public class ICD {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ICD")
    private String ICD;

    @Column(name = "Description")
    private String Description;

    @Column(name = "Status")
    private Integer Status;

    @Column(name = "EffectiveDate")
    private LocalDateTime EffectiveDate;







}
