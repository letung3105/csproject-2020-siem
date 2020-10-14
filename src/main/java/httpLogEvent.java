import java.util.ArrayList;

public class httpLogEvent {
    String IPAdress;
    String identd;
    String userID;
    String time;
    String timeZone;
    String protocol;
    String statusCode;
    String returnObjSize;
    String referer;
    String clientBrowser;
    public httpLogEvent (ArrayList<String> splittedLog) {
        IPAdress        = splittedLog.get(0);
        identd          = splittedLog.get(1);
        userID          = splittedLog.get(2);
        time            = splittedLog.get(3);
        timeZone        = splittedLog.get(4);
        protocol        = splittedLog.get(5);
        statusCode      = splittedLog.get(6);
        returnObjSize   = splittedLog.get(7);
        referer         = splittedLog.get(8);
        clientBrowser   = splittedLog.get(9);

    }

    public String getStatusCode() {
        return statusCode;
    }


}
