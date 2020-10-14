package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import vn.edu.vgu.jupiter.eventbean.HorizontalPortScanAlert;
import vn.edu.vgu.jupiter.eventbean.TcpPacketEvent;
import vn.edu.vgu.jupiter.eventbean.TcpPacketWithClosedPortEvent;

public class HorizontalPortScanAlertUtil {
    protected static EPStatement compileDeploy(String epl, EPRuntime runtime, DeploymentOptions deployOpts) {
        try {
            CompilerArguments args = new CompilerArguments(getConfiguration());
            args.getPath().add(runtime.getRuntimePath());
            EPCompiled compiled = EPCompilerProvider.getCompiler().compile(epl, args);
            return runtime.getDeploymentService().deploy(compiled, deployOpts).getStatements()[0];
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected static EPStatement compileDeploy(String epl, EPRuntime runtime) {
        try {
            CompilerArguments args = new CompilerArguments(getConfiguration());
            args.getPath().add(runtime.getRuntimePath());
            EPCompiled compiled = EPCompilerProvider.getCompiler().compile(epl, args);
            return runtime.getDeploymentService().deploy(compiled).getStatements()[0];
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(TcpPacketEvent.class);
        configuration.getCommon().addEventType(TcpPacketWithClosedPortEvent.class);
        configuration.getCommon().addEventType(HorizontalPortScanAlert.class);
        return configuration;
    }
}
