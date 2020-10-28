package vn.edu.vgu.jupiter.http_alerts.eventbean;

public class ConsecutiveFailedLoginsFromSameIPAlert {
    String IPAddress;
    String userID;
    String time;
    long failuresCount;

    public ConsecutiveFailedLoginsFromSameIPAlert(String IPAddress, String userID, String time, long failuresCount) {
        this.IPAddress = IPAddress;
        this.userID = userID;
        this.time = time;
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

    public long getFailuresCount() {
        return failuresCount;
    }
}
