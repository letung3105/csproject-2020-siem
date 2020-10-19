package vn.edu.vgu.jupiter.eventbean_http;

public class httpFileTooLargeFromSameIPAlertEvent {
    String IPAddress;
    String time;
    String userID;
    String returnObjSize;
    long failuresCount;

    public httpFileTooLargeFromSameIPAlertEvent(String IPAddress, String time, String userID,
                                                String returnObjSize, int failuresCount) {
        this.IPAddress = IPAddress;
        this.time = time;
        this.userID = userID;
        this.returnObjSize = returnObjSize;
        this.failuresCount = failuresCount;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public String getTime() {
        return time;
    }

    public String getUserID() {
        return userID;
    }

    public String getReturnObjSize() {
        return returnObjSize;
    }

    public long getFailuresCount() {
        return failuresCount;
    }
}
