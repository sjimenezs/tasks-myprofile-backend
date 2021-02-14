package myprofile.profile.impl.dao;

import myprofile.common.error.ErrorCodes;
import myprofile.common.model.Job;
import myprofile.common.model.JobSync;
import myprofile.common.result.Result;
import myprofile.profile.dao.JobDAO;
import myprofile.profile.dao.JobSyncDAO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;

public class JobDAOImpl implements JobDAO {
    @Override
    public Result<Boolean> save(SqlSession session, Job job) {
        var mapper = session.getMapper(Mapper.class);
        try {
            mapper.insert(job);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.SAVE_JOB_ERROR);
        }

        return ok(true);
    }

    @Override
    public Result<List<String>> fetchAllIds(SqlSession session) {
        var mapper = session.getMapper(Mapper.class);
        try {
            var res = mapper.fetchAllIds();
            if (res == null) {
                return ok(Collections.emptyList());
            }
            return ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.FETCH_SKILL_ERROR);
        }

    }

    public static interface Mapper {
        @Insert("INSERT into job(id,objective) VALUES(#{id}, #{objective})")
        void insert(Job job);

        @Select("select id from job")
        List<String> fetchAllIds();
    }
}
