package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.http_alerts.HTTPAlertsConfigurations.getEPConfiguration;

/**
 * This class compile the EPL statement to select file too large send attempts,
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class HTTPFileTooLargeStatement {
    private static final String statementEPL =
            "@Name('HTTPFileTooLarge')\n" +
                    "insert into HTTPFileTooLarge\n " +
                    "select IPAddress, userID, time, timeZone from HTTPLog\n " +
                    "where statusCode like \"413\"";

    private EPRuntime runtime;
    private EPStatement statement;

    public HTTPFileTooLargeStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration());
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
