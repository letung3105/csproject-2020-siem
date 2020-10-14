import com.espertech.esper.runtime.client.EPRuntime;

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
