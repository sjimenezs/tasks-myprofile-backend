package myprofile.common.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Objects;

public class Job {
    private String id;
    private String objective;
    private List<JobSkill> skills;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public List<JobSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<JobSkill> skills) {
        this.skills = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Job)) return false;
        Job job = (Job) o;
        return Objects.equals(getId(), job.getId()) && Objects.equals(getObjective(), job.getObjective()) && Objects.equals(getSkills(), job.getSkills());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getObjective(), getSkills());
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", objective='" + objective + '\'' +
                ", skills=" + skills +
                '}';
    }
}
