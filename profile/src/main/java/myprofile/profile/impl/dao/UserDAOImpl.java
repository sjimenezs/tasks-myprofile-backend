package myprofile.profile.impl.dao;

import myprofile.common.error.ErrorCodes;
import myprofile.common.model.Job;
import myprofile.common.model.User;
import myprofile.common.result.Result;
import myprofile.profile.dao.UserDAO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;

public class UserDAOImpl implements UserDAO {
    @Override
    public Result<Boolean> save(SqlSession session, User user) {
        var mapper = session.getMapper(Mapper.class);
        try {
            mapper.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.SAVE_USER_ERROR);
        }

        return ok(true);
    }

    @Override
    public Result<Optional<User>> fetchByUsername(SqlSession session, String username) {
        var mapper = session.getMapper(Mapper.class);
        try {
            var res = mapper.fetchByUsername(username.toLowerCase());
            if (res == null) {
                return ok(Optional.empty());
            }
            return ok(Optional.of(res));
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.FETCH_USER_ERROR);
        }

    }

    public interface Mapper {
        @Insert("INSERT into user(id,username,name) VALUES(#{id}, #{username}, #{name})")
        void insert(User user);

        @Select("select id, name from user where username=lower(#{username})")
        User fetchByUsername(String username);
    }
}
