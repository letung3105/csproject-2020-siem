package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPCacheUpdateStatement {
    String statement = "insert into ARPCacheUpdateEvent\n " +
            "select srcIP, srcMAC, time from ARPReplyEvent;\n " +
            "insert into ARPCacheUpdateEvent\n " +
            "select destIP, destMAC, time from ARPReplyEvent;\n " +
            "insert into ARPCacheUpdateEvent\n " +
            "select srcIP, srcMAC, time from ARPAnnouncementEvent;\n " +
            "insert into ARPCacheUpdateEvent\n " +
            "select destIP, destMAC, time from ARPAnnouncementEvent";
    private String listenStatement = "select * from ARPCacheUpdateEvent";

    public ARPCacheUpdateStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
    }
}
