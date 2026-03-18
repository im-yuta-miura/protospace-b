package in.tech_camp.protospace_b.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.Authentication;
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

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.form.PrototypeForm;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import in.tech_camp.protospace_b.validation.ValidationPriority1;
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
    public String createPrototype(@ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm, 
                                BindingResult bindingResult, 
                                @AuthenticationPrincipal CustomUserDetail currentUser) {

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
            try {
                // 1. 保存先のパスを作成（アプリ実行フォルダ直下の uploaded-images）
                String uploadDir = System.getProperty("user.dir") + "/uploaded-images";
                Path uploadPath = Paths.get(uploadDir);

                // フォルダが存在しなかったら作成する
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 2. ファイル名を生成（日付_元の名前）
                String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + imageFile.getOriginalFilename();
                
                // 3. フォルダの中にファイルを保存
                Path imagePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), imagePath);

                // 4. DBに保存するパスをセット（Webから見えるURLパス）
                prototype.setImage("/uploads/" + fileName);

            } catch (IOException e) {
                System.out.println("画像保存エラー:" + e);
                e.printStackTrace(); 
                return "form";
            }
        }

        try {
            prototypeRepository.insert(prototype);
        } catch(Exception e) {
            System.out.println("データベース保存エラー: " + e);
            return "form";
        }

        return "redirect:/";
    }
  
  @GetMapping("/")
  public String showPrototype(@AuthenticationPrincipal CustomUserDetail user, Model model) {
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

  @PostMapping("/prototypes/{prototypeId}/delete")
  public String deletePrototype(@PathVariable("prototypeId") Integer prototypeId,
                                @AuthenticationPrincipal CustomUserDetail currentUser ) {
    
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

    if (prototype == null || !prototype.getUser_id().equals(currentUser.getId())) {
      return "redirect:/prototypes/" + prototypeId;
    }

    try {
      prototypeRepository.deleteById(prototypeId);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }
    return "redirect:/";
  }
  
  @GetMapping("/prototypes/{prototypeId}/edit")
  public String editPrototype(@PathVariable("prototypeId") Integer prototypeId, Authentication authentication, Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

    if (prototype == null) {
        return "redirect:/";
    }

    CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

    if (!prototype.getUser().getId().equals(userDetail.getId())) {
        return "redirect:/";
    }

    PrototypeForm prototypeForm = new PrototypeForm();
    prototypeForm.setTitle(prototype.getTitle());
    prototypeForm.setCatchphrase(prototype.getCatchphrase());
    prototypeForm.setConcept(prototype.getConcept());

    model.addAttribute("prototypeForm", prototypeForm);
    model.addAttribute("prototypeId", prototypeId);

    return "prototypes/edit";
  }

  @PostMapping("/prototypes/{prototypeId}/update")
  public String updatePrototype(@ModelAttribute("prototypeForm") @Validated(ValidationPriority1.class) PrototypeForm prototypeForm,
                            BindingResult result,
                            @PathVariable("prototypeId") Integer prototypeId,
                            Model model) {

    if (result.hasErrors()) {
      model.addAttribute("prototypeId", prototypeId);
      return "prototypes/edit";
    }

    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    prototype.setTitle(prototypeForm.getTitle());
    prototype.setCatchphrase(prototypeForm.getCatchphrase());
    prototype.setConcept(prototypeForm.getConcept());

    MultipartFile imageFile = prototypeForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
            // 保存先のディレクトリ作成
            String uploadDir = System.getProperty("user.dir") + "/uploaded-images";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // ファイル名の生成（日付 + 元の名前）
            String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + imageFile.getOriginalFilename();
            
            // 物理ファイルの保存
            Path imagePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), imagePath);

            // DBに保存するパスを「新しいもの」に更新
            prototype.setImage("/uploads/" + fileName);

        } catch (IOException e) {
            System.out.println("画像保存エラー:" + e);
            return "prototypes/edit";
        }
    }

    try {
      prototypeRepository.update(prototype);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }

    return "redirect:/prototypes/" + prototypeId;
  }
}