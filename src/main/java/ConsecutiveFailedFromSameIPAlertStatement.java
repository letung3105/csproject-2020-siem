import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

public class ConsecutiveFailedFromSameIPAlertStatement {
    private String statement =
            "insert into httpConsecutiveFailedLoginFromSameIPAlertEvent\n " +
                "select IPAddress, time, userID\n " +
                "from httpFailedLoginEvent#time_batch(?:alertTimeWindow: integer second)\n " +
                "group by IPAddress\n " +
                "having count(*) > ?:consecutiveAttemptThreshold:integer";

    private String listenStatement = "select * from httpConsecutiveFailedLoginFromSameIPAlertEvent";

    public ConsecutiveFailedFromSameIPAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
           prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
            prepared.setObject("alertTimeWindow", ts.getSeconds());
        });

        CEPSetupUtil.compileDeploy(statement, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new ConsecutiveFailedFromSameIPAlertListener());
    }
}
