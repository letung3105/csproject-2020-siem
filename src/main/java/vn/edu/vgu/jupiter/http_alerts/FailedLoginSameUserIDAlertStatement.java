package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

/**
 * This class compile the EPL statement to select consecutive failed log in targeting one userID, raise the alert events.
 * and deploy the compiled EPL to the runtime
 *
 * @author Dang Chi Cong
 */
public class FailedLoginSameUserIDAlertStatement {
    private String statementEPL =
            "@Name('FailedLoginSameUserIDAlert')\n" +
                    "insert into FailedLoginSameUserIDAlert\n " +
                    "select IPAddress, userID, time, timeZone, count(*)\n " +
                    "from HTTPFailedLogin#time(?:alertTimeWindow:integer second)\n " +
                    "group by userID\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";
    private String listenStatementEPL = "select * from FailedLoginSameUserIDAlert";

    private EPRuntime runtime;
    private EPStatement statement;
    private EPStatement listenStatement;

    public FailedLoginSameUserIDAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        this.runtime = runtime;

        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        statement = CEPSetupUtil.compileDeploy(statementEPL, runtime, options);
        listenStatement = CEPSetupUtil.compileDeploy(listenStatementEPL, runtime);
        listenStatement.addListener(new FailedLoginSameUserIDAlertListener(highPriorityThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
