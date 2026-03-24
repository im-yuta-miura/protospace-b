package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface UserRepository {
  
  @Select("SELECT * FROM users")
  List<UserEntity> findAll();

  @Insert("INSERT INTO users ("
    + "  name, profile, affiliation_id, position_id, email, password"
    + ") VALUES ("
    + "  #{name}, #{profile}, #{affiliationId}, #{positionId}, #{email}, #{password}"
    + ")")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(UserEntity user);

  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
  boolean existsByEmail(String email);

  @Select("SELECT * FROM users WHERE email = #{email}")
  UserEntity findByEmail(String email);

  @Select("SELECT * FROM users WHERE id = #{id} LIMIT 1")
  UserEntity findById(Integer id);
}
