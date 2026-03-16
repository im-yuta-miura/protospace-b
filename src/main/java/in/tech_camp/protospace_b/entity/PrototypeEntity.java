package in.tech_camp.protospace_b.entity;

import lombok.Data;

@Data
public class PrototypeEntity {
  private Integer id;
  private String title;
  private String catchphrase;
  private String concept;
  private String image;
  private Integer user_id;
  private UserEntity user;
}