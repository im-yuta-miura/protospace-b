package in.tech_camp.protospace_b.factory;

import org.springframework.mock.web.MockMultipartFile;

import in.tech_camp.protospace_b.form.PrototypeForm;

public class EditFormFactory {
  public static PrototypeForm createPrototype() {
    PrototypeForm prototypeForm = new PrototypeForm();

    prototypeForm.setTitle("編集前のタイトル");
    prototypeForm.setCatchphrase("編集前のキャッチフレーズ");
    prototypeForm.setConcept("編集前のコンセプト");

    MockMultipartFile mockFile = new MockMultipartFile(
            "image",
            "test-image.png",
            "image/png",
            "test data".getBytes()
        );
    prototypeForm.setImage(mockFile);

    return prototypeForm;
  }
}
