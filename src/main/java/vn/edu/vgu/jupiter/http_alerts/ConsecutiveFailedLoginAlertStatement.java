package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.util.TimePeriod;
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
                    "select timeZone, time\n " +
                    "from httpFailedLoginEvent#time_batch(?:alertTimeWindow: integer second)\n " +
//                    "group by\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer";

    private String listenStatement = "select * from httpConsecutiveFailedLoginAlertEvent";

    public ConsecutiveFailedLoginAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
            prepared.setObject("alertTimeWindow", ts.getSeconds());
        });

        CEPSetupUtil.compileDeploy(statement, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new ConsecutiveFailedLoginAlertListener());
    }
}
