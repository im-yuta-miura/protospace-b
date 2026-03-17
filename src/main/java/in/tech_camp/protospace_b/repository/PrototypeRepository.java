package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  
  @Select("SELECT * FROM prototypes")
  @Results(value = {
    @Result(property = "user", column = "user_id",
            one = @One(select = "in.tech_camp.protospace_b.repository.UserRepository.findById"))
  })
  List<PrototypeEntity> findAll();

  @Insert("INSERT INTO prototypes (title, catchphrase, concept, image, user_id) VALUES (#{title}, #{catchphrase}, #{concept}, #{image}, #{user_id})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(PrototypeEntity prototype);
  @Select("SELECT * FROM prototypes WHERE id = #{id}")
  @Results(value = {
    @Result(property = "id", column = "id"),
    @Result(property = "user", column = "user_id",
            one = @One(select = "in.tech_camp.protospace_b.repository.UserRepository.findById")),
    @Result(property = "comments", column = "id", 
            many = @Many(select = "in.tech_camp.protospace_b.repository.CommentRepository.findByPrototypeId"))
  })
  PrototypeEntity findById(Integer id);

  @Delete("DELETE FROM prototypes WHERE id = #{id}")
  void deleteById(Integer id);

}
