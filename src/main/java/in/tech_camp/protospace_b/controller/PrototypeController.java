package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import in.tech_camp.protospace_b.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;
  private final UserRepository userRepository;

  @GetMapping("/")
  public String showPrototype(@AuthenticationPrincipal UserEntity user, Model model) {
    if (user != null) {
      model.addAttribute("name", user.getName());
    }
    
    List<PrototypeEntity> prototypes = prototypeRepository.findAll();
    model.addAttribute("prototypes",prototypes);
    return "prototypes/index";
  }

  @GetMapping("/prototypes/{prototypeId}")
  public String showPrototypeDetail(@PathVariable("prototypeId") Integer prototypeId, Model model) {

    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    CommentForm commentForm = new CommentForm();

    model.addAttribute("prototype", prototype);
    model.addAttribute("commentForm", commentForm);
    model.addAttribute("comments", prototype.getComments());
    
    return "prototypes/detail";
  }
}
