package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Wpctaxonomy;

public interface WpctaxonomyRepository extends JpaRepository<Wpctaxonomy, Integer> {

    @Query("SELECT Count(*) FROM Wpctaxonomy WHERE codes=:taxonomy")
    Integer validateTaxonomy(@Param("taxonomy") String taxonomy);
}