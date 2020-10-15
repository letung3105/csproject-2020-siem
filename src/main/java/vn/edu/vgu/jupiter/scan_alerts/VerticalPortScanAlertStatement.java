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
    public VerticalPortScanAlertStatement(EPRuntime runtime, int minFailedConnectionCount, int timeWindowSeconds, int alertIntervalSeconds) {
        DeploymentOptions alertOpts = new DeploymentOptions();
        alertOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("alertInvalidAccessLowerBound", minFailedConnectionCount);
                    TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
                    prepared.setObject("alertTimeWindow", ts.getSeconds());
                }
        );
        VerticalPortScanAlertUtil.compileDeploy(
                "insert into VerticalPortScanAlert\n" +
                        "select ipHeader.dstAddr\n" +
                        "from TcpPacketWithClosedPortEvent#time(?:alertTimeWindow:integer seconds)\n" +
                        "group by ipHeader.dstAddr\n" +
                        "having count(*) > ?:alertInvalidAccessLowerBound:integer",
                runtime, alertOpts);

        DeploymentOptions listenOpts = new DeploymentOptions();
        listenOpts.setStatementSubstitutionParameter(prepared -> {
                    TimePeriod ts = new TimePeriod().sec(alertIntervalSeconds);
                    prepared.setObject("alertInterval", ts.getSeconds());
                }
        );
        VerticalPortScanAlertUtil.compileDeploy(
                "select * from VerticalPortScanAlert output last every ?:alertInterval:integer seconds",
                runtime, listenOpts)
                .addListener(new VerticalPortScanAlertListener());
    }
}
