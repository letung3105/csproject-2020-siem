package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.runtime.client.EPRuntime;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsConfigurations;
import vn.edu.vgu.jupiter.scan_alerts.HTTPAlertsPlugin;

import javax.naming.NamingException;

public class HTTPAlertControlPanel {
    private static final Logger log = LoggerFactory.getLogger(HTTPAlertControlPanel.class);

    public TextField failedLoginAttemptsThreshold;
    public TextField failedLoginTimeWindow;
    public TextField failedLoginAlertInterval;
    public TextField failedLoginHighPriorityThreshold;

    public TextField sameIpFailedLoginAttemptsThreshold;
    public TextField sameIpFailedLoginTimeWindow;
    public TextField sameIpFailedLoginAlertInterval;
    public TextField sameIpFailedLoginHighPriorityThreshold;

    public TextField sameUserFailedLoginAttemptsThreshold;
    public TextField sameUserFailedLoginTimeWindow;
    public TextField sameUserFailedLoginAlertInterval;
    public TextField sameUserFailedLoginHighPriorityThreshold;

    private EPRuntime runtime;

    public void setRuntime(EPRuntime runtime) {
        this.runtime = runtime;
    }

    public void deploy(ActionEvent actionEvent) throws NamingException {
        HTTPAlertsPlugin plugin = (HTTPAlertsPlugin) runtime.getContext().getEnvironment().get("plugin-loader/HTTPAlertsPlugin");
        plugin.deploy(
                new HTTPAlertsConfigurations(
                        new HTTPAlertsConfigurations.FailedLogin(15, 6, 3, 20),
                        new HTTPAlertsConfigurations.FailedLoginFromSameIP(12, 2, 1, 15),
                        new HTTPAlertsConfigurations.FailedLoginSameUserID(3, 2, 1, 5))
        );
        log.info(runtime.getURI());
    }
}
