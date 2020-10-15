import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement to select consecutive failed log in targeting one userID, raise the alert events.
 * and deploy the compiled EPL to the runtime
 *
 * @author Dang Chi Cong
 */

public class ConsecutiveFailedLoginSameUserIDStatement {
    private String statement =
            "insert into httpConsecutiveFailedLoginOneUserIDAlert\n " +
                    "select IPAddress, userID, time, timeZone\n " +
                    "from httpFailedLoginEvent#time_batch(?:alertTimeWindow: integer second)\n " +
                    "group by userID\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer";

    private String listenStatement = "select * from httpConsecutiveFailedLoginOneUserIDAlert";

    public ConsecutiveFailedLoginSameUserIDStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
            prepared.setObject("alertTimeWindow", ts.getSeconds());
        });

        CEPSetupUtil.compileDeploy(statement, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new ConsecutiveFailedLoginSameUserIDAlertListener());
    }
}
