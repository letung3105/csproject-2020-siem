package vn.edu.vgu.jupiter.eventbean_http;

public class FailedLoginFromSameIPAlert {
    String IPAddress;
    String userID;
    String time;
    long failuresCount;

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

    public FailedLoginFromSameIPAlert(String IPAddress, String userID, String time, long failuresCount) {
        this.IPAddress = IPAddress;
        this.userID = userID;
        this.time = time;
        this.failuresCount = failuresCount;
    }
}
