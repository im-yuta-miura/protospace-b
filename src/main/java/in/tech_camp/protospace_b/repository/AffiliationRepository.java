package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.AffiliationEntity;

@Mapper
public interface  AffiliationRepository {

  @Insert("INSERT INTO affiliations ("
    + "  affiliation"
    + ") VALUES ("
    + "  #{affiliation}"
    + ")")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(AffiliationEntity affiliation);

  @Select("SELECT * FROM affiliations WHERE id = #{id}")
  AffiliationEntity findById(Integer id);

  @Select("SELECT * FROM affiliations")
  List<AffiliationEntity> findAll();

  @Select("SELECT EXISTS(SELECT 1 FROM affiliations WHERE affiliation = #{affiliation})")
  boolean existsByAffiliation(String affiliation);
}
