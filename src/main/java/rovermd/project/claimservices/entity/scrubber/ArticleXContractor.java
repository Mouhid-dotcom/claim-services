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
@Table(name = "article_x_contractor")
public class ArticleXContractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Size(max = 10)
    @Column(name = "article_version", length = 10)
    private String articleVersion;

    @Column(name = "article_type")
    private Integer articleType;

    @Column(name = "contract_id")
    private Integer contractId;

    @Column(name = "contractor_type_id")
    private Integer contractorTypeId;

    @Column(name = "contractor_version")
    private Integer contractorVersion;

    @Column(name = "last_updated")
    private Instant lastUpdated;

}