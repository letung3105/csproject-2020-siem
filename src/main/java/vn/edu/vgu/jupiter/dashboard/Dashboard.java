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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * This contains the implementation for the graphical interface that is used to control the SIEM system,
 * and display the information processed by the the system.
 *
 * @author Vo Le Tung
 */
public class Dashboard extends Application implements Initializable {

    public static final double HEIGHT = 300;
    public static final double WIDTH = 400;

    private EPRuntime runtime;

    @FXML
    public AnchorPane httpAlertControlPanel;
    @FXML
    public HTTPAlertControlPanel httpAlertControlPanelController;

    public Dashboard() {
        Configuration config = new Configuration();

        Properties httpAlertsProps = new Properties();
        httpAlertsProps.put(HTTPAlertsPlugin.RUNTIME_URI_KEY, "HTTPAlertsPlugin");
        // httpAlertsProps.put(HTTPAlertsPlugin.LOG_PATH_KEY, "/private/var/log/apache2/access.log");
        config.getRuntime().addPluginLoader(
                "HTTPAlertsPlugin",
                "vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin",
                httpAlertsProps);

        // Properties portScansAlertProps = new Properties();
        // httpAlertsProps.put(PortScansAlertPlugin.RUNTIME_URI_KEY, "PortScansAlertPlugin");
        // httpAlertsProps.put(HTTPAlertsPlugin.RUNTIME_URI_KEY, "PortScansAlertPlugin");
        // config.getRuntime().addPluginLoader(
        //         "PortScansAlertPlugin",
        //         "vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin",
        //         portScansAlertProps);

        runtime = EPRuntimeProvider.getRuntime("SIEM", config);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var root = (Parent) FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage.setTitle("SIEM Dashboard");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.httpAlertControlPanelController.setRuntime(runtime);
    }
}


