package myprofile.profile.dao;

import myprofile.common.model.User;
import myprofile.common.result.Result;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Result<Boolean> save(SqlSession session, User user);

    Result<Optional<User>> fetchByUsername(SqlSession session, String username);
}
