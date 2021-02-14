package myprofile.profile.impl.dao;

import myprofile.common.model.JobSync;
import myprofile.common.result.Result;
import myprofile.profile.dao.JobSyncDAO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;

import java.util.Optional;

import static myprofile.common.result.Result.ok;

public class JobSyncDAOImpl implements JobSyncDAO {
    @Override
    public Result<Optional<JobSync>> fetch(SqlSession session) {
        var jobSyncMapper = session.getMapper(Mapper.class);
        var record = jobSyncMapper.select();
        if (record != null) {
            return ok(Optional.of(record));
        }
        return ok(Optional.empty());
    }

    public static interface Mapper {
        @Select("SELECT date,lastPosition,total FROM jobsync")
        JobSync select();
    }
}
