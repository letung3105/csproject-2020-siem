package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement for raising alerts for vertical port scan events that might be
 * happening. An event listener is also attached to log the alert messages to the user.
 *
 * @author Tung Le Vo
 */
public class VerticalPortScanAlertStatement {
    private final String stmt =
            "insert into VerticalPortScanAlert\n" +
                    "select ipHeader.dstAddr\n" +
                    "from TcpPacketWithClosedPortEvent#time_batch(?:alertTimeWindow:integer second)\n" +
                    "group by ipHeader.dstAddr\n" +
                    "having count(*) > ?:alertInvalidAccessLowerBound:integer";

    private final String listenStmt = "select * from VerticalPortScanAlert";

    public VerticalPortScanAlertStatement(EPRuntime runtime, int minFailedConnectionCount, int timeWindowSeconds) {
        DeploymentOptions opts = new DeploymentOptions();
        opts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("alertInvalidAccessLowerBound", minFailedConnectionCount);
                    TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
                    prepared.setObject("alertTimeWindow", ts.getSeconds());
                }
        );

        VerticalPortScanAlertUtil.compileDeploy(stmt, runtime, opts);
        VerticalPortScanAlertUtil.compileDeploy(listenStmt, runtime)
                .addListener(new VerticalPortScanAlertListener());
    }
}
