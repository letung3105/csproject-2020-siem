package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement to select failed authentication attempts
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class HTTPFailedLoginEventStatement {
    String statement = "insert into HTTPFailedLoginEvent\n " +
            "select IPAddress, userID, time, timeZone from HTTPLogEvent\n " +
            "where statusCode like \"401\"";

    public HTTPFailedLoginEventStatement(EPRuntime runtime) {
        CEPSetupUtil.compileDeploy(statement, runtime);
    }
}
