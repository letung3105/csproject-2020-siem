package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

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

        PortScanAlertUtil.compileDeploy(stmt, runtime, opts);
        PortScanAlertUtil.compileDeploy(listenStmt, runtime)
                .addListener(new VerticalPortScanAlertListener());
    }
}
