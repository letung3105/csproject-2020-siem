package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.EventSender;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.common.client.module.ParseException;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.EPDeployException;
import com.espertech.esper.runtime.client.EPDeployment;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;

import java.io.IOException;

public class VerticalPortScan {
    private static final String eplFileLocation = "vertical_port_scan.epl";
    private static final String epRuntimeId = "VerticalPortScan";
    private EPRuntime epRuntime;

    public VerticalPortScan() throws IOException, EPCompileException, EPDeployException, ParseException {
        // configuration
        Configuration config = new Configuration();
        config.getCommon().addEventType(TcpPacketEvent.class);
        config.getCommon().addEventType(TcpPacketWithClosedPortEvent.class);
        config.getCommon().addEventType(VerticalPortScanAlert.class);
        config.getCompiler().getByteCode().setAllowSubscriber(true);

        // compile the module
        EPCompiler compiler = EPCompilerProvider.getCompiler();
        EPCompiled verticalPortScanModule = compiler.compile(
                compiler.readModule(eplFileLocation, getClass().getClassLoader()),
                new CompilerArguments(config)
        );

        // modify statement parameter
        // DeploymentOptions deploymentOptions =
        //         new DeploymentOptions();
        // deploymentOptions.setStatementSubstitutionParameter(prepared ->
        //         prepared.setObject());

        epRuntime = EPRuntimeProvider.getRuntime(epRuntimeId, config);
        EPDeployment verticalPortScanDeployment = epRuntime.getDeploymentService().deploy(verticalPortScanModule);
        // set subscriber
        epRuntime.getDeploymentService()
                .getStatement(
                        verticalPortScanDeployment.getDeploymentId(),
                        TcpPacketWithClosedPortEvent.class.getSimpleName())
                .setSubscriber(new TcpPacketWithClosedPortEventSubscriber());
        epRuntime.getDeploymentService()
                .getStatement(
                        verticalPortScanDeployment.getDeploymentId(),
                        VerticalPortScanAlert.class.getSimpleName())
                .setSubscriber(new VerticalPortScanAlertSubscriber());
    }

    public EventSender getRawEventSender() {
        return epRuntime.getEventService().getEventSender(TcpPacketEvent.class.getSimpleName());
    }
}
