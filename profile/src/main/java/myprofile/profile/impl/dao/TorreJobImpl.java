package myprofile.profile.impl.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import myprofile.common.error.ErrorCodes;
import myprofile.common.model.Job;
import myprofile.common.model.JobSkill;
import myprofile.common.result.Result;
import myprofile.common.utils.TorreApiUtils;
import myprofile.profile.dao.TorreJobDAO;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;

public class TorreJobImpl implements TorreJobDAO {
    private OkHttpClient client = new OkHttpClient();

    @Override
    public Result<List<Job>> fetchJobs(int size, int offset) {
        var resultBuildUrl = TorreApiUtils.buildUrlFetchJobs(size, offset);
        if (resultBuildUrl.isError()) {
            return error(resultBuildUrl);
        }
        var body = RequestBody.create(TorreApiUtils.APPLICATION_JSON, "");
        var request = new Request.Builder()
                .url(resultBuildUrl.ok())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return parseJobsFetchResponse(response);
        } catch (JsonProcessingException e) {
            return error(ErrorCodes.USER_FETCH_READ_ERROR);
        } catch (Exception e) {
            return error(ErrorCodes.USER_FETCH_ERROR);
        }
    }

    @Override
    public Result<Integer> fetchJobsCount() {
        var resultBuildUrl = TorreApiUtils.buildUrlFetchJobs(1, 0);
        if (resultBuildUrl.isError()) {
            return error(resultBuildUrl);
        }
        var body = RequestBody.create(TorreApiUtils.APPLICATION_JSON, "");
        var request = new Request.Builder()
                .url(resultBuildUrl.ok())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return parseJobsCountResponse(response);
        } catch (JsonProcessingException e) {
            return error(ErrorCodes.USER_FETCH_READ_ERROR);
        } catch (Exception e) {
            return error(ErrorCodes.USER_FETCH_ERROR);
        }
    }

    @NotNull
    private Result<Integer> parseJobsCountResponse(Response response) throws IOException {
        var plainUserProfileResponse = response.body().string();
        var jsonUserProfileResponse = TorreApiUtils.jsonMapper.readTree(plainUserProfileResponse);
        var totalNode = jsonUserProfileResponse.get("total");
        if (totalNode == null) {
            return ok(0);
        }
        return ok(totalNode.asInt());
    }

    @NotNull
    private Result<List<Job>> parseJobsFetchResponse(Response response) throws IOException {
        var plainJobsResponse = response.body().string();
        var jsonJobsResponse = TorreApiUtils.jsonMapper.readTree(plainJobsResponse);
        var resultsNode = jsonJobsResponse.get("results");
        if (resultsNode == null) {
            return ok(Collections.emptyList());
        }
        var result = new ArrayList<Job>();
        for (var resultNode : resultsNode) {
            buildJob(resultNode).ifPresent(result::add);
        }

        return ok(result);
    }

    @Nullable
    private Optional<Job> buildJob(JsonNode resultNode) {
        var job = new Job();
        var idNode = resultNode.get("id");
        if (idNode == null) {
            return Optional.empty();
        }
        var objectiveNode = resultNode.get("objective");
        if (objectiveNode == null) {
            return Optional.empty();
        }
        job.setId(idNode.asText());
        job.setObjective(objectiveNode.asText());

        buildSkills(resultNode).ifPresent(job::setSkills);

        return Optional.of(job);
    }

    private Optional<List<JobSkill>> buildSkills(JsonNode resultNode) {
        var skillsNode = resultNode.get("skills");
        if (skillsNode == null) {
            return Optional.empty();
        }
        var skills = new ArrayList<JobSkill>();
        for (var skillNode : skillsNode) {
            var jobSkill = new JobSkill();
            var nameNode = skillNode.get("name");
            if (nameNode == null) {
                continue;
            }
            jobSkill.setSkillName(nameNode.asText());
            var experienceNode = skillNode.get("experience");
            if (experienceNode != null) {
                jobSkill.setExperience(experienceNode.asText());
            }
            skills.add(jobSkill);
        }
        return Optional.of(skills);
    }
}
