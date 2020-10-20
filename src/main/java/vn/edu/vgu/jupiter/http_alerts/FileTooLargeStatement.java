package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement to select consecutive attempts to send large files
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class FileTooLargeStatement {
    String statement = "insert into httpFileTooLargeEvent\n " +
            "select IPAddress, time, timeZone, userID, returnObjSize from httpLogEvent\n " +
            "where statusCode like \"413\" or cast(returnObjSize as int) > 20000 and protocol like \"POST%\"";

    private String listenStatement = "select * from httpFileTooLargeEvent";


    public FileTooLargeStatement(EPRuntime runtime) {
        CEPSetupUtil.compileDeploy(statement, runtime);
//        ConsecutiveFailedLoginsUtil.compileDeploy(listenStatement, runtime).addListener(new FileTooLargeListener());
    }
}
