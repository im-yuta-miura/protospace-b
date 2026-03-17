// package in.tech_camp.protospace_b.form;
// import java.util.Set;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.test.context.ActiveProfiles;

// import in.tech_camp.protospace_b.factory.CommentFormFactory;
// import in.tech_camp.protospace_b.validation.ValidationPriority1;
// import jakarta.validation.ConstraintViolation;
// import jakarta.validation.Validation;
// import jakarta.validation.Validator;
// import jakarta.validation.ValidatorFactory;

// @ActiveProfiles("test")
// public class CommentFormUnitTest {

//   private CommentForm commentForm;
//   private Validator validator;

//    @BeforeEach
//     public void setUp() {
//         ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//         validator = factory.getValidator();
//         commentForm = CommentFormFactory.createComment();
//     }

//   @Test
//     public void コメントが空の場合バリデーションエラーが発生する() {
//       commentForm.setContent("");
//       Set<ConstraintViolation<CommentForm>> violations = validator.validate(commentForm,ValidationPriority1.class);
//       assertEquals(1, violations.size());
//       assertEquals("Comment can't be blank", violations.iterator().next().getMessage());
//     }
// }
