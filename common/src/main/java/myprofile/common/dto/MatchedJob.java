package myprofile.common.dto;

public class MatchedJob {
    private Integer percent;
    private Integer count;

    public MatchedJob(int percent, int count) {
        this.percent = percent;
        this.count = count;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
