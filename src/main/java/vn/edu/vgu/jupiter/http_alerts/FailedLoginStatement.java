package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;


/**
 * This class compile the EPL statement to select failed authentication attempts
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class FailedLoginStatement {
    String statement = "insert into httpFailedLoginEvent\n " +
            "select IPAddress, userID, time, timeZone from httpLogEvent\n " +
            "where statusCode like \"401\"";

    private String listenStatement = "select * from httpFailedLoginEvent";


    public FailedLoginStatement(EPRuntime runtime) {
        CEPSetupUtil.compileDeploy(statement, runtime);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new FailedLoginStatementListener());
    }
}
