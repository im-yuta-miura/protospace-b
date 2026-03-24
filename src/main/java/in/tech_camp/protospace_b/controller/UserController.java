package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_b.entity.AffiliationEntity;
import in.tech_camp.protospace_b.entity.PositionEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.exception.EmailAlreadyExistsException;
import in.tech_camp.protospace_b.form.UserForm;
import in.tech_camp.protospace_b.service.AffiliationService;
import in.tech_camp.protospace_b.service.PositionService;
import in.tech_camp.protospace_b.service.PrototypeService;
import in.tech_camp.protospace_b.service.UserService;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final PrototypeService prototypeService;

  private final AffiliationService affiliationService;
  private final PositionService positionService;

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
    UserForm form = new UserForm();
    form.setAffiliationId(0);
    form.setPositionId(0);
    model.addAttribute("userForm", form);

    List<PositionEntity> positions = positionService.getPositionOptions();
    model.addAttribute("positions", positions);

    List<AffiliationEntity> affiliations = affiliationService.getAffiliationOptions();
    model.addAttribute("affiliations", affiliations);

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

      List<PositionEntity> positions = positionService.getPositionOptions();
      model.addAttribute("positions", positions);

      List<AffiliationEntity> affiliations = affiliationService.getAffiliationOptions();
      model.addAttribute("affiliations", affiliations);

      return "users/signUp";
    }

    UserEntity user = new UserEntity();
    user.setName(userForm.getName());
    user.setProfile(userForm.getProfile());
    user.setAffiliationId(userForm.getAffiliationId());
    user.setPositionId(userForm.getPositionId());
    user.setEmail(userForm.getEmail());
    user.setPassword(userForm.getPassword());

    try {
      userService.registerUser(user);
    } catch (EmailAlreadyExistsException e) {
      List<String> emailError = List.of("メールアドレスは登録済みです。");
      model.addAttribute("errorMessages", emailError);
      model.addAttribute("userForm", userForm);

      List<PositionEntity> positions = positionService.getPositionOptions();
      model.addAttribute("positions", positions);

      List<AffiliationEntity> affiliations = affiliationService.getAffiliationOptions();
      model.addAttribute("affiliations", affiliations);

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

    model.addAttribute("affiliation", affiliationService.getAffiliation(user.getAffiliationId()));
    model.addAttribute("position", positionService.getPosition(user.getPositionId()));

    List<PrototypeEntity> prototypes = prototypeService.getPrototypeByUserId(id);

    model.addAttribute("prototypes", prototypes);

    return "users/detail";
  }
}
