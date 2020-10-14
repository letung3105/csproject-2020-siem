package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class compile the EPL statement for raising alerts for block port scan events that might be
 * happening. An event listener is also attached to log the alert messages to the user.
 *
 * @author Tung Le Vo
 */
public class BlockPortScanAlertStatement {
    private final Logger logger = LoggerFactory.getLogger(BlockPortScanAlertStatement.class);

    public BlockPortScanAlertStatement(EPRuntime runtime, int minPortsCount, int minAddressesCount, int timeWindow, int alertInterval) {
        DeploymentOptions portsCountOpts = new DeploymentOptions();
        portsCountOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("timeWindow", timeWindow);
                }
        );
        PortScansAlertUtil.compileDeploy(
                "insert into ClosedPortsCountPerAddress\n" +
                        "select timestamp, ipHeader.dstAddr, count(distinct tcpHeader.dstPort)\n" +
                        "from TcpPacketWithClosedPortEvent#time(?:timeWindow:integer seconds)\n" +
                        "group by ipHeader.dstAddr\n",
                runtime, portsCountOpts);

        DeploymentOptions alertOpts = new DeploymentOptions();
        alertOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minPortsCount", minPortsCount);
                    prepared.setObject("minAddressesCount", minAddressesCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );
        PortScansAlertUtil.compileDeploy(
                "insert into BlockPortScanAlert\n" +
                        "select timestamp\n" +
                        "from ClosedPortsCountPerAddress#time(?:timeWindow:integer seconds)\n" +
                        "where portsCount >= ?:minPortsCount:integer\n" +
                        "having count(distinct addr) >= ?:minAddressesCount:integer\n" +
                        "output first every ?:alertInterval:integer seconds",
                runtime, alertOpts);
        PortScansAlertUtil.compileDeploy("select * from BlockPortScanAlert", runtime)
                .addListener(new BlockPortScanAlertListener());
    }
}
