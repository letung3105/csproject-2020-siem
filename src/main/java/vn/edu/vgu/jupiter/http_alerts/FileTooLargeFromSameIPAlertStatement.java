package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement to select consecutive attempts to send large files toward the web server sourcing from a single IP address,
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class FileTooLargeFromSameIPAlertStatement {
    private String statementFromSameIP =
            "insert into httpMultipleFileTooLargeFromSameIPAlertEvent\n " +
                    "select IPAddress, time, userID, returnObjSize, count(*)\n " +
                    "from httpFileTooLargeEvent#time(?:timeWindow:integer second)\n" +
                    "group by IPAddress\n " +
                    "having count(*) > ?:attemptsThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";

    private String listenStatement = "select * from httpFileTooLargeFromSameIPAlertEvent";

    public FileTooLargeFromSameIPAlertStatement(EPRuntime runtime, int attemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("attemptsThreshold", attemptsThreshold);
            prepared.setObject("timeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        CEPSetupUtil.compileDeploy(statementFromSameIP, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new FileTooLargeFromSameIPAlertListener(highPriorityThreshold));
    }
}
