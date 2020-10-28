package vn.edu.vgu.jupiter.http_alerts.eventbean;

public class ConsecutiveFileTooLargeSameUserIDAlert {
    String IPAddress;
    String userID;
    String time;
    String timeZone;
    long failuresCount;

    public ConsecutiveFileTooLargeSameUserIDAlert(String IPAddress, String userID, String time, String timeZone, long failuresCount) {
        this.IPAddress = IPAddress;
        this.userID = userID;
        this.time = time;
        this.timeZone = timeZone;
        this.failuresCount = failuresCount;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public String getUserID() {
        return userID;
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
