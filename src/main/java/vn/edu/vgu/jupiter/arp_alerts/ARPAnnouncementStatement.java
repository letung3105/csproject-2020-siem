package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPAnnouncementStatement {
    String statement = "insert into ARPAnnouncementEvent\n " +
            "select srcIP, destIP, srcMAC, destMAC, time from ARPPacketEvent as a\n " +
            "where a.announcement";

    private String listenStatement = "select * from ARPAnnouncementEvent";

    public ARPAnnouncementStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
    }
}
