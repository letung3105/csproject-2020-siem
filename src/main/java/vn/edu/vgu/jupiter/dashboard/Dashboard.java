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
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin;

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
public class Dashboard extends Application implements Initializable {

    public static final double HEIGHT = 600;
    public static final double WIDTH = 400;

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
    public TextArea logArea;

    public Dashboard() {
        Configuration config = new Configuration();

        // configure HTTPAlertPlugin
        Properties httpAlertsProps = new Properties();
        httpAlertsProps.put(HTTPAlertsPlugin.RUNTIME_URI_KEY, "HTTPAlertsPlugin");
        httpAlertsProps.put(HTTPAlertsPlugin.LOG_PATH_KEY, "/var/log/apache2/access.log");
        config.getRuntime().addPluginLoader(
                "HTTPAlertsPlugin",
                "vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin",
                httpAlertsProps);

        // configure PortScanPlugin
        Properties portScansAlertProps = new Properties();
        portScansAlertProps.put("runtimeURI", "PortScansAlertPlugin");
        portScansAlertProps.put("netdev", "lo0");
        config.getRuntime().addPluginLoader(
                "PortScansAlertPlugin",
                "vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin",
                portScansAlertProps);

        runtime = EPRuntimeProvider.getRuntime("SIEM", config);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // set scene
        var root = (Parent) FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage.setTitle("SIEM Dashboard");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // shared the runtime with the controllers
        this.httpAlertControlPanelController.setRuntime(runtime);
        this.portScansAlertControlPanelController.setRuntime(runtime);
        TextAreaAppender.setTextArea(this.logArea);
    }
}


