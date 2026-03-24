package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.exception.EmailAlreadyExistsException;
import in.tech_camp.protospace_b.form.SearchForm;
import in.tech_camp.protospace_b.form.UserEditForm;
import in.tech_camp.protospace_b.form.UserForm;
import in.tech_camp.protospace_b.repository.UserRepository;
import in.tech_camp.protospace_b.service.PrototypeService;
import in.tech_camp.protospace_b.service.UserService;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final PrototypeService prototypeService;
  private final UserRepository userRepository;

  @ModelAttribute("searchForm")
  public SearchForm setUpSearchForm() {
      return new SearchForm();
  }

  @GetMapping("/users/login")
  public String showLogin(){
      return "users/login";
  }

  @GetMapping("/login")
  public String showLoginWithError(@RequestParam(value = "error") String error, Model model) {
    if (error != null) {
      model.addAttribute("loginError", "Invalid email or password.");
    }
    return "users/login";
  }

  @GetMapping("/users/sign_up")
  public String signUp(
      Model model
  ) {
    model.addAttribute("userForm", new UserForm());
    return "users/signUp";
  }

  @PostMapping("/user")
  public String createUser(
      @ModelAttribute("userForm") @Validated(ValidationOrder.class) UserForm userForm,
      BindingResult result,
      Model model
  ) {
    
    userForm.validatePasswordConfirmation(result);

    if(result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .toList();

      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("userForm", userForm);
      return "users/signUp";
    }

    UserEntity user = new UserEntity();
    user.setName(userForm.getName());
    user.setProfile(userForm.getProfile());
    user.setAffiliation(userForm.getAffiliation());
    user.setPosition(userForm.getPosition());
    user.setEmail(userForm.getEmail());
    user.setPassword(userForm.getPassword());

    try {
      userService.registerUser(user);
    } catch (EmailAlreadyExistsException e) {
      List<String> emailError = List.of("メールアドレスは登録済みです。");
      model.addAttribute("errorMessages", emailError);
      model.addAttribute("userForm", userForm);
      return "users/signUp";
    }

    return "redirect:/";
  }

  @GetMapping("/users/{user_id}/mypage")
  public String showUserDetail(
      @PathVariable("user_id") Integer id,
      Model model
  ) {

    UserEntity user = userService.findUserById(id);
    model.addAttribute("user", user);

    List<PrototypeEntity> prototypes = prototypeService.getPrototypeByUserId(id);

    model.addAttribute("prototypes", prototypes);

    return "users/detail";
  }

  @GetMapping("users/{user_id}/edit")
  public String editUser(@PathVariable("user_id") Integer userId, Authentication authentication, Model model){
    UserEntity user = userRepository.findById(userId);

    if (user == null) {
        return "redirect:/";
    }

    CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

    if (!user.getId().equals(userDetail.getId())) {
        return "redirect:/";
    }

    UserEditForm userForm = new UserEditForm();

    userForm.setName(user.getName());
    userForm.setProfile(user.getProfile());
    userForm.setAffiliation(user.getAffiliation());
    userForm.setPosition(user.getPosition());
    userForm.setEmail(user.getEmail());

    model.addAttribute("userForm", userForm);
    model.addAttribute("userId", userId);

    return "users/edit";
  }

  @PostMapping("/user/{user_id}/update")
  public String updateUser(
      @ModelAttribute("userForm") @Validated(ValidationOrder.class) UserEditForm userForm,
      BindingResult result,
      @PathVariable("user_id") Integer userId,
      Model model
  ) {
    
    if(result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .toList();

      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("userId", userId);
      return "users/edit";
    }

    UserEntity user = userRepository.findById(userId);

    if (user == null) {
      return "redirect:/";
    }

    user.setName(userForm.getName());
    user.setProfile(userForm.getProfile());
    user.setAffiliation(userForm.getAffiliation());
    user.setPosition(userForm.getPosition());
    user.setEmail(userForm.getEmail());

    try {
      userRepository.update(user);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }

    return "redirect:/users/" + userId + "/mypage";
  }
}
