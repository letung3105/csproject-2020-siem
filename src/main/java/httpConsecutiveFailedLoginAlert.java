/**
 * Store webserver failed log into POJO
 * @author Bui Xuan Phuoc
 */
public class httpConsecutiveFailedLoginAlert {
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

    public httpConsecutiveFailedLoginAlert(String IPAddress, String userID, String time) {
        this.IPAddress = IPAddress;
        this.userID = userID;
        this.time = time;
    }
}
