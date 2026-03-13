package in.tech_camp.protospace_b.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_b.ImageUrl;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.PrototypeForm;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;

  private final ImageUrl imageUrl;
  @GetMapping("/prototypes/new")
    public String showForm(Model model) {
        
        model.addAttribute("prototypeForm", new PrototypeForm());
        return "form";
    }
  
  

  @PostMapping("/prototypes")
  public String createPrototype(@ModelAttribute("prototypeForm") PrototypeForm prototypeForm, @AuthenticationPrincipal CustomUserDetail currentUser) {

    PrototypeEntity prototype = new PrototypeEntity();

    prototype.setTitle(prototypeForm.getTitle());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());
    prototype.setConcept(prototypeForm.getConcept());
    
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
        return "redirect:/prototypes/new";
      }
    }

    if (currentUser != null) {
      prototype.setUser_id(currentUser.getId());
    }

  
    try {
      prototypeRepository.insert(prototype);
    } catch(Exception e) {
      System.out.println("エラー: " + e);
      return "redirect:/";
    }

    return "redirect:/";
  }
}
