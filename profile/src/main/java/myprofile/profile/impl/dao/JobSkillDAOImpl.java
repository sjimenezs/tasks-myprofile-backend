package myprofile.profile.impl.dao;

import myprofile.common.error.ErrorCodes;
import myprofile.common.model.JobSkill;
import myprofile.common.model.Skill;
import myprofile.common.result.Result;
import myprofile.profile.dao.JobSkillDAO;
import myprofile.profile.dao.SkillDAO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.List;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;

public class JobSkillDAOImpl implements JobSkillDAO {
    @Override
    public Result<Integer> save(SqlSession session, JobSkill jobSkill) {
        var mapper = session.getMapper(Mapper.class);
        try {
            return ok(mapper.insert(jobSkill));
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.SAVE_JOBSKILL_ERROR);
        }
    }

    public interface Mapper {
        @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        @Insert("INSERT into jobskill(idjob,idskill,experience) VALUES(#{idJob},#{idSkill},#{experience})")
        Integer insert(JobSkill jobSkill);
    }
}
