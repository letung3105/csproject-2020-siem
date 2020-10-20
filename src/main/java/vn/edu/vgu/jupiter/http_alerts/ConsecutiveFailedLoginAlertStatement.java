package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement to select consecutive failed authentication attempt, raise the according alert events
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class ConsecutiveFailedLoginAlertStatement {
    private String statement =
            "insert into httpConsecutiveFailedLoginAlertEvent\n " +
                    "select timeZone, time, count(*)\n " +
                    "from httpFailedLoginEvent#time(?:alertTimeWindow:integer second)\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output first every ?:alertInterval:integer second";

    private String listenStatement = "select * from httpConsecutiveFailedLoginAlertEvent";

    public ConsecutiveFailedLoginAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        CEPSetupUtil.compileDeploy(statement, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new ConsecutiveFailedLoginAlertListener(highPriorityThreshold));
    }
}
