package myprofile.common.dto;

public class CheckUserResponse {
    private boolean exists;

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean getExists() {
        return exists;
    }
}
