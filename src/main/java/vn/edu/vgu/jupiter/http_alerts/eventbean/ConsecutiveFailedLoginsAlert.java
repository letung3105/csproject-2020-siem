package vn.edu.vgu.jupiter.http_alerts.eventbean;

public class ConsecutiveFailedLoginsAlert {
    String time;
    String timeZone;
    long failuresCount;

    public ConsecutiveFailedLoginsAlert(String time, String timeZone, long failuresCount) {
        this.time = time;
        this.timeZone = timeZone;
        this.failuresCount = failuresCount;
    }

    public String getTime() {
        return time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public long getFailuresCount() {
        return failuresCount;
    }
}
