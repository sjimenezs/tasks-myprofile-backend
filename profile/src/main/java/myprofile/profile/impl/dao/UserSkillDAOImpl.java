package myprofile.profile.impl.dao;

import myprofile.common.dto.JobUserSkillDTO;
import myprofile.common.error.ErrorCodes;

import myprofile.common.model.JobSkill;
import myprofile.common.model.UserSkill;
import myprofile.common.result.Result;
import myprofile.profile.dao.UserSkillDAO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.List;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;


public class UserSkillDAOImpl implements UserSkillDAO {
    @Override
    public Result<Integer> save(SqlSession session, UserSkill userSkill) {
        var mapper = session.getMapper(Mapper.class);
        try {
            mapper.insert(userSkill);
            return ok(userSkill.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.SAVE_USERSKILL_ERROR);
        }
    }

    @Override
    public Result<List<Integer>> fetchAllIdsByIdUser(SqlSession session, String idUser) {
        var mapper = session.getMapper(Mapper.class);
        try {
            var res = mapper.fetchAllIdsByIdUser(idUser);
            if (res == null) {
                return ok(Collections.emptyList());
            }
            return ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.FETCH_USERSKILL_ERROR);
        }

    }

    @Override
    public Result<List<JobUserSkillDTO>> fetchMatchedSkillJobs(SqlSession session, String idUser) {
        var mapper = session.getMapper(Mapper.class);
        try {
            var res = mapper.fetchMatchedSkillJobs(idUser);
            if (res == null) {
                return ok(Collections.emptyList());
            }
            return ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.FETCH_USERSKILL_ERROR);
        }

    }

    public interface Mapper {
        @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        @Insert("INSERT into userskill(iduser,idskill) VALUES(#{idUser},#{idSkill})")
        Integer insert(UserSkill userSkill);

        @Select("select idskill from userskill where iduser=#{isUser}")
        List<Integer> fetchAllIdsByIdUser(String isUser);

        @Select("select j.idJob, count (j.id) as total, count(u.id) as matched  from jobskill j left outer  join userskill u on (j.idskill = u.idskill and u.iduser = #{idUser}) group by j.idjob")
        List<JobUserSkillDTO> fetchMatchedSkillJobs(String idUser);
    }
}
