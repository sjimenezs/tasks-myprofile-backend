package myprofile.server;

import myprofile.server.schema.SchemaUpdater;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication()
@ComponentScan(basePackages = {"myprofile.server", "myprofile.profile"})
public class MyProfileApplication {
    public static void main(String[] args) throws Exception {
        new SchemaUpdater().update();
        SpringApplication.run(MyProfileApplication.class, args);
    }
}
