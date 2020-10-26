package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;

/**
 * This class compile the EPL statement to detect ARP cache flooding attempts,
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class ARPCacheFloodAlertStatement {
    private String statementEPL = "insert into ARPCacheFloodAlertEvent\n " +
            "select cast(count(distinct IP) as int) from ARPCacheUpdateEvent\n " +
            "having count(distinct IP) >= 30\n " +
            "output last every ?:alertInterval:integer second";
    private String listenStatementEPL = "select * from ARPCacheFloodAlertEvent";

    private EPStatement statement;
    private EPStatement listenStatement;

    private EPRuntime runtime;

    public ARPCacheFloodAlertStatement(EPRuntime runtime, int alertIntervalSeconds, long highPriorityThreshold) {
        this.runtime = runtime;

        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        statement = ARPAlertUtils.compileDeploy(statementEPL, runtime, options);
        listenStatement = ARPAlertUtils.compileDeploy(listenStatementEPL, runtime);
        listenStatement.addListener(new ARPCacheFloodAlertListener(highPriorityThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
