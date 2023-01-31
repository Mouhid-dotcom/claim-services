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
@Table(name = "article_x_hcpc_code")
public class ArticleXHcpcCode {
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
    @Column(name = "hcpc_code_id")
    private String hcpcCodeId;

    @Size(max = 255)
    @Column(name = "hcpc_code_version")
    private String hcpcCodeVersion;

    @Size(max = 255)
    @Column(name = "hcpc_code_group")
    private String hcpcCodeGroup;

    @Size(max = 255)
    @Column(name = "long_description")
    private String longDescription;

    @Size(max = 255)
    @Column(name = "short_description")
    private String shortDescription;

    @Size(max = 255)
    @Column(name = "`range`")
    private String range;

    @Size(max = 255)
    @Column(name = "last_updated")
    private String lastUpdated;

}