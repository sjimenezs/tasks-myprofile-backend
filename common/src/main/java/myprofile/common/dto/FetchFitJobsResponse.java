package myprofile.common.dto;

import java.util.List;

public class FetchFitJobsResponse {
    List<MatchedJob> jobsFit;

    public FetchFitJobsResponse(List<MatchedJob> jobsFit) {
        this.jobsFit = jobsFit;
    }

    public List<MatchedJob> getJobsFit() {
        return jobsFit;
    }

    public void setJobsFit(List<MatchedJob> jobsFit) {
        this.jobsFit = jobsFit;
    }
}
