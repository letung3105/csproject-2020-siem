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
public class PortScanAlertStatement {
    private final String stmtVerti =
            "insert into VerticalPortScanAlert\n" +
                    "select ipHeader.dstAddr\n" +
                    "from TcpPacketWithClosedPortEvent#time_batch(?:alertTimeWindow:integer second)\n" +
                    "group by ipHeader.dstAddr\n" +
                    "having count(*) > ?:alertInvalidAccessLowerBound:integer";

    private final String listenStmtVerti = "select * from VerticalPortScanAlert";

    private final String stmtHori =
            "insert into HorizontalPortScanAlert\n" +
                    "select tcpHeader.dstPort\n" +
                    "from TcpPacketWithClosedPortEvent#time_batch(?:alertTimeWindow:integer second)\n" +
                    "group by tcpHeader.dstPort\n" +
                    "having count(*) > ?:alertInvalidAccessLowerBound:integer";

    private final String listenStmtHori = "select * from HorizontalPortScanAlert";

    public PortScanAlertStatement(EPRuntime runtime, int minFailedConnectionCount, int timeWindowSeconds) {
        DeploymentOptions opts = new DeploymentOptions();
        opts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("alertInvalidAccessLowerBound", minFailedConnectionCount);
                    TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
                    prepared.setObject("alertTimeWindow", ts.getSeconds());
                }
        );

        PortScanAlertUtil.compileDeploy(stmtVerti, runtime, opts);
        PortScanAlertUtil.compileDeploy(listenStmtVerti, runtime)
                .addListener(new VerticalPortScanAlertListener());

        PortScanAlertUtil.compileDeploy(stmtHori, runtime, opts);
        PortScanAlertUtil.compileDeploy(listenStmtHori, runtime)
                .addListener(new HorizontalPortScanAlertListener());
    }
}
