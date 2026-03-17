package in.tech_camp.protospace_b.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_b.ImageUrl;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.form.PrototypeForm;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import in.tech_camp.protospace_b.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;
  private final UserRepository userRepository;

  private final ImageUrl imageUrl;
  @GetMapping("/prototypes/new")
    public String showForm(Model model) {
        
        model.addAttribute("prototypeForm", new PrototypeForm());
        return "form";
    }
  
  

  @PostMapping("/prototypes")
  public String createPrototype(@ModelAttribute("prototypeForm")  @Validated PrototypeForm prototypeForm, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetail currentUser) {

    if(bindingResult.hasErrors() || prototypeForm.getImage().isEmpty()) {

      return "form";
    }

    PrototypeEntity prototype = new PrototypeEntity();

    prototype.setTitle(prototypeForm.getTitle());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());
    prototype.setConcept(prototypeForm.getConcept());
    
    prototype.setUser_id(currentUser.getId());


    MultipartFile imageFile = prototypeForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()){
      try{
        String uploadDir = imageUrl.getImageUrl();
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        prototype.setImage("/uploads/" + fileName);

      } catch (IOException e) {
        System.out.println("画像保存エラー:" + e);
        return "form";
      }
    }

  
    try {
      prototypeRepository.insert(prototype);
    } catch(Exception e) {
      System.out.println("エラー: " + e);
      return "form";
    }

    return "redirect:/";
  }
  
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
