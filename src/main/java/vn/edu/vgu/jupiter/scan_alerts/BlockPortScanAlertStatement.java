package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

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

    private String deployPortCountID;
    private String deployAlertID;
    private String deployListenID;

    public BlockPortScanAlertStatement(EPRuntime runtime, int minPortsCount, int minAddressesCount, int timeWindow, int alertInterval, int countThreshold) {
        DeploymentOptions portsCountOpts = new DeploymentOptions();
        portsCountOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("timeWindow", timeWindow);
                }
        );
        PortScansAlertUtil.compileDeploy(portsCountStmt, runtime, portsCountOpts);
        deployPortCountID = PortScansAlertUtil.getCurrentID();

        DeploymentOptions alertOpts = new DeploymentOptions();
        alertOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minPortsCount", minPortsCount);
                    prepared.setObject("minAddressesCount", minAddressesCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );
        PortScansAlertUtil.compileDeploy(alertStmt, runtime, alertOpts);
        deployAlertID = PortScansAlertUtil.getCurrentID();
        PortScansAlertUtil
                .compileDeploy("select * from BlockPortScanAlert", runtime)
                .addListener(new BlockPortScanAlertListener(countThreshold));
        deployListenID = PortScansAlertUtil.getCurrentID();
    }

    public String getDeployAlertID() { return deployAlertID; }
    public String getDeployListenID() { return deployListenID; }
    public String getDeployPortCountID() { return deployPortCountID; }
}
