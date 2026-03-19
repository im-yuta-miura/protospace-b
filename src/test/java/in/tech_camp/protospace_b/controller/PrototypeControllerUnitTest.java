package in.tech_camp.protospace_b.controller;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
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
      // 準備：コントローラーが途中で落ちないようにプロトタイプを準備する
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
}
