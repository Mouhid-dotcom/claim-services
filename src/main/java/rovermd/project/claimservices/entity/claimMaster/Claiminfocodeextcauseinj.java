package rovermd.project.claimservices.entity.claimMaster;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "claiminfocodeextcauseinj")
@NoArgsConstructor
@Getter
@Setter
public class Claiminfocodeextcauseinj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

//    @JsonBackReference
//    @OneToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name="ClaimInfoMasterId", referencedColumnName="Id")
//    Claiminfomaster claiminfomaster;
//
//    @Size(max = 255)
//    @Column(name = "ClaimNumber")
//    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="ClaimInfoMasterId", referencedColumnName="Id"),
            @JoinColumn(name="ClaimNumber", referencedColumnName="ClaimNumber")
    })
    Claiminfomaster claiminfomaster;

    @Size(max = 255)
    @Column(name = "Code")
    private String code;

    @Column(name = "Status")
    private Integer status;

    @Size(max = 255)
    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Size(max = 255)
    @Column(name = "CreatedIP")
    private String createdIP;

    @Size(max = 255)
    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "UpdatedAt")
    private Instant updatedAt;

    @PrePersist
    public void added() {
        createdDate = new Date().toInstant();
        createdBy = "MOUHID_CREATED";

    }

    @PreUpdate
    public void Updated() {
        updatedAt = new Date().toInstant();
        updatedBy = "MOUHID_UPDATED";

    }
}