package in.tech_camp.protospace_b.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PrototypeFormTest {

    private Validator validator;

    // テストの前に毎回「審判（バリデーター）」を準備する
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void すべて正しく入力されていればバリデーションエラーは起きない() {
        // 1. 準備：テスト用のニセ画像を作る
        MockMultipartFile mockImage = new MockMultipartFile(
            "image", "test.png", "image/png", "test data".getBytes());

        // 2. 準備：フォームに完璧なデータをセットする
        PrototypeForm form = new PrototypeForm();
        form.setTitle("テストタイトル");
        form.setCatchphrase("テストキャッチコピー");
        form.setConcept("テストコンセプト");
        form.setImage(mockImage);

        // 3. 実行：審判にチェックしてもらう
        Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(form);

        // 4. 検証：エラー（violations）が0件であること（isEmptyがtrue）を確認
        assertTrue(violations.isEmpty());
    }

    @Test
    public void タイトルが空の場合はエラーが発生する() {
        // 1. 準備：テスト用のニセ画像を作る
        MockMultipartFile mockImage = new MockMultipartFile(
            "image", "test.png", "image/png", "test data".getBytes());

        // 2. 準備：タイトルだけわざと「空文字」にする
        PrototypeForm form = new PrototypeForm();
        form.setTitle(""); // ここが @NotBlank に引っかかるはず！
        form.setCatchphrase("テストキャッチコピー");
        form.setConcept("テストコンセプト");
        form.setImage(mockImage);

        // 3. 実行：審判にチェックしてもらう
        Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(form);

        // 4. 検証：エラーが「1件」出ていることを確認する
        assertEquals(1, violations.size());
        
        // （おまけ）エラーメッセージが正しいか確認
        assertEquals("空白は許可されていません", violations.iterator().next().getMessage()); 
        // ※もしエラーメッセージのテストで失敗したら、↑の "空白は許可されていません" の部分を、実際に出た英語のエラー（"must not be blank"など）に書き換えてください。
    }
}