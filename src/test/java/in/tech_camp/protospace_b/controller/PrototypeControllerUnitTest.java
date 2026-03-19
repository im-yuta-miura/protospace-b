package in.tech_camp.protospace_b.controller;

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
