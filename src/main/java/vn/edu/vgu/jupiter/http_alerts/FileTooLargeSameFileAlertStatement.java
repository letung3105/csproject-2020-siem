package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

public class FileTooLargeSameFileAlertStatement {
    private String statementFromSameIP =
            "insert into httpMultipleFileTooLargeFromSameIPAlertEvent\n" +
                    "select time, timeZone, returnObjSize\n" +
                    "from httpFileTooLargeEvent#time(?:alertTimeWindow:integer seconds)\n" +
                    "group by returnObjSize\n" +
                    "having count(*) > ?:attemptsThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";

    private String listenStatement = "select * from httpFileTooLargeFromSameIPAlertEvent";

    public FileTooLargeSameFileAlertStatement(EPRuntime runtime, int attemptsThreshold,
                                              int timeWindowSeconds, int alertIntervalSeconds,
                                              long highPriorityThreshold) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("attemptsThreshold", attemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        CEPSetupUtil.compileDeploy(statementFromSameIP, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new FileTooLargeSameFileAlertListener(highPriorityThreshold));
    }
}
