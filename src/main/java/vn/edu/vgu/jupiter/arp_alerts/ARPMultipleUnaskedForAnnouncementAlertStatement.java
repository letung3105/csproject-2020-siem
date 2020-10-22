package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import vn.edu.vgu.jupiter.http_alerts.CEPSetupUtil;
import vn.edu.vgu.jupiter.http_alerts.ConsecutiveFailedLoginAlertListener;

public class ARPMultipleUnaskedForAnnouncementAlertStatement {
    String statement =     "insert into ARPMultipleUnaskedForAnnouncementAlertEvent\n " +
            "select srcIP, srcMAC, time\n " +
            "from ARPAnnouncementEvent#time_batch(?:alertTimeWindow:integer second)\n " +
            "group by srcIP\n " +
            "having count(*) > ?:consecutiveAttemptThreshold:integer";

    private String listenStatement = "select * from ARPMultipleUnaskedForAnnouncementAlertEvent";

    public ARPMultipleUnaskedForAnnouncementAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
            prepared.setObject("alertTimeWindow", ts.getSeconds());
        });

        ARPAlertUtils.compileDeploy(statement, runtime, options);
        ARPAlertUtils.compileDeploy(listenStatement, runtime).addListener(new ARPMultipleUnaskedForAnnouncementAlertListener());
    }
}
