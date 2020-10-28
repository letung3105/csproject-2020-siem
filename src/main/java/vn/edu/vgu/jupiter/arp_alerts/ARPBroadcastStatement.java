package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.arp_alerts.ARPAlertsConfigurations.getEPConfiguration;

public class ARPBroadcastStatement {
    String statementEPL =
            "@Name('ARPBroadcastEvent')\n" +
                    "insert into ARPBroadcastEvent\n " +
                    "select srcIP, destIP, srcMAC, time from ARPPacketEvent\n " +
                    "where destMAC like '00:00:00:00:00:00'";

    private String listenStatementEPL = "select * from ARPBroadcastEvent";
    private EPStatement statement;

    private EPRuntime runtime;

    public ARPBroadcastStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration());
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
