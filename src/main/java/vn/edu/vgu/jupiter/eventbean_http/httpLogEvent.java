import java.util.ArrayList;

public class httpLogEvent {
    String IPAddress;
    String identd;
    String userID;
    String time;
    String timeZone;
    String protocol;
    String statusCode;
    String returnObjSize;
    String referer;
    String clientBrowser;

    public String getIPAddress() {
        return IPAddress;
    }

    public String getIdentd() {
        return identd;
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

    public String getProtocol() {
        return protocol;
    }

    public String getReturnObjSize() {
        return returnObjSize;
    }

    public String getReferer() {
        return referer;
    }

    public String getClientBrowser() {
        return clientBrowser;
    }



    public httpLogEvent(ArrayList<String> splittedLog) {
        IPAddress = splittedLog.get(0);
        identd = splittedLog.get(1);
        userID = splittedLog.get(2);
        time = splittedLog.get(3);
        timeZone = splittedLog.get(4);
        protocol = splittedLog.get(5);
        statusCode = splittedLog.get(6);
        returnObjSize = splittedLog.get(7);
        referer = splittedLog.get(8);
        clientBrowser = splittedLog.get(9);
    }

    public String getStatusCode() {
        return statusCode;
    }
}
