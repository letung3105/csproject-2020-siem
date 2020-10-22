package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

public class FileTooLargeAlertStatement {
    private String statement =
            "insert into httpConsecutiveFailedLoginOneUserIDAlert\n" +
                    "select IPAddress, userID, time, timeZone\n" +
                    "from httpFailedLoginEvent#time(?:alertTimeWindow:integer second)\n" +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";

    private String listenStatement = "select * from httpConsecutiveFailedLoginOneUserIDAlert";

    public FileTooLargeAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold,
                                      int timeWindowSeconds, int alertIntervalSeconds,
                                      long highPriorityThreshold) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        CEPSetupUtil.compileDeploy(statement, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new FileTooLargeAlertListener(highPriorityThreshold));
    }
}
