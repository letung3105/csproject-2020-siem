package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPCacheFloodAlertStatement {
    String statement = "insert into ARPCacheFloodAlertEvent\n " +
            "select count(*) from ARPReplyStatement NATURAL JOIN ARPAnnouncementStatement\n " +
            "where count(*) > 255";
    private String listenStatement = "select * from ARPReplyEvent";

    public ARPCacheFloodAlertStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
    }
}
