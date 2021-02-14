package myprofile.common.error;

import java.util.Arrays;
import java.util.List;

public class MyProfileError {
    private String code;
    private List<Object> params;

    public MyProfileError() {
    }

    public MyProfileError(String code, Object... params) {
        this.code = code;
        if (params != null) {
            this.params = Arrays.asList(params);
        }
    }

    public String code() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", params=" + params +
                '}';
    }
}
