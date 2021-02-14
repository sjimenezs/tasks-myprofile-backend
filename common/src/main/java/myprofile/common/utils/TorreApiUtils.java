package myprofile.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import myprofile.common.error.ErrorCodes;
import myprofile.common.result.Result;
import okhttp3.MediaType;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;

public class TorreApiUtils {
    public static final okhttp3.MediaType APPLICATION_JSON = MediaType.get("application/json; charset=utf-8");
    public static ObjectMapper jsonMapper = new ObjectMapper();

    private static Result<String> fetchBaseApiUrl() {
        var base = System.getenv("TORRE_API_BASE");
        if (base == null || base.isEmpty()) {
            return error(ErrorCodes.NO_TORRE_API_DEFINED);
        }
        return ok(base);
    }

    private static Result<String> fetchSearchBaseApiUrl() {
        var base = System.getenv("TORRE_SEARCH_API_BASE");
        if (base == null || base.isEmpty()) {
            return error(ErrorCodes.NO_TORRE_SEARCH_API_DEFINED);
        }
        return ok(base);
    }

    public static Result<String> buildUrlFetchUser(String username) {
        if (username == null || username.isEmpty()) {
            return error(ErrorCodes.USERNAME_REQUIRED);
        }

        var rBaseApiUrl = fetchBaseApiUrl();
        if (rBaseApiUrl.isError()) {
            return error(rBaseApiUrl);
        }

        var urlBuilder = new StringBuilder(rBaseApiUrl.ok()).append("bios").append("/").append(username);
        return ok(urlBuilder.toString());
    }

    public static Result<String> buildUrlFetchJobs(int size, int offset) {
        var rBaseApiUrl = fetchSearchBaseApiUrl();
        if (rBaseApiUrl.isError()) {
            return error(rBaseApiUrl);
        }

        var urlBuilder = new StringBuilder(rBaseApiUrl.ok()).append("opportunities/_search?").append("offset=").append(offset).append("&size=").append(size);
        return ok(urlBuilder.toString());
    }
}
