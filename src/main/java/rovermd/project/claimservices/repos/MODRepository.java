package rovermd.project.claimservices.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.MOD;

import java.util.List;

public interface MODRepository extends JpaRepository<MOD,Long> {

    @Query("SELECT c FROM MOD c " +
            " WHERE CONCAT(IFNULL(c.code,''),' ',IFNULL(c.description,''))" +
            " LIKE %:txt% AND  c.status=1")
    List<MOD> searchByLike(@Param("txt") String txt);

    MOD findMODByCode(String code);

}
