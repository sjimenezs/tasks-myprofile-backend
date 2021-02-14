package myprofile.profile.impl.dao;

import myprofile.common.error.ErrorCodes;
import myprofile.common.model.Skill;
import myprofile.common.result.Result;
import myprofile.profile.dao.SkillDAO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.List;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;

public class SkillDAOImpl implements SkillDAO {
    @Override
    public Result<Integer> save(SqlSession session, Skill skill) {
        var mapper = session.getMapper(Mapper.class);
        try {
            mapper.insert(skill);
            return ok(skill.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.SAVE_SKILL_ERROR);
        }
    }

    @Override
    public Result<List<Skill>> fetchAll(SqlSession session) {
        var mapper = session.getMapper(Mapper.class);
        try {
            var res = mapper.fetchAll();
            if (res == null) {
                return ok(Collections.emptyList());
            }
            return ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.FETCH_SKILL_ERROR);
        }

    }

    public interface Mapper {
        @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        @Insert("INSERT into skill(name) VALUES(#{name})")
        Integer insert(Skill skill);

        @Select("select id,name from skill")
        List<Skill> fetchAll();
    }
}
