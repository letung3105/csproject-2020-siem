package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPDuplicateIPAlertStatement {
    String statement = "insert into ARPDuplicateIPAlertEvent\n " +
            "select IP, STUFF ((" +
            "SELECT ', ' + [Name]) from ARPReplyStatement NATURAL JOIN ARPAnnouncementStatement\n " +
            "WHERE (IP = Results.IP),1,2,'') as MACs, time\n " +
            "from ARPReplyStatement NATURAL JOIN ARPAnnouncementStatement" +
            "group by IP" +
            "having count(*) > 1";
    private String listenStatement = "select * from ARPDuplicateIPAlertEvent";

    public ARPDuplicateIPAlertStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
    }
}
