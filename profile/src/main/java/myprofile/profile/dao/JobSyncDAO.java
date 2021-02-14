package myprofile.profile.dao;

import myprofile.common.model.Job;
import myprofile.common.model.JobSync;
import myprofile.common.result.Result;
import org.apache.ibatis.session.SqlSession;

import java.util.Optional;

public interface JobSyncDAO {
    Result<Optional<JobSync>> fetch(SqlSession session);
}
