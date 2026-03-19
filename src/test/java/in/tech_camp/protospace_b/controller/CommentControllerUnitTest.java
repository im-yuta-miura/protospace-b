package in.tech_camp.protospace_b.controller;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.CommentRepository;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import in.tech_camp.protospace_b.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CommentControllerUnitTest {
  @Mock
  private PrototypeRepository prototypeRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private CustomUserDetail currentUser;

  @Mock
  private BindingResult bindingResult;

  @InjectMocks
  private CommentController prototypeController;
  private Model model;
  
  @BeforeEach
    void setUp() {
        model = new ExtendedModelMap();
    }

  @Test
  public void コメントが有効な場合保存に成功し詳細ページへリダイレクトする() {
    int prototypeId = 100;
    int userId = 1;
    
    when(bindingResult.hasErrors()).thenReturn(false);
    when(currentUser.getId()).thenReturn(userId);
    
    PrototypeEntity dummyPrototype = new PrototypeEntity();
    when(prototypeRepository.findById(prototypeId)).thenReturn(dummyPrototype);
    when(userRepository.findById(userId)).thenReturn(new UserEntity());
    
    CommentForm commentForm = new CommentForm();
    commentForm.setContent("テストコメント");

    String result = prototypeController.createComment(prototypeId, commentForm, bindingResult, currentUser, model);


    verify(commentRepository, times(1)).insert(any(CommentEntity.class));
  
    assertThat(result, is("redirect:/prototypes/" + prototypeId));
  }


  @Test
  public void バリデーションエラーがある場合保存されずに詳細ページが表示される() {
    int prototypeId = 100;
    when(bindingResult.hasErrors()).thenReturn(true); 
    
    PrototypeEntity dummyPrototype = new PrototypeEntity();
    dummyPrototype.setComments(new ArrayList<>()); 
    when(prototypeRepository.findById(prototypeId)).thenReturn(dummyPrototype);

    CommentForm commentForm = new CommentForm();

    String result = prototypeController.createComment(prototypeId, commentForm, bindingResult, currentUser, model);

    verify(commentRepository, never()).insert(any(CommentEntity.class));
    
    assertThat(result, is("prototypes/detail"));
    
    assertThat(model.getAttribute("prototype"), is(dummyPrototype));
  }

}
