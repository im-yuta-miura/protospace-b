package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PositionEntity;

@Mapper
public interface  PositionRepository {

  @Insert("INSERT INTO positions ("
    + "  position"
    + ") VALUES ("
    + "  #{position}"
    + ")")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(PositionEntity position);

  @Select("SELECT * FROM positions WHERE id = #{id}")
  PositionEntity findById(Integer id);

  @Select("SELECT * FROM positions")
  List<PositionEntity> findAll();
  
  @Select("SELECT EXISTS(SELECT 1 FROM positions WHERE position = #{position})")
  boolean existsByPosition(String position);
}
