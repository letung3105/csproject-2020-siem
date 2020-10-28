package vn.edu.vgu.jupiter;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;

public class EPFacade {
    /**
     * Compile and deploy the compiled statement with the given deployment options
     *
     * @param eplString  The epl statement
     * @param epRuntime  The runtime to deploy to
     * @param epConfig   The configuration for deploying and compiling with the given runtime
     * @param deployOpts deployment options
     * @return the statement that was deployed
     */
    public static EPStatement compileDeploy(String eplString,
                                            EPRuntime epRuntime,
                                            Configuration epConfig,
                                            DeploymentOptions deployOpts) {
        try {
            CompilerArguments args = new CompilerArguments(epConfig);
            args.getPath().add(epRuntime.getRuntimePath());
            EPCompiled compiled = EPCompilerProvider.getCompiler().compile(eplString, args);
            return epRuntime.getDeploymentService().deploy(compiled, deployOpts).getStatements()[0];
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Compile and deploy the compiled statement
     *
     * @param eplString The EPL statement string
     * @param epRuntime The runtime to deploy to
     * @param epConfig  Configuration for deploying and compiling with the given runtime
     * @return the statement that was deployed
     */
    public static EPStatement compileDeploy(String eplString, EPRuntime epRuntime, Configuration epConfig) {
        try {
            CompilerArguments args = new CompilerArguments(epConfig);
            args.getPath().add(epRuntime.getRuntimePath());
            EPCompiled compiled = EPCompilerProvider.getCompiler().compile(eplString, args);
            return epRuntime.getDeploymentService().deploy(compiled).getStatements()[0];
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}