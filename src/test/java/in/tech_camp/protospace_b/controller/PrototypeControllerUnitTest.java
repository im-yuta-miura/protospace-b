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
    }
}