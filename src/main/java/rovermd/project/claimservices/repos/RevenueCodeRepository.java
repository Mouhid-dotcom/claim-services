package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.RevenueCode;

import java.util.List;

public interface RevenueCodeRepository extends JpaRepository<RevenueCode,Long> {

    @Query("SELECT c FROM RevenueCode c " +
            " WHERE CONCAT(IFNULL(c.revCode,''),' ',IFNULL(c.RevDescription,''))" +
            " LIKE %:txt% ")
    List<RevenueCode> searchByLike(@Param("txt") String txt);

    List<RevenueCode> findRevenueCodeByRevCode(String code);

    @Query("SELECT Count(*) FROM RevenueCode where  revCode=:revCode")
    Integer validateRevCode(@Param("revCode") String revCode);

}
