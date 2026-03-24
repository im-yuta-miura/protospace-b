package in.tech_camp.protospace_b.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.exception.EmailAlreadyExistsException;
import in.tech_camp.protospace_b.factory.UserFormFactory;
import in.tech_camp.protospace_b.repository.UserRepository;
import in.tech_camp.protospace_b.service.UserService;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import in.tech_camp.protospace_b.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserFormUnitTest {
  
  private UserForm userForm;
  private Validator validator;
  private BindingResult bindingResult;
  
  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository mockUserRepository;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    userForm = UserFormFactory.createUser();
    bindingResult = Mockito.mock(BindingResult.class);
  }
  
  @Nested
  class ユーザー作成ができる場合 {
    @Test
    public void nicknameとemailとpasswordとpasswordConfirmationが存在すれば登録できる () {
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
      assertEquals(0, violations.size());
    }
  }

  @Nested
  class ユーザー作成ができない場合 {

    @Test
    public void nameが空の場合バリデーションエラーが発生する() {
      userForm.setName("");
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
      assertEquals("ユーザー名は必須です", violations.iterator().next().getMessage());
    }

    @Test
    public void profileが空の場合バリデーションエラーが発生する() {
      userForm.setProfile("");
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
      assertEquals("プロフィールは必須です", violations.iterator().next().getMessage());
    }

    @Test
    public void 所属が空の場合バリデーションエラーが発生する() {
      userForm.setAffiliationId(null);
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
      assertEquals("所属は必須です", violations.iterator().next().getMessage());
    }
    
    @Test
    public void 役職が空の場合バリデーションエラーが発生する() {
      userForm.setPositionId(null);
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
      assertEquals("役職は必須です", violations.iterator().next().getMessage());
    }
    
    @Test
    public void emailが空の場合バリデーションエラーが発生する() {
      userForm.setEmail("");
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
      assertEquals("メールアドレスは必須です", violations.iterator().next().getMessage());
    }

    @Test
    public void emailが不正な場合バリデーションエラーが発生する() {
      userForm.setEmail(userForm.getEmail().replace("@", ""));
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
      assertEquals("正しいメールアドレスを入力してください", violations.iterator().next().getMessage());
    }
    
    @Test
    public void emailが登録済みの場合エラーが発生する() {
      when(mockUserRepository.existsByEmail(anyString()))
          .thenReturn(true);
      
      UserEntity user = new UserEntity();
      user.setEmail(userForm.getEmail());
      
      assertThrows(
        EmailAlreadyExistsException.class,
        () -> userService.registerUser(user)
      ); 
    }


    @Test
    public void passwordが空の場合バリデーションエラーが発生する() {
      userForm.setPassword("");
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
      assertEquals("パスワードは必須です", violations.iterator().next().getMessage());
    }
    
    @Test
    public void passwordが5文字の場合バリデーションエラーが発生する() {
      userForm.setPassword("a".repeat(5));
      Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
      assertEquals("パスワードは6文字以上で入力してください", violations.iterator().next().getMessage());
    }

    @Test
    public void passwordとpasswordConfirmationが不一致ではバリデーションエラーが発生する() {
      userForm.setPasswordConfirmation("differentPassword");
      userForm.validatePasswordConfirmation(bindingResult);
      verify(bindingResult).rejectValue("passwordConfirmation", "error.user", "パスワードが一致しません");
    }



  }

}
