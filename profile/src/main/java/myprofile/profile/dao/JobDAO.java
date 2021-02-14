package myprofile.profile.dao;

import myprofile.common.model.Job;
import myprofile.common.result.Result;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public interface JobDAO {
    Result<Boolean> save(SqlSession session, Job job);

    Result<List<String>> fetchAllIds(SqlSession session);
}
