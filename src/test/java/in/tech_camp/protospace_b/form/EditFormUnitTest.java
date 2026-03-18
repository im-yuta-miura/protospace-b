package in.tech_camp.protospace_b.form;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.protospace_b.factory.PrototypeFormFactory;
import in.tech_camp.protospace_b.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
public class EditFormUnitTest {
  private PrototypeForm editForm;
  private Validator validator;

  @BeforeEach
    public void setUp() {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      validator = factory.getValidator();
      editForm = PrototypeFormFactory.createPrototypeForm();
    }

  @Test
  public void Titleが空の場合バリデーションエラーが発生する(){
    editForm.setTitle("");
    Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(editForm, ValidationPriority1.class);
    assertEquals(1, violations.size());
    assertEquals("title can't be blank", violations.iterator().next().getMessage());
  }

  @Test
  public void キャッチフレーズが空の場合バリデーションエラーが発生する(){
    editForm.setCatchphrase("");
    Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(editForm, ValidationPriority1.class);
    assertEquals(1, violations.size());
    assertEquals("catchphrase can't be blank", violations.iterator().next().getMessage());
  }

  @Test
  public void コンセプトが空の場合バリデーションエラーが発生する(){
    editForm.setConcept("");
    Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(editForm, ValidationPriority1.class);
    assertEquals(1, violations.size());
    assertEquals("concept can't be blank", violations.iterator().next().getMessage());
  }

  @Test
  public void 編集成功(){
    editForm.setTitle("編集後のタイトル");
    editForm.setCatchphrase("編集後のフレーズ");
    editForm.setConcept("編集後のコンセプト");
    Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(editForm, ValidationPriority1.class);
    assertEquals(0, violations.size());
  }
}