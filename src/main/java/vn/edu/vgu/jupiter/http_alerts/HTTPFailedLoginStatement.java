package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

/**
 * This class compile the EPL statement to select failed authentication attempts
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class HTTPFailedLoginStatement {
    private static final String statementEPL =
            "@Name('HTTPFailedLogin')\n" +
                    "insert into HTTPFailedLogin\n " +
                    "select IPAddress, userID, time, timeZone from HTTPLog\n " +
                    "where statusCode like \"401\"";

    private EPRuntime runtime;
    private EPStatement statement;

    public HTTPFailedLoginStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = CEPSetupUtil.compileDeploy(statementEPL, runtime);
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
