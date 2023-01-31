package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.Addoncode;

public interface AddoncodeRepository extends JpaRepository<Addoncode, Integer> {

    @Query("SELECT Count(*) FROM Addoncode WHERE addOnCode=:code")
    Integer validateAddOnCodes(@Param("code") String code);

    @Query("SELECT Count(*) FROM Addoncode WHERE primaryCodes=:primaryCode AND addOnCode=:code")
    Integer validateAddOnCodesAndPrimaryCodes(@Param("primaryCode") String primaryCode,@Param("code") String code);


}