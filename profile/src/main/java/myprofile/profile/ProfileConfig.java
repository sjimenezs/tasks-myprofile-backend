package myprofile.profile;

import myprofile.profile.dao.*;
import myprofile.profile.impl.dao.*;
import myprofile.profile.impl.services.ProfileServicesImpl;
import myprofile.services.ProfileServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("myprofile.profile")
@EnableScheduling
public class ProfileConfig {
    @Bean
    public ProfileServices profileServices(UserDAO userDAO, TorreJobDAO torreJobDAO, JobSyncDAO jobSyncDAO, JobDAO jobDAO, ProfileConnectionFactory connectionFactory, SkillDAO skillDAO, JobSkillDAO jobSkillDAO) {
        return new ProfileServicesImpl(userDAO, torreJobDAO, jobSyncDAO, jobDAO, skillDAO, jobSkillDAO, connectionFactory);
    }

    @Bean
    public TorreJobDAO torreJobDAO() {
        return new TorreJobImpl();
    }

    @Bean
    public UserDAO userDAO() {
        return new UserDAOImpl();
    }

    @Bean
    public JobSyncDAO jobSyncDAO() {
        return new JobSyncDAOImpl();
    }

    @Bean
    public JobDAO jobDAO() {
        return new JobDAOImpl();
    }

    @Bean
    public SkillDAO skillDAO() {
        return new SkillDAOImpl();
    }

    @Bean
    public JobSkillDAO jobSkillDAO() {
        return new JobSkillDAOImpl();
    }

    @Bean
    @Scope("singleton")
    public ProfileConnectionFactory profileConnectionFactory() {
        return new ProfileConnectionFactoryImpl();
    }
}
