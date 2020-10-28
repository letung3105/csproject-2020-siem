package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.arp_alerts.ARPAlertsConfigurations.getEPConfiguration;

/**
 * This class compile the EPL statement to select ARP reply packets
 *
 * @author Bui Xuan Phuoc
 */
public class ARPReplyStatement {
    String statementEPL =
            "@Name('ARPReplyEvent')\n" +
                    "insert into ARPReplyEvent\n " +
                    "select srcIP, destIP, srcMAC, destMAC, time from ARPPacketEvent\n " +
                    "where reply";

    private String listenStatement = "select * from ARPReplyEvent";
    private EPStatement statement;

    private EPRuntime runtime;

    public ARPReplyStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration());
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
