package in.tech_camp.protospace_b;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    String path = "file:" + System.getProperty("user.dir") + "/uploaded-images/";

    registry.addResourceHandler("/uploads/**")
            .addResourceLocations(path);
  }
}
