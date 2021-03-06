package myprofile.profile.dao;

import myprofile.common.dto.JobUserSkillDTO;
import myprofile.common.dto.UserSkillJobsDTO;
import myprofile.common.model.UserSkill;
import myprofile.common.result.Result;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public interface UserSkillDAO {
    Result<Integer> save(SqlSession session, UserSkill skill);

    Result<List<Integer>> fetchAllIdsByIdUser(SqlSession session, String idUser);

    Result<List<JobUserSkillDTO>> fetchMatchedSkillJobs(SqlSession session, String idUser);

    Result<List<UserSkillJobsDTO>> countJobsPerSkillByIdUser(SqlSession session, String idUser);
}
