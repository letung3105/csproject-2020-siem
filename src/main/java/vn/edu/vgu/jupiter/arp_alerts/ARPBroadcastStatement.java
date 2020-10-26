package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

public class ARPBroadcastStatement {
    String statementEPL = "insert into ARPBroadcastEvent\n " +
            "select srcIP, destIP, srcMAC, time from ARPPacketEvent\n " +
            "where destMAC like '00:00:00:00:00:00'";

    private String listenStatementEPL = "select * from ARPBroadcastEvent";
    private EPStatement statement;

    private EPRuntime runtime;

    public ARPBroadcastStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = ARPAlertUtils.compileDeploy(statementEPL, runtime);
    }
    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
