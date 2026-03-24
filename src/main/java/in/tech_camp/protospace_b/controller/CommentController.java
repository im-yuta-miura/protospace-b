package in.tech_camp.protospace_b.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.CommentRepository;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import in.tech_camp.protospace_b.repository.UserRepository;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CommentController {

  private final CommentRepository commentRepository;

  private final UserRepository userRepository;

  private final PrototypeRepository prototypeRepository;

  @PostMapping("/prototypes/{prototypeId}/comment")
  public String createComment(@PathVariable("prototypeId") Integer prototypeId, 
                              @ModelAttribute("commentForm") @Validated(ValidationOrder.class) CommentForm commentForm,
                              BindingResult result,
                              @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

    if (result.hasErrors()) {
      model.addAttribute("errorMessages", result.getAllErrors());
      model.addAttribute("prototype", prototype);
      model.addAttribute("commentForm", commentForm);
      model.addAttribute("comments", prototype.getComments());
      return "prototypes/detail";
    }

    CommentEntity comment = new CommentEntity();
    comment.setContent(commentForm.getContent());
    comment.setPrototype(prototype);
    comment.setUser(userRepository.findById(currentUser.getId()));

    try {
      commentRepository.insert(comment);
    } catch (Exception e) {
      model.addAttribute("prototype", prototype);
      model.addAttribute("commentForm", commentForm);
      System.out.println("エラー：" + e);
      return "prototypes/detail";
    }

    return "redirect:/prototypes/" + prototypeId;
  }

  // --- コメント編集画面の表示 ---
    @GetMapping("/comments/{commentId}/edit")
    public String editComment(@PathVariable("commentId") Integer commentId, 
                              @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
        CommentEntity comment = commentRepository.findById(commentId);

        // 存在チェック & 本人確認
        if (comment == null || !comment.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/";
        }

        CommentForm commentForm = new CommentForm();
        commentForm.setContent(comment.getContent());
        
        model.addAttribute("commentForm", commentForm);
        model.addAttribute("commentId", commentId);
        model.addAttribute("prototypeId", comment.getPrototype().getId()); // 戻るボタン等に便利

        return "comments/edit"; // HTMLのパスを指定
    }
  
  // --- コメント更新処理 ---
    @PostMapping("/comments/{commentId}/update")
    public String updateComment(@PathVariable("commentId") Integer commentId, 
                                @ModelAttribute("commentForm") @Validated(ValidationOrder.class) CommentForm commentForm,
                                BindingResult result,
                                @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
        
        CommentEntity comment = commentRepository.findById(commentId);

        // セキュリティチェック：本人のコメントか？
        if (comment == null || !comment.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            model.addAttribute("commentId", commentId);
            return "comments/edit";
        }

        // 既存のエンティティの内容を書き換えて更新
        comment.setContent(commentForm.getContent());
        commentRepository.update(comment);

        // 紐づいているプロトタイプの詳細画面へ戻る
        return "redirect:/prototypes/" + comment.getPrototype().getId();
    }

    // --- コメント削除処理 ---
    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") Integer commentId,
                                @AuthenticationPrincipal CustomUserDetail currentUser) {
        
        CommentEntity comment = commentRepository.findById(commentId);

        // 本人確認
        if (comment != null && comment.getUser().getId().equals(currentUser.getId())) {
            Integer prototypeId = comment.getPrototype().getId();
            commentRepository.deleteById(commentId);
            return "redirect:/prototypes/" + prototypeId;
        }

        return "redirect:/";
    }
}
  

