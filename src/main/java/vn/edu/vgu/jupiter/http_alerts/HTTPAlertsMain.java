package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPUndeployException;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import vn.edu.vgu.jupiter.eventbean_http.FileTooLargeSameUserIDAlert;

import java.io.File;

public class HTTPAlertsMain implements Runnable {

    private EPRuntime runtime;
    private HTTPFailedLoginEventStatement httpFailedLoginEventStmt;
    private FailedLoginAlertStatement failedLoginAlertStmt;
    private FailedLoginFromSameIPAlertStatement failedLoginFromSameIPAlertStmt;
    private FailedLoginSameUserIDAlertStatement failedLoginSameUserIDAlertStmt;
    private FileTooLargeAlertStatement fileTooLargeAlertStmt;
    private FileTooLargeSameUserIDAlertStatement fileTooLargeSameUserIDAlertStmt;

    private String logPath;

    public HTTPAlertsMain(String logPath) {
        this.runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), CEPSetupUtil.getConfiguration());
        this.logPath = logPath;
    }

    public static void main(String[] args) {
        HTTPAlertsConfigurations httpAlertConfig = new HTTPAlertsConfigurations(
                new HTTPAlertsConfigurations.FailedLogin(15, 6, 3, 20),
                new HTTPAlertsConfigurations.FailedLoginFromSameIP(12, 2, 1, 15),
                new HTTPAlertsConfigurations.FailedLoginSameUserID(3, 2, 1, 5),
                new HTTPAlertsConfigurations.FileTooLarge(5, 1, 3, 4),
                new HTTPAlertsConfigurations.FileTooLargeSameUserID(4, 1, 5, 3)
        );

        HTTPAlertsMain httpAlertsMain = new HTTPAlertsMain("/private/var/log/apache2/access.log");
        httpAlertsMain.deploy(httpAlertConfig);
        httpAlertsMain.run();
    }

    /**
     * A while loop is run to check if there are any new entries from the log file.
     * If new entries are found, they are turned into httpLogEvent and send to the CEP machine
     */
    public void run() {
        File apacheAccessLogFile = new File(logPath);
        TailerListener listener = new HTTPDLogTailer(runtime);
        Tailer tailer = Tailer.create(apacheAccessLogFile, listener, 100);
        tailer.run();
    }

    /**
     * Deploy all the modules related to HTTP alerts with the given configurations
     *
     * @param configs configurations for http alerts
     * @author Vo Le Tung
     */
    public void deploy(HTTPAlertsConfigurations configs) {
        httpFailedLoginEventStmt = new HTTPFailedLoginEventStatement(runtime);
        failedLoginAlertStmt = new FailedLoginAlertStatement(
                runtime,
                configs.getFailedLogin().getConsecutiveAttemptsThreshold(),
                configs.getFailedLogin().getTimeWindow(),
                configs.getFailedLogin().getAlertInterval(),
                configs.getFailedLogin().getHighPriorityThreshold());
        failedLoginFromSameIPAlertStmt = new FailedLoginFromSameIPAlertStatement(runtime,
                configs.getFailedLoginFromSameIP().getConsecutiveAttemptsThreshold(),
                configs.getFailedLoginFromSameIP().getTimeWindow(),
                configs.getFailedLoginFromSameIP().getAlertInterval(),
                configs.getFailedLoginFromSameIP().getHighPriorityThreshold());
        failedLoginSameUserIDAlertStmt = new FailedLoginSameUserIDAlertStatement(runtime,
                configs.getFailedLoginSameUserID().getConsecutiveAttemptsThreshold(),
                configs.getFailedLoginSameUserID().getTimeWindow(),
                configs.getFailedLoginSameUserID().getAlertInterval(),
                configs.getFailedLoginSameUserID().getHighPriorityThreshold());

        fileTooLargeAlertStmt = new FileTooLargeAlertStatement(
                runtime,
                configs.getFileTooLarge().getConsecutiveAttemptsThreshold(),
                configs.getFileTooLarge().getTimeWindow(),
                configs.getFileTooLarge().getAlertInterval(),
                configs.getFileTooLarge().getHighPriorityThreshold());

        fileTooLargeSameUserIDAlertStmt = new FileTooLargeSameUserIDAlertStatement(runtime,
                configs.getFileTooLargeSameUserID().getConsecutiveAttemptsThreshold(),
                configs.getFailedLoginSameUserID().getTimeWindow(),
                configs.getFileTooLargeSameUserID().getAlertInterval(),
                configs.getFileTooLargeSameUserID().getHighPriorityThreshold()
        );
    }

    /**
     * Undeploy all the modules related to HTTP service alerts.
     *
     * @author Vo Le Tung
     */
    public void undeploy() throws EPUndeployException {
        if (httpFailedLoginEventStmt != null) {
            httpFailedLoginEventStmt.undeploy();
        }
        if (failedLoginAlertStmt != null) {
            failedLoginAlertStmt.undeploy();
        }
        if (failedLoginFromSameIPAlertStmt != null) {
            failedLoginFromSameIPAlertStmt.undeploy();
        }
        if (failedLoginSameUserIDAlertStmt != null) {
            failedLoginSameUserIDAlertStmt.undeploy();
        }
        if (fileTooLargeAlertStmt != null) {
            fileTooLargeAlertStmt.undeploy();
        }
        if (fileTooLargeSameUserIDAlertStmt != null) {
            fileTooLargeSameUserIDAlertStmt.undeploy();
        }
    }
}
