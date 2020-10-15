package vn.edu.vgu.jupiter.eventbean_http;

public class httpFileTooLargeEvent {
    String IPAddress;
    String time;
    String timeZone;
    String userID;
    String returnObjSize;

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

    public String getReturnObjSize() {
        return returnObjSize;
    }

    public httpFileTooLargeEvent(String IPAddress, String time, String timeZone, String userID, String returnObjSize) {
        this.IPAddress = IPAddress;
        this.time = time;
        this.timeZone = timeZone;
        this.userID = userID;
        this.returnObjSize = returnObjSize;
    }
}
