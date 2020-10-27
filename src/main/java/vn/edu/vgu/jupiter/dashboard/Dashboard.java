package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin;
import vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin;

import java.util.Properties;

/**
 * This contains the implementation for the graphical interface that is used to control the SIEM system, * and display
 * the information processed by the the system.
 * <p>
 * The log messages are displayed using a custom Log4j2 appender which takes all the log messages that are produced
 * by the system and appends it the the content of the designated TextArea. To control the SIEM system, the main Esper's
 * runtime is shared across individual controller, each controller can then access the plugin that was loaded with the
 * runtime to control the parameters for raising alerts corresponding to the plugin.
 *
 * @author Vo Le Tung
 * @author Pham Nguyen Than hAn
 */
public class Dashboard {

    @FXML
    public AnchorPane httpAlertControlPanel;
    @FXML
    public HTTPAlertControlPanel httpAlertControlPanelController;
    @FXML
    public AnchorPane portScansAlertControlPanel;
    @FXML
    public PortScansAlertController portScansAlertControlPanelController;
    @FXML
    public TextArea logArea;

    private String logFileLocation;
    private String netDeviceName;

    public void setLogFileLocation(String logFileLocation) {
        this.logFileLocation = logFileLocation;
    }

    public void setNetDeviceName(String netDeviceName) {
        this.netDeviceName = netDeviceName;
    }

    public void setPlugin() {
        Configuration config = new Configuration();

        // configure HTTPAlertPlugin
        // log location is /var/log/apache2/access.log
        Properties httpAlertsProps = new Properties();
        httpAlertsProps.put(HTTPAlertsPlugin.RUNTIME_URI_KEY, "SIEM");
        httpAlertsProps.put(HTTPAlertsPlugin.LOG_PATH_KEY, logFileLocation);
        config.getRuntime().addPluginLoader(
                "HTTPAlertsPlugin",
                "vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin",
                httpAlertsProps);

        // configure PortScanPlugin
        Properties portScansAlertProps = new Properties();
        portScansAlertProps.put(PortScansAlertPlugin.RUNTIME_URI_KEY, "PortScansAlertPlugin");
        portScansAlertProps.put(PortScansAlertPlugin.NETDEV_KEY, netDeviceName);
        config.getRuntime().addPluginLoader(
                "PortScansAlertPlugin",
                "vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin",
                portScansAlertProps);

        EPRuntime runtime = EPRuntimeProvider.getRuntime("SIEM", config);

        // shared the runtime with the controllers
        this.httpAlertControlPanelController.setRuntime(runtime);
        this.portScansAlertControlPanelController.setRuntime(runtime);
        TextAreaAppender.setTextArea(this.logArea);
    }
}


