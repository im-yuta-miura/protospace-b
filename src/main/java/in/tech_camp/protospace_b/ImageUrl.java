package in.tech_camp.protospace_b;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ImageUrl {
  @Value("${image.url}")
  private String url;

  public String getImageUrl(){
    return url;
  }

  public Path getPath(){
    return Paths.get(url, "uploaded-images").toAbsolutePath().normalize();
  }
}