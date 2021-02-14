package myprofile.profile;

import myprofile.common.context.ExecutionContext;
import myprofile.common.dto.CheckUserRequest;
import myprofile.common.dto.CheckUserResponse;
import myprofile.common.result.Result;
import myprofile.services.ProfileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController()
@RequestMapping("profile")
public class ProfileApi {
    private final ProfileServices services;

    @Autowired
    public ProfileApi(ProfileServices services) {
        this.services = services;
    }

    @GetMapping("/v1/status")
    String status() {
        return "ok";
    }

    @PostMapping("/v1/checkuser")
    Result<CheckUserResponse> CheckUserRequest(@RequestBody CheckUserRequest request) {
        return this.services.checkUser(request);
    }
}
