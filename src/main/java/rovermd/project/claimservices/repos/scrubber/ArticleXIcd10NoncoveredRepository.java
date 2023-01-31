package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import rovermd.project.claimservices.entity.scrubber.ArticleXIcd10Noncovered;

public interface ArticleXIcd10NoncoveredRepository extends JpaRepository<ArticleXIcd10Noncovered, Long> {
}