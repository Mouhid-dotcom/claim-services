package rovermd.project.claimservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "claim_audittrails")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClaimAudittrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "RuleHTML")
    private String ruleHTML;

    @Size(max = 255)
    @Column(name = "ClaimNo")
    private String claimNo;

    @Size(max = 255)
    @Column(name = "ClaimType")
    private String claimType;

    @Size(max = 255)
    @Column(name = "UserID")
    private String userID;

    @Size(max = 255)
    @Column(name = "ClientID")
    private String clientID;

    @Column(name = "CreatedAt")
    private Instant createdAt;

    @Size(max = 255)
    @Column(name = "UserIP")
    private String userIP;

    @Size(max = 255)
    @Column(name = "Action")
    private String action;

    @Size(max = 255)
    @Column(name = "Reason")
    private String reason;

    @Lob
    @Column(name = "RuleText")
    private String ruleText;

    @PrePersist
    public void created() {
        createdAt = new Date().toInstant();
        userID = "MOUHID_CREATED";
        userIP = "127.0.0.1";
        if(claimNo.startsWith("CP-"))
            claimType="2";
        else
            claimType="1";
    }

}