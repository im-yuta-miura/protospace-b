package in.tech_camp.protospace_b.controller;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_b.ImageUrl;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.form.PrototypeForm;
import in.tech_camp.protospace_b.repository.PrototypeRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PrototypeControllerUnitTest {

    @Mock
    private PrototypeRepository prototypeRepository;
  @Mock
  private CustomUserDetail currentUser;

  @InjectMocks
  private PrototypeController prototypeController;

    @Mock
    private ImageUrl imageUrl;

    @InjectMocks
    private PrototypeController prototypeController;

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ExtendedModelMap();
    }

    // --- 新規投稿画面のテスト ---

    @Test
    public void 投稿画面にリクエストするとformのビューファイルが返ってくる() {
        String result = prototypeController.showForm(model);
        assertThat(result, is("form"));
    }

    // --- 投稿保存のテスト ---

    @Test
    public void 正しい情報を入力して投稿するとトップページにリダイレクトされる() {
        PrototypeForm form = new PrototypeForm();
        form.setTitle("テストタイトル");

        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "test".getBytes());
        form.setImage(image);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        CustomUserDetail currentUser = mock(CustomUserDetail.class);
        when(currentUser.getId()).thenReturn(1);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // 画像保存パスの設定
        when(imageUrl.getPath()).thenReturn(Path.of("test-uploads"));

        String result = prototypeController.createPrototype(form, bindingResult, currentUser);

        assertThat(result, is("redirect:/"));
        verify(prototypeRepository, times(1)).insert(any());
    }

    
    

    @Test
    public void 詳細機能にリクエストするとプロトタイプ詳細ページのビューファイルがレスポンスで返ってくる() {
        PrototypeEntity dummyPrototype = new PrototypeEntity();
        dummyPrototype.setId(1);
        dummyPrototype.setComments(new ArrayList<>());

        lenient().when(prototypeRepository.findById(1)).thenReturn(dummyPrototype);

        String result = prototypeController.showPrototypeDetail(1, model);

        assertThat(result, is("prototypes/detail"));
    }

    @Test
    public void 詳細機能にリクエストするとレスポンスに投稿済みのプロトタイプ情報が含まれること() {
        PrototypeEntity prototype1 = new PrototypeEntity();
        prototype1.setId(1);
        prototype1.setTitle("プロトタイプ1");
        prototype1.setComments(new ArrayList<>());

        when(prototypeRepository.findById(1)).thenReturn(prototype1);

        prototypeController.showPrototypeDetail(1, model);

        assertThat(model.getAttribute("prototype"), is(prototype1));
    }

    @Test
    public void 詳細機能にリクエストするとレスポンスにコメントフォームが存在する() {
        PrototypeEntity dummyPrototype = new PrototypeEntity();
        dummyPrototype.setComments(new ArrayList<>());
        when(prototypeRepository.findById(1)).thenReturn(dummyPrototype);

        prototypeController.showPrototypeDetail(1, model);

        assertThat(model.getAttribute("commentForm"), is(instanceOf(CommentForm.class)));
      // 準備：コントローラーが途中で落ちないようにプロトタイプを準備
      PrototypeEntity dummyPrototype = new PrototypeEntity();
      dummyPrototype.setComments(new ArrayList<>()); // getComments()で落ちないように
      when(prototypeRepository.findById(1)).thenReturn(dummyPrototype);

      Model model = new ExtendedModelMap(); 
      
      prototypeController.showPrototypeDetail(1, model); 
      
      assertThat(model.getAttribute("commentForm"), is(instanceOf(CommentForm.class)));
    }


  @Test
    public void 投稿者本人が削除リクエストを送った場合削除に成功しトップページへリダイレクトする() {
      // 準備：IDが「1」のユーザーと、そのユーザーが投稿したプロトタイプ
      when(currentUser.getId()).thenReturn(1);
      
      PrototypeEntity prototype = new PrototypeEntity();
      prototype.setUser_id(1); // 投稿者のIDを「1」にセット
      
      when(prototypeRepository.findById(100)).thenReturn(prototype);

      
      String result = prototypeController.deletePrototype(100, currentUser);

      verify(prototypeRepository, times(1)).deleteById(100);
  
      assertThat(result, is("redirect:/"));
}

  @Test
    public void 投稿者本人以外が削除リクエストを送った場合削除されず詳細ページへリダイレクトする() {
      // 1. 準備：IDが「1」のユーザーに対し、投稿者が「99（他人）」のプロトタイプ
      when(currentUser.getId()).thenReturn(1);
      
      PrototypeEntity prototype = new PrototypeEntity();
      prototype.setUser_id(99); // 他人の投稿
      
      when(prototypeRepository.findById(100)).thenReturn(prototype);

      // 2. 実行
      String result = prototypeController.deletePrototype(100, currentUser);

      // 3. 検証：
      // ① deleteById が「一度も呼ばれていない」ことを確認（重要！）
      verify(prototypeRepository, never()).deleteById(anyInt());
      // ② 元の詳細ページへリダイレクトされることを確認
      assertThat(result, is("redirect:/prototypes/100"));
  }
      // 2. 実行
      prototypeController.showPrototypeDetail(1, model); 
      
      // 3. 検証：インスタンスそのものを比べるのではなく「CommentFormクラスのデータが入っているか」を確認
      assertThat(model.getAttribute("commentForm"), is(instanceOf(CommentForm.class)));
    }
}