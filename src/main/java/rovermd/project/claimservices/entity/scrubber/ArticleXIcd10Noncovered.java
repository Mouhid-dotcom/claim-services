package rovermd.project.claimservices.entity.scrubber;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "article_x_icd10_noncovered")
public class ArticleXIcd10Noncovered {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "article_id")
    private String articleId;

    @Size(max = 255)
    @Column(name = "article_version")
    private String articleVersion;

    @Size(max = 255)
    @Column(name = "icd10_code_id")
    private String icd10CodeId;

    @Size(max = 255)
    @Column(name = "icd10_code_version")
    private String icd10CodeVersion;

    @Size(max = 255)
    @Column(name = "icd10_noncovered_group")
    private String icd10NoncoveredGroup;

    @Size(max = 255)
    @Column(name = "`range`")
    private String range;

    @Size(max = 255)
    @Column(name = "sort_order")
    private String sortOrder;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "last_updated")
    private String lastUpdated;

}