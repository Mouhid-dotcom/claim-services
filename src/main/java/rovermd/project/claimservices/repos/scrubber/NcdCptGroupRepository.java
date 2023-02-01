package rovermd.project.claimservices.repos.scrubber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rovermd.project.claimservices.entity.scrubber.NcdCptGroup;

import java.util.List;

public interface NcdCptGroupRepository extends JpaRepository<NcdCptGroup, Integer> {

    @Query("SELECT Count(*) FROM NcdCptGroup WHERE cpt=:code")
    Integer validateNcdCptGroupCPT(@Param("code") String code);
    @Query("SELECT " +
            " Count(*) " +
            " FROM" +
            "  NcdCptGroup a " +
            " INNER JOIN Ncdxcptxicd b ON a.grpN = b.grpN " +
            " WHERE  " +
            " a.cpt = :cpt " +
            " AND b.icd = :icd " +
            " AND b.resCode = :resCode " +
            " AND b.icd NOT LIKE '%-%' ")
    Integer validateNcdCptGroupXNcdxcptxicd_Denial(@Param("cpt") String cpt, @Param("icd") String icd, @Param("resCode") int resCode);

    @Query("SELECT " +
            " Count(*) " +
            " FROM" +
            "  NcdCptGroup a " +
            " INNER JOIN Ncdxcptxicd b ON a.grpN = b.grpN " +
            " WHERE  " +
            " a.cpt = :cpt " +
            " AND b.icd = :icd " +
            " AND b.resCode = :resCode ")
    Integer validateNcdCptGroupXNcdxcptxicd_Med(@Param("cpt") String cpt, @Param("icd") String icd, @Param("resCode") int resCode);

    @Query("SELECT " +
            " b.icd " +
            " FROM" +
            "  NcdCptGroup a " +
            " INNER JOIN Ncdxcptxicd b ON a.grpN = b.grpN " +
            " WHERE  " +
            " a.cpt = :cpt " +
            " AND b.resCode = :resCode " +
            " AND b.icd NOT LIKE '%-%' ")
    List<String> getNcdCptGroupXNcdxcptxicd(@Param("cpt") String cpt, @Param("resCode") int resCode);
}