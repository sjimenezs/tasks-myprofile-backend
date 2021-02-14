package myprofile.server;

import myprofile.profile.ProfileConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ProfileConfig.class})
public class MyProfileConfiguration {
}
