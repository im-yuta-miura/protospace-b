package in.tech_camp.protospace_b.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_b.validation.ValidationPriority1;
import in.tech_camp.protospace_b.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForm {

  /** 名前 */
  @NotBlank(message = "ユーザー名は必須です", groups = ValidationPriority1.class)
  private String name;

  /** プロフィール */
  @NotBlank(message = "プロフィールは必須です", groups = ValidationPriority1.class)
  private String profile;

  /** 所属 */
  @Min(value=1, message = "所属は必須です", groups = ValidationPriority1.class)
  private Integer affiliationId;

  /** 役職 */
  @Min(value=1, message = "役職は必須です", groups = ValidationPriority1.class)
  private Integer positionId;

  /** メール */
  @NotBlank(message = "メールアドレスは必須です", groups = ValidationPriority1.class)
  @Email(message = "正しいメールアドレスを入力してください", groups = ValidationPriority2.class)
  private String email;

  /** パスワード */
  @NotBlank(message = "パスワードは必須です", groups = ValidationPriority1.class)
  @Length(min = 6, max = 128, message = "パスワードは6文字以上で入力してください", groups = ValidationPriority2.class)
  private String password;
  
  /** パスワード再入力 */
  private String passwordConfirmation;

  public void validatePasswordConfirmation(BindingResult result) {
    if (!password.equals(passwordConfirmation)) {
      result.rejectValue("passwordConfirmation", "error.user", "パスワードが一致しません");
    }
  }
}