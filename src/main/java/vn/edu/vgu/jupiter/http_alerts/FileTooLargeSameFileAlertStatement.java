import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

public class FileTooLargeSameFileAlertStatement {
    private String statementFromSameIP =
            "insert into httpMultipleFileTooLargeFromSameIPAlertEvent\n " +
                    "select time, timeZone, returnObjSize\n " +
                    "from httpFileTooLargeEvent\n " +
                    "group by returnObjSize\n " +
                    "having count(*) > ?:attemptsThreshold:integer";

    private String listenStatement = "select * from httpFileTooLargeFromSameIPAlertEvent";

    public FileTooLargeSameFileAlertStatement(EPRuntime runtime, int attemptsThreshold) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("attemptsThreshold", attemptsThreshold);
        });

        CEPSetupUtil.compileDeploy(statementFromSameIP, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new FileTooLargeSameFileAlertListener());
    }
}
