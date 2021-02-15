package myprofile.profile.impl.dao;

import myprofile.common.result.Result;
import myprofile.profile.dao.ProfileConnectionFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.concurrent.locks.ReentrantLock;

import static myprofile.common.result.Result.ok;

public class ProfileConnectionFactoryImpl implements ProfileConnectionFactory {
    private SqlSessionFactory factory = null;
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public Result<SqlSession> openSession() {
        this.buildFactory();
        return ok(this.factory.openSession());
    }

    @Override
    public Result<SqlSession> openTransaction() {
        this.buildFactory();
        return ok(this.factory.openSession(false));
    }

    private void buildFactory() {
        if (factory == null) {
            try {
                lock.lock();
                if (factory == null) {
                    makeFactory();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public Result<SqlSession> openBatchSession() {
        this.buildFactory();
        return ok(this.factory.openSession(ExecutorType.BATCH, false));
    }

    private DataSource makeDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("DB_URL");
        dataSource.setUsername("DB_USER");
        dataSource.setPassword("DB_PASSWORD");
        return dataSource;
    }

    private void makeFactory() {
        TransactionFactory transactionFactory =
                new JdbcTransactionFactory();
        Environment environment =
                new Environment("development", transactionFactory, this.makeDataSource());
        Configuration configuration = new Configuration(environment);
        this.register(configuration);
        this.factory =
                new SqlSessionFactoryBuilder().build(configuration);
    }

    private void register(Configuration configuration) {
        configuration.addMapper(JobSyncDAOImpl.Mapper.class);
        configuration.addMapper(JobDAOImpl.Mapper.class);
        configuration.addMapper(SkillDAOImpl.Mapper.class);
        configuration.addMapper(JobSkillDAOImpl.Mapper.class);
    }

}
