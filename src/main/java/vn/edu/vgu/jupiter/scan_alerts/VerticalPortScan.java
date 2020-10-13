package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.EventSender;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.common.client.module.ParseException;
import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VerticalPortScan {
    private static final String eplFileLocation = "vertical_port_scan.epl";
    private static final String epRuntimeId = "VerticalPortScan";

    private Configuration config;
    private EPRuntime runtime;
    private Map<String, EPCompiled> compiledItems;
    private Map<String, EPDeployment> deployments;

    public VerticalPortScan() throws IOException, EPCompileException, ParseException {
        // configuration
        config = new Configuration();
        config.getCommon().addEventType(TcpPacketEvent.class);
        config.getCommon().addEventType(TcpPacketWithClosedPortEvent.class);
        config.getCommon().addEventType(VerticalPortScanAlert.class);
        config.getCompiler().getByteCode().setAllowSubscriber(true);
        compiledItems = new HashMap<>();
        deployments = new HashMap<>();

        // compile the module
        EPCompiler compiler = EPCompilerProvider.getCompiler();
        compiledItems.put("AlertModule", compiler.compile(
                compiler.readModule(eplFileLocation, getClass().getClassLoader()),
                new CompilerArguments(config)
        ));
        String stmtAlert = "insert into VerticalPortScanAlert\n" +
                "select ipHeader.dstAddr\n" +
                "from TcpPacketWithClosedPortEvent#time_batch(?:alertTimeWindow:integer)\n" +
                "group by ipHeader.dstAddr\n" +
                "having count(*) > ?:alertInvalidAccessLowerBound:integer";
        compiledItems.put("AlertAggregation", compiler.compile(stmtAlert, new CompilerArguments(config)));
    }

    public void deploy(int minFailedConnectionCount, int timeWindowSeconds) throws EPDeployException {
        // deploy modules
        runtime = EPRuntimeProvider.getRuntime(epRuntimeId, config);
        deployments.put(
                "AlertModule",
                runtime.getDeploymentService().deploy(compiledItems.get("AlertModule")));
        // deploy alerts filter
        DeploymentOptions alertOptions = new DeploymentOptions();
        alertOptions.setStatementSubstitutionParameter(prepared -> {
                    prepared.setObject("alertInvalidAccessLowerBound", minFailedConnectionCount);
                    TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
                    prepared.setObject("alertTimeWindow", ts.getSeconds());
                }
        );
        deployments.put(
                "AlertAggregation",
                runtime.getDeploymentService().deploy(compiledItems.get("AlertAggregation"), alertOptions));

        runtime.getDeploymentService()
                .getStatement(
                        deployments.get("AlertModule").getDeploymentId(),
                        VerticalPortScanAlert.class.getSimpleName())
                .setSubscriber(new VerticalPortScanAlertSubscriber());
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeployAll();
        deployments.clear();
    }

    public EventSender getRawEventSender() {
        return runtime.getEventService().getEventSender(TcpPacketEvent.class.getSimpleName());
    }
}
