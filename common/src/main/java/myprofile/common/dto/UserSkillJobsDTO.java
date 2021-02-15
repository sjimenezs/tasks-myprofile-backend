package myprofile.common.dto;

import java.util.Objects;

public class UserSkillJobsDTO {
    private Integer idSkill;
    private String name;
    private Integer count;

    public Integer getIdSkill() {
        return idSkill;
    }

    public void setIdSkill(Integer idSkill) {
        this.idSkill = idSkill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSkillJobsDTO)) return false;
        UserSkillJobsDTO that = (UserSkillJobsDTO) o;
        return Objects.equals(getIdSkill(), that.getIdSkill()) && Objects.equals(getName(), that.getName()) && Objects.equals(getCount(), that.getCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdSkill(), getName(), getCount());
    }

    @Override
    public String toString() {
        return "UserSkillJobsDTO{" +
                "idSkill=" + idSkill +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
