package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.arp_alerts.ARPAlertsConfigurations.getEPConfiguration;

/**
 * This class compile the EPL statement to detect surges of gratuitous ARP packets,
 *
 * @author Dang Chi Cong
 */
public class ARPMultipleUnaskedForAnnouncementAlertStatement {
    String statementEPL =
            "@Name('ARPMultipleUnaskedForAnnouncementAlertEvent')\n" +
                    "insert into ARPMultipleUnaskedForAnnouncementAlertEvent\n " +
                    "select cast(count(*) as int), srcIP, srcMAC, time\n " +
                    "from ARPAnnouncementEvent#time_batch(?:alertTimeWindow:integer second)\n " +
                    "group by srcIP\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n " +
                    "output last every ?:alertInterval:integer second";

    private String listenStatementEPL = "select * from ARPMultipleUnaskedForAnnouncementAlertEvent";

    private EPStatement statement;
    private EPStatement listenStatement;


    private EPRuntime runtime;

    public ARPMultipleUnaskedForAnnouncementAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        this.runtime = runtime;
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
            prepared.setObject("alertTimeWindow", ts.getSeconds());
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration(), options);
        listenStatement = EPFacade.compileDeploy(listenStatementEPL, runtime, getEPConfiguration());
        listenStatement.addListener(new ARPMultipleUnaskedForAnnouncementAlertListener(highPriorityThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
