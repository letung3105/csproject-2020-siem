public class httpFileTooLargeFromSameIPAlertEvent {
    String IPAddress;
    String time;
    String userID;
    String returnObjSize;

    public httpFileTooLargeFromSameIPAlertEvent(String IPAddress, String time, String userID, String returnObjSize) {
        this.IPAddress = IPAddress;
        this.time = time;
        this.userID = userID;
        this.returnObjSize = returnObjSize;
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
}
