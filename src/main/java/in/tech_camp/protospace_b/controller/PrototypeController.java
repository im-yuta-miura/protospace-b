package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.PrototypeForm;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;

  @GetMapping("/prototypes/new")
    public String showForm(Model model) {
        
        model.addAttribute("prototypeForm", new PrototypeForm());
        return "form";
    }
  
  

  @PostMapping("/prototypes")
  public String createPrototype(@ModelAttribute("prototypeForm") PrototypeForm prototypeForm) {

    PrototypeEntity prototype = new PrototypeEntity();

    prototype.setTitle(prototypeForm.getTitle());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setImage(prototypeForm.getImage());
  
    try {
      prototypeRepository.insert(prototype);
    } catch(Exception e) {
      System.out.println("エラー: " + e);
      return "redirect:/";
    }

    return "redirect:/";
  }
}
