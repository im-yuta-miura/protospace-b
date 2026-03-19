package in.tech_camp.protospace_b.controller;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.exception.EmailAlreadyExistsException;
import in.tech_camp.protospace_b.form.UserForm;
import in.tech_camp.protospace_b.service.PrototypeService;
import in.tech_camp.protospace_b.service.UserService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserControllerUnitTest {
  
  @Mock
  private UserService userService;

  @Mock
  private PrototypeService prototypeService;

  @InjectMocks
  private UserController userController;


  @Mock
  private BindingResult bindingResult;
  
  private Model model;

  @BeforeEach
  public void setUp() {
    model = new ExtendedModelMap();
  }

  @Nested
  public class ログイン機能 {
    @Test
    public void loginにアクセスするとログインページが開けること() {
      
      String result = userController.showLogin();
      assertThat(result, is("users/login"));
    }
    
    @Test
    public void GETパラメータerrorがある状態でログインするとエラーメッセージが表示されること() {

      String result = userController.showLoginWithError("", model);
      
      assertThat(result, is("users/login"));
      assertThat(
          model.getAttribute("loginError"),
          is("Invalid email or password."));

    }
  }

  @Nested
  public class ユーザー登録機能 {

    @Test
    public void サインアップページが表示できること() {
      
      String result = userController.signUp(model);
      assertThat(result, is("users/signUp"));

      assertThat(
        model.getAttribute("userForm"),
        equalTo(new UserForm()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ユーザー登録時にEmail重複でサインアップ画面に戻ること() throws Exception {

      when(bindingResult.hasErrors()).thenReturn(false);

      when(userService.registerUser(any())).thenThrow(new EmailAlreadyExistsException("email重複"));

      var form = new UserForm();
      form.setEmail("test@test.com");
      form.setPassword("p@ssw0rd");
      form.setPasswordConfirmation("p@ssw0rd");

      String result = userController.createUser(form, bindingResult, model);

      assertThat(result, is("users/signUp"));
      
      var errorMessages = (List<String>)model.getAttribute("errorMessages");

      assertThat(
        errorMessages,
        contains("メールアドレスは登録済みです。"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ユーザー登録ができること() throws Exception {

      when(bindingResult.hasErrors()).thenReturn(false);

      when(userService.registerUser(any())).thenReturn(new UserEntity());
      
      var form = new UserForm();
      form.setEmail("test@test.com");
      form.setPassword("p@ssw0rd");
      form.setPasswordConfirmation("p@ssw0rd");
      form.setName("testName");
      form.setProfile("testProfile");
      form.setAffiliation("testAffiliation");
      form.setPosition("testPosition");

      String result = userController.createUser(form, bindingResult, model);

      assertThat(result, is("redirect:/"));
      
      var errorMessages = (List<String>)model.getAttribute("errorMessages");

      assertThat(errorMessages,nullValue());
    }
  }
  
  @Nested
  public class ユーザー詳細機能 {
@SuppressWarnings("unchecked")
    @Test
    public void ユーザー詳細が取得できること() throws Exception {

      var user = new UserEntity();
      user.setId(1);
      user.setName("userName");
      user.setPassword("testPassword");
      user.setEmail("test@example.com");
      user.setProfile("testProfile");
      user.setAffiliation("testAffiliation");
      user.setPosition("testPosition");
      when(userService.findUserById(1)).thenReturn(user);

      var proto1 = new PrototypeEntity();
      proto1.setId(1);
      proto1.setTitle("testTitle1");
      proto1.setImage(null);
      proto1.setCatchphrase("testCatchphrase1");
      proto1.setConcept("testConcept1");
      proto1.setComments(List.of());

      var proto2 = new PrototypeEntity();
      proto2.setId(2);
      proto2.setTitle("testTitle2");
      proto2.setImage(null);
      proto2.setCatchphrase("testCatchphrase2");
      proto2.setConcept("testConcept2");
      proto2.setComments(List.of());

      var prototypes = List.of(proto1, proto2);

      when(prototypeService.getPrototypeByUserId(1)).thenReturn(prototypes);

      String result = userController.showUserDetail(1, model);

      assertThat(result, is("users/detail"));
      
      assertThat(model.getAttribute("user"),is(user));
      assertThat(model.getAttribute("prototypes"),is(prototypes));
      
    }
  }
}
