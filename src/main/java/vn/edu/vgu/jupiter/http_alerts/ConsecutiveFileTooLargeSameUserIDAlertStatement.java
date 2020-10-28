package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.http_alerts.HTTPAlertsConfigurations.getEPConfiguration;

public class ConsecutiveFileTooLargeSameUserIDAlertStatement {
    private static final String statementEPL =
            "@Name('ConsecutiveFileTooLargeSameUserIDAlert')\n" +
                    "insert into ConsecutiveFileTooLargeSameUserIDAlert\n " +
                    "select IPAddress, userID, time, timeZone, count(*)\n " +
                    "from HTTPFileTooLarge#time(?:alertTimeWindow:integer second)\n " +
                    "group by userID\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";
    private static final String listenStatementEPL = "select * from ConsecutiveFileTooLargeSameUserIDAlert";

    private EPRuntime runtime;
    private EPStatement statement;
    private EPStatement listenStatement;

    public ConsecutiveFileTooLargeSameUserIDAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        this.runtime = runtime;

        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration(), options);
        listenStatement = EPFacade.compileDeploy(listenStatementEPL, runtime, getEPConfiguration());
        listenStatement.addListener(new ConsecutiveFileTooLargeSameUserIDAlertListener(highPriorityThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
