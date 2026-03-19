package in.tech_camp.protospace_b.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.PrototypeForm;
import in.tech_camp.protospace_b.repository.PrototypeRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PrototypeControllerUnitTest {

  @Mock
  private PrototypeRepository prototypeRepository;

  @Mock
  private ImageUrl imageUrl;

  @InjectMocks
  private PrototypeController prototypeController;

  private Model model;

  @BeforeEach
  public void setUp() {

    model = new ExtendedModelMap();

  }

  @Test
  public void 投稿画面にリクエストするとformのビューファイルが返ってくる() {

    String result = prototypeController.showForm(model);

    assertThat(result,is("form"));

  }

  @Test
  public void 正しい情報を入力して投稿するとトップページにリダイレクトされる(){

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

    when(imageUrl.getPath()).thenReturn(java.nio.file.Path.of("test-uploads"));

    String result = prototypeController.createPrototype(form, bindingResult, currentUser);

    assertThat(result, is("redirect:/"));
        
    verify(prototypeRepository, times(1)).insert(any());

  }
}

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.PrototypeRepository;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PrototypeControllerUnitTest {
  @Mock
  private PrototypeRepository prototypeRepository;

  @InjectMocks
  private PrototypeController prototypeController;

  @Test
    public void 詳細機能にリクエストするとプロトタイプ詳細ページのビューファイルがレスポンスで返ってくる() {
        
      // 1. 事前準備：モックが返すダミーのプロトタイプデータを作る
      PrototypeEntity dummyPrototype = new PrototypeEntity();
      dummyPrototype.setId(1);
    
      // repository.findById(1) が呼ばれたら dummyPrototype を返すように設定
      // ※これをしないと Controller 内で NullPointerException が起きる可能性があります
      lenient().when(prototypeRepository.findById(1)).thenReturn(dummyPrototype);
      Model model = new ExtendedModelMap();

        String result = prototypeController.showPrototypeDetail(1,model);

        assertThat(result,is("prototypes/detail"));
    }


  @Test
    public void 詳細機能にリクエストするとレスポンスに投稿済みのプロトタイプ情報が含まれること() {
        PrototypeEntity prototype1 = new PrototypeEntity();
        prototype1.setId(1);
        prototype1.setTitle("プロトタイプ1");
        prototype1.setCatchphrase("プロトタイプ1");
        prototype1.setConcept("プロトタイプ1");
        prototype1.setImage("image1.jpg");

        prototype1.setComments(new ArrayList<>());

        when(prototypeRepository.findById(1)).thenReturn(prototype1);

        Model model = new ExtendedModelMap();
        prototypeController.showPrototypeDetail(1,model);

        assertThat(model.getAttribute("prototype"), is(prototype1));
    }

  @Test
    public void 詳細機能にリクエストするとレスポンスにコメントフォームが存在する() {
      // 1. 準備：コントローラーが途中で落ちないようにプロトタイプを準備する
      PrototypeEntity dummyPrototype = new PrototypeEntity();
      dummyPrototype.setComments(new ArrayList<>()); // getComments()で落ちないように
      when(prototypeRepository.findById(1)).thenReturn(dummyPrototype);

      Model model = new ExtendedModelMap(); 
      
      // 2. 実行
      prototypeController.showPrototypeDetail(1, model); 
      
      // 3. 検証：インスタンスそのものを比べるのではなく「CommentFormクラスのデータが入っているか」を確認
      assertThat(model.getAttribute("commentForm"), is(instanceOf(CommentForm.class)));
    }
}
