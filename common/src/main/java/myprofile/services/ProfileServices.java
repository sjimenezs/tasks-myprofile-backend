package myprofile.services;

import myprofile.common.dto.CheckUserRequest;
import myprofile.common.dto.CheckUserResponse;
import myprofile.common.result.Result;

public interface ProfileServices {
    Result<CheckUserResponse> login(CheckUserRequest request);
}
