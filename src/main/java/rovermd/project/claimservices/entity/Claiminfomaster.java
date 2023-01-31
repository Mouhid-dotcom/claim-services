package rovermd.project.claimservices.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rovermd.project.claimservices.util.AuditTrail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

//@EntityListeners(AuditTrail.class)
@Entity
@Table(name = "claiminfomaster")
@NoArgsConstructor
@Setter
@Getter
public class Claiminfomaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;


//    @Override
//    public String toString() {
//        return super.toString();
//    }

    @Column(name = "ClientId")
    private Integer clientId;

    @Column(name = "PatientRegId")
    private Integer patientRegId;

    @Column(name = "VisitId")
    private Integer visitId;

    @Size(max = 255)
    @Column(name = "ClaimNumber",nullable = false)
    private String claimNumber;

    @Size(max = 255)
    @Column(name = "RefNumber")
    private String refNumber;

    @Size(max = 255)
    @Column(name = "TypeBillText")
    private String typeBillText;

    @Size(max = 255)
    @Column(name = "PatientName")
    private String patientName;

    @Size(max = 255)
    @Column(name = "PatientMRN")
    private String patientMRN;

    @Size(max = 255)
    @Column(name = "AcctNo")
    private String acctNo;

    @Size(max = 255)
    @Column(name = "PhNumber")
    private String phNumber;

    @Size(max = 255)
    @Column(name = "Email")
    private String email;

    @Lob
    @Column(name = "Address")
    private String address;

    @Size(max = 255)
    @Column(name = "DOS")
    private String dos;

    @Size(max = 255)
    @Column(name = "UploadDate")
    private String uploadDate;

    @Size(max = 255)
    @Column(name = "AttendingProvider")
    private String attendingProvider;

    @Size(max = 255)
    @Column(name = "ReferringProvider")
    private String referringProvider;

    @Size(max = 255)
    @Column(name = "BillingProviders")
    private String billingProviders;

    @Size(max = 255)
    @Column(name = "ClientName")
    private String clientName;

    @Size(max = 255)
    @Column(name = "PriInsuranceNameId")
    private String priInsuranceNameId;

    @Size(max = 255)
    @Column(name = "MemId")
    private String memId;

    @Size(max = 255)
    @Column(name = "PolicyType")
    private String policyType;

    @Size(max = 255)
    @Column(name = "GrpNumber")
    private String grpNumber;
    @Size(max = 255)
    @Column(name = "PatientRelationtoPrimary")
    private String PatientRelationtoPrimary;

    @Size(max = 255)
    @Column(name = "SecondaryInsuranceId")
    private String secondaryInsuranceId;

    @Size(max = 255)
    @Column(name = "SecondaryInsuranceMemId")
    private String secondaryInsuranceMemId;

    @Size(max = 255)
    @Column(name = "SecondaryInsuranceGrpNumber")
    private String secondaryInsuranceGrpNumber;

    @Size(max = 255)
    @Column(name = "PatientRelationshiptoSecondary")
    private String PatientRelationshiptoSecondary;


    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Size(max = 255)
    @Column(name = "CreatedBy")
    private String createdBy;

    @Size(max = 255)
    @Column(name = "CreatedIP")
    private String createdIP;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "ClaimType")
    private Integer claimType;

    @Size(max = 255)
    @Column(name = "OperatingProvider")
    private String operatingProvider;

    @Size(max = 255)
    @Column(name = "Freq")
    private String freq;

    @Size(max = 255)
    @Column(name = "RenderingProvider")
    private String renderingProvider;

    @Size(max = 255)
    @Column(name = "SupervisingProvider")
    private String supervisingProvider;

    @Size(max = 255)
    @Column(name = "OrderingProvider")
    private String orderingProvider;

    @Size(max = 255)
    @Column(name = "InterControlNo")
    private String interControlNo;

    @Column(name = "ClaimProgress")
    private Integer claimProgress;

    @Column(name = "ReadytoSubmit")
    private Integer readytoSubmit;

    @Column(name = "timesSubmitted")
    private Integer timesSubmitted;

    @Column(name = "TotalCharges")
    private Double totalCharges;

    @Column(name = "Balance")
    private Double balance;

    @Size(max = 255)
    @Column(name = "PCN")
    private String pcn;

    @Size(max = 255)
    @Column(name = "Allowed")
    private String allowed;

    @Size(max = 255)
    @Column(name = "Paid")
    private String paid;

    @Size(max = 255)
    @Column(name = "Adjusted")
    private String adjusted;

    @Size(max = 255)
    @Column(name = "Unpaid")
    private String unpaid;

    @Size(max = 255)
    @Column(name = "AdditionalActions")
    private String additionalActions;

    @Size(max = 255)
    @Column(name = "timesSaved")
    private String timesSaved;

    @Size(max = 255)
    @Column(name = "OrigClaim")
    private String origClaim;

    @Size(max = 255)
    @Column(name = "EDI_FILE")
    private String ediFile;

    @Size(max = 255)
    @Column(name = "isSent")
    private String isSent;

    @Column(name = "SentAt")
    private Instant sentAt;

    @Lob
    @Column(name = "Error")
    private String error;

    @Size(max = 255)
    @Column(name = "EDI_DIR")
    private String ediDir;

    @Column(name = "ErrorAt")
    private Instant errorAt;

    @Size(max = 255)
    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "UpdatedAt")
    private Instant updatedAt;

    @Column(name = "ViewDate")
    private Instant viewDate;

    @Size(max = 255)
    @Column(name = "EDI_GENERATED_BY")
    private String ediGeneratedBy;





    //    @OneToMany(fetch = FetchType.LAZY)
    @OneToMany(targetEntity = Claimchargesinfo.class, cascade = CascadeType.ALL)
//    @JoinColumn(name = "ClaimInfoMasterId",referencedColumnName = "id")
    @JoinColumns({
           @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" ,updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber",updatable = false)
    })
    List<Claimchargesinfo> claimchargesinfo;

    @JsonManagedReference
    @OneToOne(mappedBy = "claiminfomaster", cascade = CascadeType.ALL)
    Claimadditionalinfo claimadditionalinfo;

    @JsonManagedReference
    @OneToOne(mappedBy = "claiminfomaster", cascade = CascadeType.ALL)
    Claimambulancecode claimambulancecode;

    @JsonManagedReference
    @OneToOne(mappedBy = "claiminfomaster", cascade = CascadeType.ALL)
    Claiminformationcode claiminformationcode;


    @OneToMany(targetEntity = Claiminfocodeextcauseinj.class, cascade = CascadeType.ALL)
    @JoinColumns({
           @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" , updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber", updatable = false)
    })
    List<Claiminfocodeextcauseinj> claiminfocodeextcauseinj;
//    @JsonManagedReference
//    @OneToOne(mappedBy = "claiminfomaster",cascade=CascadeType.ALL)
//    Claiminfocodeextcauseinj claiminfocodeextcauseinj;

    @OneToMany(targetEntity = Claiminfocodereasvisit.class, cascade = CascadeType.ALL)
    @JoinColumns({
           @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" , updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber", updatable = false)
    })
    List<Claiminfocodereasvisit> claiminfocodereasvisit;


//    @JsonManagedReference
//    @OneToOne(mappedBy = "claiminfomaster",cascade=CascadeType.ALL)
//    Claiminfocodereasvisit claiminfocodereasvisit;


    @OneToMany(targetEntity = Claiminfocodeothdiag.class, cascade = CascadeType.ALL)
    @JoinColumns({
           @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" , updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber", updatable = false)
    })
    List<Claiminfocodeothdiag> claiminfocodeothdiag;


//    @JsonManagedReference
//    @OneToOne(mappedBy = "claiminfomaster",cascade=CascadeType.ALL)
//    Claiminfocodeothdiag claiminfocodeothdiag;


    @OneToMany(targetEntity = Claiminfocodeothprocedure.class, cascade = CascadeType.ALL)
    @JoinColumns({
           @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" , updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber", updatable = false)
    })
    List<Claiminfocodeothprocedure> claiminfocodeothprocedure;

    @OneToMany(targetEntity = Claiminfocodeoccspan.class, cascade = CascadeType.ALL)
    @JoinColumns({
           @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" , updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber", updatable = false)
    })
    List<Claiminfocodeoccspan> claiminfocodeoccspan;

    @OneToMany(targetEntity = Claiminfooccurance.class, cascade = CascadeType.ALL)
    @JoinColumns({
           @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" , updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber", updatable = false)
    })
    List<Claiminfooccurance> claiminfooccurance;

    @OneToMany(targetEntity = Claiminfocodevaluecode.class, cascade = CascadeType.ALL)
    @JoinColumns({
           @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" , updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber", updatable = false)
    })
    List<Claiminfocodevaluecode> claiminfocodevaluecode;

    @OneToMany(targetEntity = Claiminfocodeconditioncode.class, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "ClaimInfoMasterId", referencedColumnName = "Id" , updatable = false),
            @JoinColumn(name = "ClaimNumber", referencedColumnName = "ClaimNumber", updatable = false)
    })
    List<Claiminfocodeconditioncode> claiminfocodeconditioncode;

    @PrePersist
    public void created() {
        createdDate = new Date().toInstant();
        createdBy = "MOUHID_CREATED";

        if(claimNumber.startsWith("CP")){
            claimType = 2;
        }else{
            claimType = 1;
        }
    }

    @PreUpdate
    public void Updated() {
        updatedAt = new Date().toInstant();

        if(claimNumber.startsWith("CP")){
            claimType = 2;
        }else{
            claimType = 1;
        }

        System.out.println("NewClaimUpdate updatedAt -> " + updatedAt + " updatedBy -> " + updatedBy);
    }

}