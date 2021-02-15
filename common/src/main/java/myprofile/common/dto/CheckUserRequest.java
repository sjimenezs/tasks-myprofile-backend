package myprofile.common.dto;

import java.util.Objects;

public class CheckUserRequest {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckUserRequest)) return false;
        CheckUserRequest that = (CheckUserRequest) o;
        return Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public String toString() {
        return "CheckUserRequest{" +
                "username='" + username + '\'' +
                '}';
    }
}
