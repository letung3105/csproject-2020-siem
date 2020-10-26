package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin;
import vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin;

import javax.naming.NamingException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

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
public class Dashboard extends Application implements PropertyChangeListener, Initializable {

    public static final double WIDTH = 960;
    public static final double HEIGHT = 640;

    private EPRuntime runtime;

    @FXML
    public AnchorPane httpAlertControlPanel;
    @FXML
    public HTTPAlertControlPanel httpAlertControlPanelController;
    @FXML
    public AnchorPane portScansAlertControlPanel;
    @FXML
    public PortScansAlertController portScansAlertControlPanelController;
    @FXML
    public TabPane metricsPanel;
    @FXML
    public MetricsPanel metricsPanelController;
    @FXML
    public TextArea logArea;

    private String logFileLocation;
    private String netDeviceName;

    public Dashboard() {

    }

    public void setLogFileLocation(String logFileLocation) {
        this.logFileLocation = logFileLocation;
    }

    public void setNetDeviceName(String netDeviceName) {
        this.netDeviceName = netDeviceName;
    }

    public void setPlugin(){
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

        runtime = EPRuntimeProvider.getRuntime("SIEM", config);
    }

    public static void main(String[] args) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        PatternLayout.Builder patternBuilder = PatternLayout.newBuilder();
        patternBuilder.withPattern("%d{HH:mm:ss.SSS} [%level]: %msg%n");
        Filter filter = ThresholdFilter.createFilter(Level.INFO, Filter.Result.ACCEPT, Filter.Result.NEUTRAL);
        Appender textAreaAppender = TextAreaAppender.createAppender("TextAreaAppender", patternBuilder.build(), filter);
        ctx.getRootLogger().addAppender(textAreaAppender);
        ctx.updateLoggers();

        launch(args);
    }

    @Override
    public void stop() throws Exception {
        runtime.destroy();
        super.stop();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // set scene
        var root = (Parent) FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage.setTitle("SIEM Dashboard");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

        // shared the runtime with the controllers
        this.httpAlertControlPanelController.setRuntime(runtime);
        this.portScansAlertControlPanelController.setRuntime(runtime);
        TextAreaAppender.setTextArea(this.logArea);

        try {
            HTTPAlertsPlugin httpPlugin = (HTTPAlertsPlugin) runtime
                    .getContext().getEnvironment().get("plugin-loader/HTTPAlertsPlugin");
            PortScansAlertPlugin portScanPlugin = (PortScansAlertPlugin) runtime
                    .getContext().getEnvironment().get("plugin-loader/PortScansAlertPlugin");

            httpPlugin.addStatementMetricListener(metricsPanelController);
            portScanPlugin.addStatementMetricListener(metricsPanelController);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}


