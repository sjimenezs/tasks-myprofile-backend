package myprofile.profile.dao;

import myprofile.common.model.Job;
import myprofile.common.model.User;
import myprofile.common.result.Result;

import java.util.List;
import java.util.Optional;

public interface TorreJobDAO {
    Result<List<Job>> fetchJobs(int size, int offset);

    Result<Integer> fetchJobsCount();
}
