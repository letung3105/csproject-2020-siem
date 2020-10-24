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
    private static final String eplRaiseAlert =
            "insert into VerticalPortScanAlert\n" +
                    "select timestamp, ipHeader.dstAddr, count(distinct tcpHeader.dstPort)\n" +
                    "from TcpPacketWithClosedPort#time(?:timeWindow:integer seconds)\n" +
                    "group by ipHeader.dstAddr\n" +
                    "having count(distinct tcpHeader.dstPort) >= ?:minConnectionsCount:integer\n" +
                    "output first every ?:alertInterval:integer seconds";
    private static final String eplListen = "select * from VerticalPortScanAlert";

    private EPRuntime runtime;
    private EPStatement stmtRaiseAlert;
    private EPStatement stmtListen;

    public VerticalPortScanAlertStatement(EPRuntime runtime,
                                          int minConnectionsCount,
                                          int timeWindow,
                                          int alertInterval,
                                          int countThreshold) {
        this.runtime = runtime;

        DeploymentOptions alertOpts = new DeploymentOptions();
        alertOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minConnectionsCount", minConnectionsCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );
        stmtRaiseAlert = PortScansAlertUtil.compileDeploy(eplRaiseAlert, runtime, alertOpts);

        stmtListen = PortScansAlertUtil.compileDeploy(eplListen, runtime);
        stmtListen.addListener(new VerticalPortScanAlertListener(countThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(stmtRaiseAlert.getDeploymentId());
        runtime.getDeploymentService().undeploy(stmtListen.getDeploymentId());
    }
}
