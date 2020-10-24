package vn.edu.vgu.jupiter.dashboard;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.desktop.AppForegroundListener;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller asks for user inputs in net device for port scans alerts and log location for http alerts
 * Upon receiving the correct values, it will switch to Dashboard
 */
public class StartupConfig extends Application {
    @FXML
    private MenuButton netDeviceMenuButton;
    @FXML
    private TextField apacheLogLocationField;

    public StartupConfig(){

    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    @FXML
    private void applyVariables(){

    }
}
