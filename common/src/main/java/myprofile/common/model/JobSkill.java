package myprofile.common.model;

import java.util.Objects;

public class JobSkill {
    private Integer id;
    private Integer idSkill;
    private String skillName;
    private String experience;
    private String idJob;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSkill() {
        return idSkill;
    }

    public void setIdSkill(Integer idSkill) {
        this.idSkill = idSkill;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobSkill)) return false;
        JobSkill jobSkill = (JobSkill) o;
        return Objects.equals(getSkillName(), jobSkill.getSkillName()) && Objects.equals(getExperience(), jobSkill.getExperience());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSkillName(), getExperience());
    }

    @Override
    public String toString() {
        return "JobSkill{" +
                "name='" + skillName + '\'' +
                ", experience='" + experience + '\'' +
                '}';
    }


}
