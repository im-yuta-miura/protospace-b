package in.tech_camp.protospace_b.factory;

import org.springframework.mock.web.MockMultipartFile;

import com.github.javafaker.Faker;

import in.tech_camp.protospace_b.form.PrototypeForm;

public class PrototypeFormFactory {

  private static final Faker faker = new Faker();

  public static PrototypeForm createPrototypeForm() {
    PrototypeForm prototypeForm = new PrototypeForm();

    prototypeForm.setTitle(faker.lorem().sentence());
    prototypeForm.setCatchphrase(faker.lorem().sentence());   // キャッチコピー
    prototypeForm.setConcept(faker.lorem().paragraph());
    
    MockMultipartFile mockImage = new MockMultipartFile(
      "image",                
      "test.png",             
      "image/png",            
      "test data".getBytes()
    );

    prototypeForm.setImage(mockImage);

    return prototypeForm;

  }
}