package myprofile.profile.dao;

import myprofile.common.model.User;
import myprofile.common.result.Result;

import java.util.Optional;

public interface UserDAO {
    Result<Optional<User>> fetchUser(String username);
}
