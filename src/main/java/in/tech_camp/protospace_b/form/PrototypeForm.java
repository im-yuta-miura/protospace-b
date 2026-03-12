package in.tech_camp.protospace_b.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PrototypeForm {
  private String title;
  private String catchphrase;
  private String concept;
  private MultipartFile image;
}

