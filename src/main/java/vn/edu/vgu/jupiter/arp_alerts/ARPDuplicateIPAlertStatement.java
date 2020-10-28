package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.arp_alerts.ARPAlertsConfigurations.getEPConfiguration;

/**
 * This class compile the EPL statement to detect IP entries that has multiple MAC addresses linked to it,
 *
 * @author Bui Xuan Phuoc
 */
public class ARPDuplicateIPAlertStatement {
    String statementEPL = "insert into ARPDuplicateIPAlertEvent\n " +
            "select IP, time\n " +
            "from ARPCacheUpdateEvent\n " +
            "group by IP\n " +
            "having count(distinct MAC) > 1";

    private String listenStatementEPL = "select * from ARPDuplicateIPAlertEvent";
    private EPStatement statement;
    private EPStatement listenStatement;


    private EPRuntime runtime;

    public ARPDuplicateIPAlertStatement(EPRuntime runtime) {
        this.runtime = runtime;
        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration());
        listenStatement = EPFacade.compileDeploy(listenStatementEPL, runtime, getEPConfiguration());
        listenStatement.addListener(new ARPDuplicateIPAlertListener());
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
