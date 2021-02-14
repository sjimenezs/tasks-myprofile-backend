package myprofile.profile.dao;

import myprofile.common.model.JobSkill;
import myprofile.common.result.Result;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public interface JobSkillDAO {
    Result<Integer> save(SqlSession session, JobSkill skill);
}
