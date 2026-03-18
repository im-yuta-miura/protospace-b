package in.tech_camp.protospace_b;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final ImageUrl url;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    String path = "file:" + url.getPath().toString() + "/";

    registry.addResourceHandler("/uploads/**")
            .addResourceLocations(path);
  }
}
