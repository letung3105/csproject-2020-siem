package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

/**
 * This class compile the EPL statement which simulates an ARP cache table,
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class ARPCacheUpdateStatement {
    private String statementEPL = "insert into ARPCacheUpdateEvent\n " +
            "select srcIP, srcMAC, time from ARPReplyEvent;\n " +

            "insert into ARPCacheUpdateEvent\n " +
            "select destIP, destMAC, time from ARPReplyEvent;\n " +

            "insert into ARPCacheUpdateEvent\n " +
            "select srcIP, srcMAC, time from ARPAnnouncementEvent;\n " +

            "insert into ARPCacheUpdateEvent\n " +
            "select destIP, destMAC, time from ARPAnnouncementEvent";
    private String listenStatementEPL = "select * from ARPCacheUpdateEvent";

    private EPStatement statement;

    private EPRuntime runtime;

    public ARPCacheUpdateStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = ARPAlertUtils.compileDeploy(statementEPL, runtime);
    }
    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
