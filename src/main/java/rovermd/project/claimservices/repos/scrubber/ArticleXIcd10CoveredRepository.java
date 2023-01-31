package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.scrubber.ArticleXIcd10Covered;

public interface ArticleXIcd10CoveredRepository extends JpaRepository<ArticleXIcd10Covered, Long> {
}