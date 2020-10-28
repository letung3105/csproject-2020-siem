package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

public class HTTPFileTooLargeEventStatement {
    String statementEPL = "insert into HTTPFileTooLarge\n " +
            "select IPAddress, userID, time, timeZone from HTTPLog\n " +
            "where statusCode like \"413\"";
    private EPStatement statement;

    private EPRuntime runtime;

    public HTTPFileTooLargeEventStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = CEPSetupUtil.compileDeploy(statementEPL, runtime);
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
    }
}
