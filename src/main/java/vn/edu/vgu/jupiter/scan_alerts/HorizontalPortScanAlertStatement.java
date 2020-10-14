package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

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

        HorizontalPortScanAlertUtil.compileDeploy(stmt, runtime, opts);
        HorizontalPortScanAlertUtil.compileDeploy(listenStmt, runtime)
                .addListener(new HorizontalPortScanAlertListener());
    }
}
