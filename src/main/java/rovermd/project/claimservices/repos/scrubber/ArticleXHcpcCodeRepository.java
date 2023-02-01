package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.ArticleXHcpcCode;

public interface ArticleXHcpcCodeRepository extends JpaRepository<ArticleXHcpcCode, Long> {

    @Query("SELECT Count(*) FROM ArticleXHcpcCode WHERE hcpcCodeId=:code")
    Integer validateArticleXHcpcCodeCPT(@Param("code") String code);

    @Query("SELECT Count(*) FROM ArticleXContractor a " +
            "INNER JOIN ArticleXHcpcCode b ON a.articleId = b.articleId " +
            "INNER JOIN ArticleXIcd10Noncovered c ON a.articleId = c.articleId " +
            "WHERE " +
            "(" +
            "   a.contractId = 334 " +
            "   OR a.contractId = 338 " +
            ") " +
            "AND c.icd10CodeId = :icd " +
            "AND b.hcpcCodeId = :cpt ")
    Integer validateArticleXHcpcCodeCPT_X_ArticleXHcpcCode_X_ArticleXIcd10Noncovered(@Param("cpt") String cpt, @Param("icd") String icd);
}