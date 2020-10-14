import  com.espertech.esper.runtime.client.EPRuntime;

public class UnauthorizedStatement {
    String statement = "insert into Unauthorized\n" +
            "select * from httpLogEvent\n " +
            "where statusCode like \"401\"";

    public UnauthorizedStatement(EPRuntime runtime) {
        UnauthorizedLogUtil.compileDeploy(statement, runtime);
    }
}
