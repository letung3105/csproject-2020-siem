package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vn.edu.vgu.jupiter.scan_alerts.PortScansAlertMain;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * This contains the implementation for the graphical interface that is used to control the SIEM system,
 * and display the information processed by the the system.
 *
 * @author Vo Le Tung
 */
public class Dashboard extends Application {

    public static final double HEIGHT = 300;
    public static final double WIDTH = 400;

    private EPRuntime runtime;

    @Override
    public void start(Stage primaryStage) throws IOException {
        var root = (Parent) FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage.setTitle("SIEM Dashboard");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    private void setRuntime(){
        Configuration config = new Configuration();
        Properties props = new Properties();
        props.put("runtimeURI", "PortScansAlertPlugin");
        props.put("netdev", "lo0");
        config.getRuntime().addPluginLoader("PortScansAlertPlugin", "vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin", props);

        runtime = EPRuntimeProvider.getRuntime("PortScansAlertPlugin", config);
    }
}


