package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;

  @GetMapping("/")
  public String showPrototype(Model model) {
    String userName = "新井";
    List<PrototypeEntity> prototypes = prototypeRepository.findAll();
    model.addAttribute("userName", userName);
    model.addAttribute("prototypes",prototypes);
    return "prototypes/index";
  }
}
