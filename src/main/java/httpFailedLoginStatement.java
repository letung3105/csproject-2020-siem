import com.espertech.esper.runtime.client.EPRuntime;

public class httpFailedLoginStatement {
    String statement = "insert into httpFailedLogin\n " +
            "select * from httpLogEvent\n " +
            "where statusCode like \"401\"";

    public httpFailedLoginStatement(EPRuntime runtime) {
        ConsecutiveFailedLoginsUtil.compileDeploy(statement, runtime);
    }
}
