package myprofile.profile.impl.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import myprofile.common.error.ErrorCodes;
import myprofile.common.model.JobSkill;
import myprofile.common.model.User;
import myprofile.common.model.UserSkill;
import myprofile.common.result.Result;
import myprofile.common.utils.TorreApiUtils;
import myprofile.profile.dao.TorreUserDAO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;

public class TorreUserDAOImpl implements TorreUserDAO {
    private OkHttpClient client = new OkHttpClient();

    @Override
    public Result<Optional<User>> fetchUser(String username) {
        var rUrl = TorreApiUtils.buildUrlFetchUser(username);
        if (rUrl.isError()) {
            return error(rUrl);
        }
        Request request = new Request.Builder()
                .url(rUrl.ok())
                .build();

        try (Response response = client.newCall(request).execute()) {
            return parseUserFetchResponse(response);
        } catch (JsonProcessingException e) {
            return error(ErrorCodes.USER_FETCH_READ_ERROR);
        } catch (Exception e) {
            return error(ErrorCodes.USER_FETCH_ERROR);
        }
    }

    @NotNull
    private Result<Optional<User>> parseUserFetchResponse(Response response) throws IOException {
        var plainUserProfileResponse = response.body().string();
        var jsonUserProfileResponse = TorreApiUtils.jsonMapper.readTree(plainUserProfileResponse);
        var personNode = jsonUserProfileResponse.get("person");
        if (personNode == null) {
            return ok(Optional.empty());
        }
        var idNode = personNode.get("id");
        if (idNode == null) {
            return error(ErrorCodes.USER_FETCH_READ_ERROR);
        }
        var nameNode = personNode.get("name");
        if (nameNode == null) {
            return error(ErrorCodes.USER_FETCH_READ_ERROR);
        }

        var publicIdNode = personNode.get("publicId");
        if (publicIdNode == null) {
            return error(ErrorCodes.USER_FETCH_READ_ERROR);
        }

        var user = new User();
        user.setId(idNode.asText());
        user.setName(nameNode.asText());
        user.setUsername(publicIdNode.asText());
        buildSkills(jsonUserProfileResponse).ifPresent(user::setSkills);

        return ok(Optional.of(user));
    }

    private Optional<List<UserSkill>> buildSkills(JsonNode resultNode) {
        var skillsNode = resultNode.get("strengths");
        if (skillsNode == null) {
            return Optional.empty();
        }
        var skills = new ArrayList<UserSkill>();
        for (var skillNode : skillsNode) {
            var userSkill = new UserSkill();
            var nameNode = skillNode.get("name");
            if (nameNode == null) {
                continue;
            }
            userSkill.setSkillName(nameNode.asText());
            skills.add(userSkill);
        }
        return Optional.of(skills);
    }
}
