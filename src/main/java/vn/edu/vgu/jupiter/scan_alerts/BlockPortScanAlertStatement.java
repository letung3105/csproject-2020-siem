package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockPortScanAlertStatement {
    private final Logger logger = LoggerFactory.getLogger(BlockPortScanAlertStatement.class);

    public BlockPortScanAlertStatement(EPRuntime runtime, int minPortsCount, int minAddressesCount, int timeWindow, int alertInterval) {
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
                        "from ClosedPortsCount#time(?:timeWindow:integer seconds)\n" +
                        "where portsCount >= ?:minPortsCount:integer\n" +
                        "having count(distinct addr) >= ?:minAddressesCount:integer\n" +
                        "output first every ?:alertInterval:integer seconds",
                runtime, alertOpts);
        PortScansAlertUtil.compileDeploy("select * from BlockPortScanAlert", runtime)
                .addListener(new BlockPortScanAlertListener());
    }
}
