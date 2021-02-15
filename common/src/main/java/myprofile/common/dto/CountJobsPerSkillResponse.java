package myprofile.common.dto;

import java.util.List;

public class CountJobsPerSkillResponse {
    private List<UserSkillJobsDTO> skills;

    public CountJobsPerSkillResponse(List<UserSkillJobsDTO> skills) {
        this.skills = skills;
    }

    public List<UserSkillJobsDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<UserSkillJobsDTO> skills) {
        this.skills = skills;
    }
}
