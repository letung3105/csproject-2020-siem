import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
<<<<<<< HEAD:src/main/java/ConsecutiveFailedLoginsUtil.java


/**
 * this is the helper for compiling and setting configuration
 * @author Bui Xuan Phuoc
 */
public class ConsecutiveFailedLoginsUtil {
=======
import vn.edu.vgu.jupiter.eventbean.*;

/**
 * Utility functions for compiling and deploying statements.
 *
 * @author Vo Le Tung
 * @author Pham Nguyen Thanh An
 */
public class PortScansAlertUtil {

>>>>>>> master:src/main/java/PortScansAlertUtil.java
    /**
     * Compile and deploy EPL statements
     * @param epl
     * @param runtime
     * @param deployOpts
     * @return deployedStatement
     */
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

    /**
     * Compile and deploy EPL statements
     * @param epl
     * @param runtime
     * @return deployedStatement
     */
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

    /**
     * Get configure
     * @return
     */

    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
<<<<<<< HEAD:src/main/java/ConsecutiveFailedLoginsUtil.java
        configuration.getCommon().addEventType(httpLogEvent.class);
        configuration.getCommon().addEventType(httpFailedLogin.class);
        configuration.getCommon().addEventType(httpConsecutiveFailedLoginAlert.class);
=======
        configuration.getCommon().addEventType(TcpPacketEvent.class);
        configuration.getCommon().addEventType(TcpPacketWithClosedPortEvent.class);
        configuration.getCommon().addEventType(ClosedPortsCountPerAddress.class);
        configuration.getCommon().addEventType(VerticalPortScanAlert.class);
        configuration.getCommon().addEventType(HorizontalPortScanAlert.class);
        configuration.getCommon().addEventType(BlockPortScanAlert.class);
        configuration.getRuntime().getLogging().setEnableExecutionDebug(false);
        configuration.getRuntime().getLogging().setEnableTimerDebug(false);
>>>>>>> master:src/main/java/PortScansAlertUtil.java
        return configuration;
    }
}
