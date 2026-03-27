package in.tech_camp.protospace_b.form;


import in.tech_camp.protospace_b.validation.ValidationPriority1;
import in.tech_camp.protospace_b.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserEditForm {

  /** 名前 */
  @NotBlank(message = "ユーザー名は必須です", groups = ValidationPriority1.class)
  private String name;

  /** プロフィール */
  @NotBlank(message = "プロフィールは必須です", groups = ValidationPriority1.class)
  private String profile;

  /** 所属 */
  @NotBlank(message = "所属は必須です", groups = ValidationPriority1.class)
  private Integer affiliationId;

  /** 役職 */
  @NotBlank(message = "役職は必須です", groups = ValidationPriority1.class)
  private Integer positionId;

  /** メール */
  @NotBlank(message = "メールアドレスは必須です", groups = ValidationPriority1.class)
  @Email(message = "正しいメールアドレスを入力してください", groups = ValidationPriority2.class)
  private String email;
}