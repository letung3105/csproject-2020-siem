package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

/**
 * This class compile the EPL statement for raising alerts for vertical port scan events that might be
 * happening. An event listener is also attached to log the alert messages to the user.
 *
 * @author Vo Le Tung
 */
public class VerticalPortScanAlertStatement {
    private static final String alertStmt = "insert into VerticalPortScanAlert\n" +
            "select timestamp, ipHeader.dstAddr, count(distinct tcpHeader.dstPort)\n" +
            "from TcpPacketWithClosedPortEvent#time(?:timeWindow:integer seconds)\n" +
            "group by ipHeader.dstAddr\n" +
            "having count(distinct tcpHeader.dstPort) >= ?:minConnectionsCount:integer\n" +
            "output first every ?:alertInterval:integer seconds";
    private static final String listenStmt = "select * from VerticalPortScanAlert";

    private EPStatement statement;
    private EPStatement listenStatement;

    private EPRuntime runtime;

    public VerticalPortScanAlertStatement(EPRuntime runtime, int minConnectionsCount, int timeWindow, int alertInterval, int countThreshold) {
        DeploymentOptions alertOpts = new DeploymentOptions();
        alertOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minConnectionsCount", minConnectionsCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );
        statement = PortScansAlertUtil.compileDeploy(alertStmt, runtime, alertOpts);
        listenStatement = PortScansAlertUtil.compileDeploy(listenStmt, runtime);
        listenStatement.addListener(new VerticalPortScanAlertListener(countThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
