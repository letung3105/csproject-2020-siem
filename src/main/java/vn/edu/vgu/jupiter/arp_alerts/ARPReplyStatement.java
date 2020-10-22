package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

public class ARPReplyStatement {
    String statementEPL = "insert into ARPReplyEvent\n " +
            "select srcIP, destIP, srcMAC, destMAC, time from ARPPacketEvent\n " +
            "where reply";

    private String listenStatement = "select * from ARPReplyEvent";
    private EPStatement statement;

    private EPRuntime runtime;

    public ARPReplyStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = ARPAlertUtils.compileDeploy(statementEPL, runtime);
    }
    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
