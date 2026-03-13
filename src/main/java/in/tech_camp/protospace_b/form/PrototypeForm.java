package in.tech_camp.protospace_b.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrototypeForm {

  @NotBlank
  private String title;

  @NotBlank
  private String catchphrase;

  @NotBlank
  private String concept;

  @NotNull
  private MultipartFile image;
}

