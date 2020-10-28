package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.scan_alerts.PortScansAlertConfigurations.getEPConfiguration;

/**
 * A class to compile EPL statements for the HorizontalPortScanAlert event
 * A listener is attached to log message
 *
 * @author Pham Nguyen Thanh An
 */
public class HorizontalPortScanAlertStatement {
    private static final String alertStmt =
            "@Name('HorizontalPortScanAlert')\n" +
                    "insert into HorizontalPortScanAlert\n" +
                    "select timestamp, tcpHeader.dstPort, count(distinct ipHeader.dstAddr)\n" +
                    "from TcpPacketWithClosedPort#time(?:timeWindow:integer second)\n" +
                    "group by tcpHeader.dstPort\n" +
                    "having count(distinct ipHeader.dstAddr) >= ?:minConnectionsCount:integer\n" +
                    "output first every ?:alertInterval:integer second";

    private static final String listenStmt = "select * from HorizontalPortScanAlert";

    private EPStatement statement;
    private EPStatement listenStatement;

    private EPRuntime runtime;

    public HorizontalPortScanAlertStatement(EPRuntime runtime, int minConnectionsCount, int timeWindow, int alertInterval, int countThreshold) {
        this.runtime = runtime;

        DeploymentOptions opts = new DeploymentOptions();
        opts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minConnectionsCount", minConnectionsCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );

        statement = EPFacade.compileDeploy(alertStmt, runtime, getEPConfiguration(), opts);
        listenStatement = EPFacade.compileDeploy(listenStmt, runtime, getEPConfiguration());
        listenStatement.addListener(new HorizontalPortScanAlertListener(countThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
