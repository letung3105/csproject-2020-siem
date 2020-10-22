package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPCacheUpdateStatement {
    String statement = "insert into ARPCacheUpdateEvent\n " +
            "select srcIP, destIP, srcMAC, destMAC, time from ARPReplyEvent;\n " +
            "insert into ARPCacheUpdateEvent\n " +
            "select srcIP, destIP, srcMAC, destMAC, time from ARPAnnouncementEvent";
    private String listenStatement = "select * from ARPCacheUpdateEvent";

    public ARPCacheUpdateStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
    }
}
