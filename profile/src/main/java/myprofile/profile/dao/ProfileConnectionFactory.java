package myprofile.profile.dao;

import myprofile.common.result.Result;
import org.apache.ibatis.session.SqlSession;

public interface ProfileConnectionFactory {
    Result<SqlSession> openSession();

    Result<SqlSession> openTransaction();

    Result<SqlSession> openBatchSession();
}
