package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

/**
 * This class compile the EPL statement to select ARP announcements packets
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class ARPAnnouncementStatement {
    String statementEPL = "insert into ARPAnnouncementEvent\n " +
            "select srcIP, destIP, srcMAC, destMAC, time from ARPPacketEvent as a\n " +
            "where a.announcement";

    private String listenStatement = "select * from ARPAnnouncementEvent";
    private EPStatement statement;

    private EPRuntime runtime;

    public ARPAnnouncementStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = ARPAlertUtils.compileDeploy(statementEPL, runtime);
    }
    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
