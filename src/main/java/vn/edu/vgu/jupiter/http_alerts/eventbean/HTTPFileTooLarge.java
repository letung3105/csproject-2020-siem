package vn.edu.vgu.jupiter.http_alerts.eventbean;

public class HTTPFileTooLarge {
    String IPAddress;
    String userID;
    String time;
    String timeZone;

    public String getIPAddress() {
        return IPAddress;
    }

    public String getTime() {
        return time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getUserID() {
        return userID;
    }


    public HTTPFileTooLarge(String IPAddress, String userID, String time, String timeZone) {
        this.IPAddress = IPAddress;
        this.userID = userID;
        this.time = time;
        this.timeZone = timeZone;
    }
}
