package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * A class to compile EPL statements for the HorizontalPortScanAlert event
 * A listener is attached to log message
 *
 * @author Pham Nguyen Thanh An
 */
public class HorizontalPortScanAlertStatement {
    private static final String alertStmt =
            "insert into HorizontalPortScanAlert\n" +
                    "select timestamp, tcpHeader.dstPort\n" +
                    "from TcpPacketWithClosedPortEvent#time(?:timeWindow:integer second)\n" +
                    "group by tcpHeader.dstPort\n" +
                    "having count(distinct ipHeader.dstAddr) >= ?:minConnectionsCount:integer\n" +
                    "output first every ?:alertInterval:integer second";

    private static final String listenStmt = "select * from HorizontalPortScanAlert";

    public HorizontalPortScanAlertStatement(EPRuntime runtime, int minConnectionsCount, int timeWindow, int alertInterval, int countThreshold) {
        DeploymentOptions opts = new DeploymentOptions();
        opts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minConnectionsCount", minConnectionsCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );

        PortScansAlertUtil.compileDeploy(alertStmt, runtime, opts);
        PortScansAlertUtil.compileDeploy(listenStmt, runtime)
                .addListener(new HorizontalPortScanAlertListener(countThreshold));
    }
}
