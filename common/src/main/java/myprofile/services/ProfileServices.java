package myprofile.services;

import myprofile.common.dto.*;
import myprofile.common.result.Result;

public interface ProfileServices {
    Result<CheckUserResponse> login(CheckUserRequest request);

    Result<FetchFitJobsResponse> fetchFitJobs(FetchFitJobsRequest request);

    Result<CountJobsPerSkillResponse> countJobsPerSkillByIdUser(CountJobsPerSkillRequest request);
}
