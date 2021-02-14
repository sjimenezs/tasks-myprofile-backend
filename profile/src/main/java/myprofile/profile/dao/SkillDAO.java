package myprofile.profile.dao;

import myprofile.common.model.Job;
import myprofile.common.model.Skill;
import myprofile.common.result.Result;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public interface SkillDAO {
    Result<Integer> save(SqlSession session, Skill skill);

    Result<List<Skill>> fetchAll(SqlSession session);
}
