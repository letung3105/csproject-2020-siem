import com.espertech.esper.runtime.client.EPRuntime;

public class FailedLoginStatement {
    String statement = "insert into httpFailedLoginEvent\n " +
            "select IPAddress, time, timeZone, userID from httpLogEvent\n " +
            "where statusCode like \"401\"";

    private String listenStatement = "select * from httpFailedLoginEvent";


    public FailedLoginStatement(EPRuntime runtime) {
        CEPSetupUtil.compileDeploy(statement, runtime);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new FailedLoginStatementListener());
    }
}
