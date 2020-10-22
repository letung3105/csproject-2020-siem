package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

/**
 * This class compile the EPL statement for raising alerts for block port scan events that might be
 * happening. An event listener is also attached to log the alert messages to the user.
 *
 * @author Vo Le Tung
 */
public class BlockPortScanAlertStatement {
    private static final String portsCountStmt =
            "insert into ClosedPortsCountPerAddress\n" +
                    "select timestamp, ipHeader.dstAddr, count(distinct tcpHeader.dstPort)\n" +
                    "from TcpPacketWithClosedPortEvent#time(?:timeWindow:integer seconds)\n" +
                    "group by ipHeader.dstAddr\n";
    private static final String alertStmt =
            "insert into BlockPortScanAlert\n" +
                    "select timestamp, count(distinct addr)\n" +
                    "from ClosedPortsCountPerAddress#time(?:timeWindow:integer seconds)\n" +
                    "where portsCount >= ?:minPortsCount:integer\n" +
                    "having count(distinct addr) >= ?:minAddressesCount:integer\n" +
                    "output first every ?:alertInterval:integer seconds";

    private EPStatement portsCountStatement;
    private EPStatement statement;
    private EPStatement listenStatement;

    private EPRuntime runtime;

    public BlockPortScanAlertStatement(EPRuntime runtime, int minPortsCount, int minAddressesCount, int timeWindow, int alertInterval, int countThreshold) {
        this.runtime = runtime;

        DeploymentOptions portsCountOpts = new DeploymentOptions();
        portsCountOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("timeWindow", timeWindow);
                }
        );
        portsCountStatement = PortScansAlertUtil.compileDeploy(portsCountStmt, runtime, portsCountOpts);

        DeploymentOptions alertOpts = new DeploymentOptions();
        alertOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minPortsCount", minPortsCount);
                    prepared.setObject("minAddressesCount", minAddressesCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );
        statement = PortScansAlertUtil.compileDeploy(alertStmt, runtime, alertOpts);
        listenStatement = PortScansAlertUtil.compileDeploy("select * from BlockPortScanAlert", runtime);
        listenStatement.addListener(new BlockPortScanAlertListener(countThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(portsCountStatement.getDeploymentId());
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
