package myprofile.services;

import myprofile.common.dto.CheckUserRequest;
import myprofile.common.dto.CheckUserResponse;
import myprofile.common.dto.FetchFitJobsRequest;
import myprofile.common.dto.FetchFitJobsResponse;
import myprofile.common.result.Result;

public interface ProfileServices {
    Result<CheckUserResponse> login(CheckUserRequest request);

    Result<FetchFitJobsResponse> fetchFitJobs(FetchFitJobsRequest request);
}
