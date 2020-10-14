import com.espertech.esper.runtime.client.EPRuntime;

public class httpFailedLoginStatement {
    String statement = "insert into httpFailedLogin\n " +
            "select IPAddress, time, timeZone, userID from httpLogEvent\n " +
            "where statusCode like \"401\"";

    private String listenStatement = "select * from httpConsecutiveFailedLoginAlert";


    public httpFailedLoginStatement(EPRuntime runtime) {
        ConsecutiveFailedLoginsUtil.compileDeploy(statement, runtime);
        ConsecutiveFailedLoginsUtil.compileDeploy(listenStatement, runtime).addListener(new FailedLoginStatementListener());
    }
}