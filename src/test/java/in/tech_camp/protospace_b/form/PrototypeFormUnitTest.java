package in.tech_camp.protospace_b.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.protospace_b.factory.PrototypeFormFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
@SpringBootTest
public class PrototypeFormUnitTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
  
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    class プロトタイプが投稿できる場合 {
        @Test
        public void 全ての項目が適切に入力されていれば保存できる() {
            
            PrototypeForm prototypeForm = PrototypeFormFactory.createPrototypeForm();
            
            
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm);
            
            
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    class プロトタイプが投稿できない場合 {
        @Test
        public void titleが空だと保存できない() {
            PrototypeForm form = PrototypeFormFactory.createPrototypeForm();
            form.setTitle(""); 
            
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(form);
            assertEquals(1, violations.size()); 
        }

        @Test
        public void catchphraseが空だと保存できない() {
            PrototypeForm form = PrototypeFormFactory.createPrototypeForm();
            form.setCatchphrase(""); 
            
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(form);
            assertEquals(1, violations.size());
        }

        @Test
        public void conceptが空だと保存できない() {
            PrototypeForm form = PrototypeFormFactory.createPrototypeForm();
            form.setConcept(""); 
            
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(form);
            assertEquals(1, violations.size());
        }

        @Test
        public void imageがnullだと保存できない() {
            PrototypeForm form = PrototypeFormFactory.createPrototypeForm();
            form.setImage(null);
            
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(form);
            assertEquals(1, violations.size());
        }
    }
}