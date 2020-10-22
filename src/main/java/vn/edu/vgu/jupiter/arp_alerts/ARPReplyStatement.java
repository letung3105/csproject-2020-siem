package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import vn.edu.vgu.jupiter.http_alerts.CEPSetupUtil;

public class ARPReplyStatement {
    String statement = "insert into ARPReplyEvent\n " +
            "select srcIP, destIP, srcMAC, destMAC, time from ARPPacketEvent\n " +
            "where isReply";

    private String listenStatement = "select * from ARPReplyEvent";

    public ARPReplyStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
    }
}
