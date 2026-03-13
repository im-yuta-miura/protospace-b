package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  
  @Select("SELECT * FROM prototypes")
    List<PrototypeEntity> findAll();

  @Insert("INSERT INTO prototypes (title, catchphrase, concept, image, user_id) VALUES (#{title}, #{catchphrase}, #{concept}, #{image}, #{user_id})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(PrototypeEntity prototype);
  @Select("SELECT * FROM prototypes WHERE id = #{id}")
  PrototypeEntity findById(Integer id);
}
