package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class PrototypeController {

  private final PrototypeRepository prototypeRepository;

  
  @GetMapping("/prototypes/{prototypeId}")
  public String showTweetDetail(@PathVariable("prototypeId") Integer prototypeId, Model model) {
      PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
      model.addAttribute("prototype", prototype);
      return "prototypes/detail";
  }
  
}