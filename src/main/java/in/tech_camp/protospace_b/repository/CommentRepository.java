package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_b.entity.CommentEntity;


@Mapper
public interface  CommentRepository {
  @Select("SELECT c.*, u.id AS user_id, u.name AS user_name FROM comments c JOIN users u ON c.user_id = u.id WHERE c.prototype_id = #{prototypeId}")
    @Results(value = {
        @Result(property = "user.id", column = "user_id"),
        @Result(property = "user.name", column = "user_name"),
        @Result(property = "prototype", column = "prototype_id", 
                one = @One(select = "in.tech_camp.protospace_b.repository.PrototypeRepository.findById"))
    })
    List<CommentEntity> findByPrototypeId(Integer prototypeId);

  @Insert("INSERT INTO comments (content, user_id, prototype_id) VALUES (#{content}, #{user.id}, #{prototype.id})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(CommentEntity comment);

  @Update("UPDATE comments SET content = #{content} WHERE id = #{id}")
  void update(CommentEntity prototype);

  @Delete("DELETE FROM comments WHERE id = #{id}")
  void deleteById(Integer id);


  @Select("SELECT * FROM comments WHERE id = #{id}")
    @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "in.tech_camp.protospace_b.repository.UserRepository.findById")),
        @Result(property = "prototype", column = "prototype_id",
                one = @One(select = "in.tech_camp.protospace_b.repository.PrototypeRepository.findById"))
    })

  CommentEntity findById(Integer id);


}
