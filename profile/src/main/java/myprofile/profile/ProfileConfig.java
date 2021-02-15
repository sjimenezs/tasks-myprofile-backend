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
    public ProfileServices profileServices(TorreUserDAO torreUserDAO, TorreJobDAO torreJobDAO, JobSyncDAO jobSyncDAO, JobDAO jobDAO, ProfileConnectionFactory connectionFactory, SkillDAO skillDAO, JobSkillDAO jobSkillDAO, UserSkillDAO userSkillDAO, UserDAO userDAO) {
        return new ProfileServicesImpl(torreUserDAO, torreJobDAO, jobSyncDAO, jobDAO, skillDAO, jobSkillDAO, userDAO, userSkillDAO, connectionFactory);
    }

    @Bean
    public TorreJobDAO torreJobDAO() {
        return new TorreJobImpl();
    }

    @Bean
    public TorreUserDAO torreUserDAO() {
        return new TorreUserDAOImpl();
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
    public UserSkillDAO userSkillDAO() {
        return new UserSkillDAOImpl();
    }

    @Bean
    public UserDAO userDAO() {
        return new UserDAOImpl();
    }

    @Bean
    @Scope("singleton")
    public ProfileConnectionFactory profileConnectionFactory() {
        return new ProfileConnectionFactoryImpl();
    }
}
