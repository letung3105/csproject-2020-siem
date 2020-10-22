package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPReplyStatement {
    String statement = "insert into ARPReplyEvent\n " +
            "select srcIP, destIP, srcMAC, destMAC, time from ARPPacketEvent\n " +
            "where reply";

    private String listenStatement = "select * from ARPReplyEvent";

    public ARPReplyStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
    }
}
