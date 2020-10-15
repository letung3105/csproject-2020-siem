package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * A class to compile EPL statements for the HorizontalPortScanAlert event
 * A listener is attached to log message
 *
 * @author Pham Nguyen Thanh An
 */
public class HorizontalPortScanAlertStatement {
    private final String stmt =
            "insert into HorizontalPortScanAlert\n" +
                    "select tcpHeader.dstPort\n" +
                    "from TcpPacketWithClosedPortEvent#time_batch(?:alertTimeWindow:integer second)\n" +
                    "group by tcpHeader.dstPort\n" +
                    "having count(*) > ?:alertInvalidAccessLowerBound:integer";

    private final String listenStmt = "select * from HorizontalPortScanAlert";

    public HorizontalPortScanAlertStatement(EPRuntime runtime, int minFailedConnectionCount, int timeWindowSeconds) {
        DeploymentOptions opts = new DeploymentOptions();
        opts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("alertInvalidAccessLowerBound", minFailedConnectionCount);
                    TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
                    prepared.setObject("alertTimeWindow", ts.getSeconds());
                }
        );

        PortScanAlertUtil.compileDeploy(stmt, runtime, opts);
        PortScanAlertUtil.compileDeploy(listenStmt, runtime)
                .addListener(new HorizontalPortScanAlertListener());
    }
}
