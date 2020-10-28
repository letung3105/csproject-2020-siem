package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.http_alerts.HTTPAlertsConfigurations.getEPConfiguration;

public class ConsecutiveFileTooLargeAlertStatement {
    private static final String statementEPL =
            "@Name('ConsecutiveFileTooLargeAlert')\n" +
                    "insert into ConsecutiveFileTooLargeAlert\n " +
                    "select timeZone, time, count(*)\n " +
                    "from HTTPFileTooLarge#time(?:alertTimeWindow:integer second)\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";
    private static final String listenEPL = "select * from ConsecutiveFileTooLargeAlert";

    private EPRuntime runtime;
    private EPStatement statement;
    private EPStatement listenStatement;

    public ConsecutiveFileTooLargeAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        this.runtime = runtime;

        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration(), options);
        listenStatement = EPFacade.compileDeploy(listenEPL, runtime, getEPConfiguration());
        listenStatement.addListener(new ConsecutiveFileTooLargeAlertListener(highPriorityThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}