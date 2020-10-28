package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

public class FileTooLargeSameUserIDAlertStatement {
    private String statementEPL =
            "insert into FileTooLarSameUserIDAlert\n " +
            "select IPAddress, userID, time, timeZone, count(*)\n " +
            "from HTTPFileTooLarge#time(?:alertTimeWindow:integer second)\n " +
            "group by userID\n " +
            "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
            "output last every ?:alertInterval:integer second";
    private String listenStatementEPL = "select * from FileTooLarSameUserIDAlert";

    private EPStatement statement;
    private EPStatement listenStatement;

    private EPRuntime runtime;

    public FileTooLargeSameUserIDAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        this.runtime = runtime;

        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        statement = CEPSetupUtil.compileDeploy(statementEPL, runtime, options);
        listenStatement = CEPSetupUtil.compileDeploy(listenStatementEPL, runtime);
        listenStatement.addListener(new FileTooLargeSameUserIDAlertListener(highPriorityThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }

}
