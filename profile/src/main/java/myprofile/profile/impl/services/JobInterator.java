package myprofile.profile.impl.services;

import myprofile.common.model.Job;
import myprofile.common.result.Result;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class JobInterator implements Iterator<Job> {
    private int offset;
    private final Function<Integer, Result<List<Job>>> provider;
    private Iterator<Job> jobs;
    private boolean isMoreJobsAvailable = true;

    public JobInterator(Function<Integer, Result<List<Job>>> provider, int offset) {
        this.provider = provider;
        this.offset = offset;
    }

    private void fetchJobs() {
        if (this.isMoreJobsAvailable) {
            var resultFetchJobs = provider.apply(this.offset);
            if (resultFetchJobs.isError()) {
                return;
            }
            var jobs = resultFetchJobs.getOk();
            if (jobs.size() < 500) {
                this.isMoreJobsAvailable = false;
            }
            this.offset += jobs.size();
            this.jobs = resultFetchJobs.getOk().iterator();
        }
    }

    @Override
    public boolean hasNext() {
        if (jobs == null|| !jobs.hasNext()) {
            this.fetchJobs();
        }
        return (this.jobs != null && this.jobs.hasNext());
    }

    @Override
    public Job next() {
        return this.jobs.next();
    }
}