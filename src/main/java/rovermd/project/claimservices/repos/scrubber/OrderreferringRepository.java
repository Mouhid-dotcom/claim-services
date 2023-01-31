package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Orderreferring;

public interface OrderreferringRepository extends JpaRepository<Orderreferring, Integer> {


    @Query("SELECT Count(*) from Orderreferring where lastName =:lastname AND npi=:npi")
    Integer validateOrderingOrRefferingProvider(@Param("lastname") String lastname, @Param("npi") String npi);
}