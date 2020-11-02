package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import vn.edu.vgu.jupiter.arp_alerts.ARPAlertsPlugin;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin;
import vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin;

import javax.naming.NamingException;
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
    public PortScansAlertControlPanel portScansAlertControlPanelController;
    @FXML
    public AnchorPane arpAlertControlPanel;
    @FXML
    public ARPAlertControlPanel arpAlertControlPanelController;
    @FXML
    public TabPane metricsPanel;
    @FXML
    public MetricsPanel metricsPanelController;
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
        httpAlertsProps.put(HTTPAlertsPlugin.RUNTIME_URI_KEY, "HTTPAlertsPlugin");
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

        Properties arpAlertsProps = new Properties();
        arpAlertsProps.put(ARPAlertsPlugin.RUNTIME_URI_KEY, "ARPAlertsPlugin");
        arpAlertsProps.put(ARPAlertsPlugin.NETDEV_KEY, netDeviceName);
        config.getRuntime().addPluginLoader(
                "ARPAlertsPlugin",
                "vn.edu.vgu.jupiter.arp_alerts.ARPAlertsPlugin",
                arpAlertsProps);

        Platform.runLater(() -> {
            EPRuntime runtime = EPRuntimeProvider.getRuntime("SIEM", config);
            this.httpAlertControlPanelController.setRuntime(runtime);
            this.portScansAlertControlPanelController.setRuntime(runtime);
            this.arpAlertControlPanelController.setRuntime(runtime);

            TextAreaAppender.setTextArea(this.logArea);
            try {
                HTTPAlertsPlugin httpPlugin = (HTTPAlertsPlugin) runtime
                        .getContext().getEnvironment().get("plugin-loader/HTTPAlertsPlugin");
                PortScansAlertPlugin portScanPlugin = (PortScansAlertPlugin) runtime
                        .getContext().getEnvironment().get("plugin-loader/PortScansAlertPlugin");
                ARPAlertsPlugin arpPlugin = (ARPAlertsPlugin) runtime
                        .getContext().getEnvironment().get("plugin-loader/ARPAlertsPlugin");

                httpPlugin.addStatementMetricListener(metricsPanelController);
                portScanPlugin.addStatementMetricListener(metricsPanelController);
                arpPlugin.addStatementMetricListener(metricsPanelController);
            } catch (NamingException e) {
                e.printStackTrace();
                Platform.exit();
            }
        });
    }

}


