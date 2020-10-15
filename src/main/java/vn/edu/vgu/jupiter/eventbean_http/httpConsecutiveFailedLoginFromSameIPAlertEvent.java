package vn.edu.vgu.jupiter.eventbean_http;

public class httpConsecutiveFailedLoginFromSameIPAlertEvent {
    String IPAddress;
    String userID;
    String time;

    public String getIPAddress() {
        return IPAddress;
    }

    public String getUserID() {
        return userID;
    }

    public String getTime() {
        return time;
    }

    public httpConsecutiveFailedLoginFromSameIPAlertEvent(String IPAddress, String userID, String time) {
        this.IPAddress = IPAddress;
        this.userID = userID;
        this.time = time;
    }
}
