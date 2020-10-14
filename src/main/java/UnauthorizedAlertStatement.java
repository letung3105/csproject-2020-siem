import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

public class UnauthorizedAlertStatement {
    private String statement =
            "insert into UnauthorizedAlert\n " +
                    "select statusCode\n " +
                    "from UnauthorizedLogin#time_batch(?:alertTimeWindow: integer second)\n " +
                    "group by statusCode\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer";

    private String listenStatement = "select * from UnauthorizedAlert";

    public UnauthorizedAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
            prepared.setObject("alertTimeWindow", ts.getSeconds());
        });

        UnauthorizedLogUtil.compileDeploy(statement, runtime, options);
        UnauthorizedLogUtil.compileDeploy(listenStatement, runtime).addListener(new UnauthorizedAlertListener());
    }
}