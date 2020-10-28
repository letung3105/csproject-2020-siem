package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.scan_alerts.PortScansAlertConfigurations.getEPConfiguration;

/**
 * This class compile the EPL statement for raising alerts for block port scan events that might be
 * happening. An event listener is also attached to log the alert messages to the user.
 *
 * @author Vo Le Tung
 */
public class BlockPortScanAlertStatement {

    private static final String eplPortsCountPerAddr =
            "@Name('ClosedPortsCountPerAddress')\n" +
                    "insert into ClosedPortsCountPerAddress\n" +
                    "select timestamp, ipHeader.dstAddr, count(distinct tcpHeader.dstPort)\n" +
                    "from TcpPacketWithClosedPort#time(?:timeWindow:integer seconds)\n" +
                    "group by ipHeader.dstAddr\n";

    private static final String eplRaiseAlert =
            "@Name('BlockPortScanAlert')\n" +
                    "insert into BlockPortScanAlert\n" +
                    "select timestamp, count(distinct addr)\n" +
                    "from ClosedPortsCountPerAddress#time(?:timeWindow:integer seconds)\n" +
                    "where portsCount >= ?:minPortsCount:integer\n" +
                    "having count(distinct addr) >= ?:minAddressesCount:integer\n" +
                    "output first every ?:alertInterval:integer seconds";

    private static final String eplListen = "select * from BlockPortScanAlert";

    private EPRuntime runtime;
    private EPStatement stmtPortsCountPerAddr;
    private EPStatement stmtRaiseAlert;
    private EPStatement stmtListen;

    public BlockPortScanAlertStatement(EPRuntime runtime,
                                       int minPortsCount,
                                       int minAddressesCount,
                                       int timeWindow,
                                       int alertInterval,
                                       int countThreshold) {
        this.runtime = runtime;

        DeploymentOptions portsCountOpts = new DeploymentOptions();
        portsCountOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("timeWindow", timeWindow);
                }
        );
        stmtPortsCountPerAddr = EPFacade.compileDeploy(eplPortsCountPerAddr, runtime, getEPConfiguration(), portsCountOpts);

        DeploymentOptions alertOpts = new DeploymentOptions();
        alertOpts.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("minPortsCount", minPortsCount);
                    prepared.setObject("minAddressesCount", minAddressesCount);
                    prepared.setObject("timeWindow", timeWindow);
                    prepared.setObject("alertInterval", alertInterval);
                }
        );
        stmtRaiseAlert = EPFacade.compileDeploy(eplRaiseAlert, runtime, getEPConfiguration(), alertOpts);

        stmtListen = EPFacade.compileDeploy(eplListen, runtime, getEPConfiguration());
        stmtListen.addListener(new BlockPortScanAlertListener(countThreshold));
    }

    /**
     * Undeploy the set of statements managed by this class
     *
     * @throws EPUndeployException Exception will undeploying the EPL statements
     */
    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(stmtPortsCountPerAddr.getDeploymentId());
        runtime.getDeploymentService().undeploy(stmtRaiseAlert.getDeploymentId());
        runtime.getDeploymentService().undeploy(stmtListen.getDeploymentId());
    }
}
