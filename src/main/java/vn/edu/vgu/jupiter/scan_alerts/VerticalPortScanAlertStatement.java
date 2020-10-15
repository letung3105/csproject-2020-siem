package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement for raising alerts for vertical port scan events that might be
 * happening. An event listener is also attached to log the alert messages to the user.
 *
 * @author Tung Le Vo
 */
public class VerticalPortScanAlertStatement {
    private static final String alertStmt = "insert into VerticalPortScanAlert\n" +
            "select timestamp, ipHeader.dstAddr\n" +
            "from TcpPacketWithClosedPortEvent#time(?:timeWindow:integer seconds)\n" +
            "group by ipHeader.dstAddr\n" +
            "having count(distinct tcpHeader.dstPort) >= ?:minConnectionsCount:integer\n" +
            "output first every ?:alertInterval:integer seconds";
    private static final String listenStmt = "select * from VerticalPortScanAlert";

    public VerticalPortScanAlertStatement(EPRuntime runtime, int minConnectionsCount, int timeWindow, int alertInterval) {
        DeploymentOptions alertOpts = new DeploymentOptions();
        alertOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minConnectionsCount", minConnectionsCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );
        PortScansAlertUtil.compileDeploy(alertStmt, runtime, alertOpts);
        PortScansAlertUtil
                .compileDeploy(listenStmt, runtime)
                .addListener(new VerticalPortScanAlertListener());
    }
}
