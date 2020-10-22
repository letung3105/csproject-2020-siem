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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vn.edu.vgu.jupiter.scan_alerts.HTTPAlertsPlugin;
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
public class Dashboard extends Application implements Initializable {

    public static final double HEIGHT = 300;
    public static final double WIDTH = 400;

    private EPRuntime runtime;

    public Dashboard() {
        Configuration config = new Configuration();
        Properties httpAlertsProps = new Properties();
        httpAlertsProps.put(HTTPAlertsPlugin.RUNTIME_URI_KEY, "HTTPAlertsPlugin");
        config.getRuntime().addPluginLoader("HTTPAlertsPlugin", "vn.edu.vgu.jupiter.scan_alerts.HTTPAlertsPlugin", httpAlertsProps);
        runtime = EPRuntimeProvider.getRuntime("SIEM", config);

        setDashboard(this);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setDashboard(Dashboard thisDashboard) {
        dashboard = thisDashboard;
        latch.countDown();
    }

    public static Dashboard waitAndGetDashboard() {
        try {
            latch.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dashboard;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var root = (Parent) FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage.setTitle("SIEM Dashboard");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.httpAlertControlPanelController.setRuntime(runtime);
    }

    /**
     * This method will gather all inputs in Text Fields
     * and make portScanMain undeploy all and deploy with new vars
     */
    @FXML
    private void refreshPortScansAlertParameter() {
        setParametersVertical();
        setParametersHorizontal();
        setParametersBlock();
        portScansMain.setVariableChange(true);
    }

    private void setParametersVertical() {
        //Get parameter from GUI
        String verticalMinCountVar = verticalMinCount.getText();
        String verticalWarnCountVar = verticalWarnCount.getText();
        String verticalTimeVar = verticalTime.getText();
        String verticalTimeIntervalVar = verticalTimeInterval.getText();

        //Check if integer and set
        if (isInteger(verticalMinCountVar)) {
            portScansMain.setMinConnectionCountVertical(Integer.parseInt(verticalMinCountVar));
        }
        if (isInteger(verticalWarnCountVar)) {
            portScansMain.setCountVertical(Integer.parseInt(verticalWarnCountVar));
        }
        if (isInteger(verticalTimeVar)) {
            portScansMain.setTimeWindowVertical(Integer.parseInt(verticalTimeVar));
        }
        if (isInteger(verticalTimeIntervalVar)) {
            portScansMain.setIntervalVertical(Integer.parseInt(verticalTimeIntervalVar));
        }
    }

    private void setParametersHorizontal() {
        //Get parameter from GUI
        String minCountVar = horizontalMinCount.getText();
        String warnCountVar = horizontalWarnCount.getText();
        String timeWindowVar = horizontalTime.getText();
        String timeIntervalVar = horizontalTimeInterval.getText();

        //Check if integer and set
        if (isInteger(minCountVar)) {
            portScansMain.setMinConnectionCountHorizontal(Integer.parseInt(minCountVar));
        }
        if (isInteger(warnCountVar)) {
            portScansMain.setCountHorizontal(Integer.parseInt(warnCountVar));
        }
        if (isInteger(timeWindowVar)) {
            portScansMain.setTimeWindowHorizontal(Integer.parseInt(timeWindowVar));
        }
        if (isInteger(timeIntervalVar)) {
            portScansMain.setIntervalHorizontal(Integer.parseInt(timeIntervalVar));
        }
    }
||||||| 5729d69
    public Dashboard(){
        setDashboard(this);
    }

    public static void setDashboard(Dashboard thisDashboard){
        dashboard = thisDashboard;
        latch.countDown();
    }

    public static Dashboard waitAndGetDashboard(){
        try{
            latch.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dashboard;
    }

    //PORT SCAN
    private PortScansAlertMain portScansMain;
    @FXML
    private TextField verticalMinCount;
    @FXML
    private TextField verticalWarnCount;
    @FXML
    private TextField verticalTime;
    @FXML
    private TextField verticalTimeInterval;

    /**
     * This method will gather all inputs in Text Fields
     * and make portScanMain undeploy all and deploy with new vars
     */
    @FXML
    private void refreshPortScansAlertParameter(){
        setParametersVertical();
        setParametersHorizontal();
        setParametersBlock();
        portScansMain.setVariableChange(true);
    }

    private void setParametersVertical(){
        //Get parameter from GUI
        String verticalMinCountVar = verticalMinCount.getText();
        String verticalWarnCountVar = verticalWarnCount.getText();
        String verticalTimeVar = verticalTime.getText();
        String verticalTimeIntervalVar = verticalTimeInterval.getText();

        //Check if integer and set
        if(isInteger(verticalMinCountVar)){
            portScansMain.setMinConnectionCountVertical(Integer.parseInt(verticalMinCountVar));
        }
        if(isInteger(verticalWarnCountVar)){
            portScansMain.setCountVertical(Integer.parseInt(verticalWarnCountVar));
        }
        if(isInteger(verticalTimeVar)){
            portScansMain.setTimeWindowVertical(Integer.parseInt(verticalTimeVar));
        }
        if(isInteger(verticalTimeIntervalVar)){
            portScansMain.setIntervalVertical(Integer.parseInt(verticalTimeIntervalVar));
        }
    }

    @FXML
    private TextField horizontalMinCount;
    @FXML
    private TextField horizontalWarnCount;
    @FXML
    private TextField horizontalTime;
    @FXML
    private TextField horizontalTimeInterval;

    private void setParametersHorizontal(){
        //Get parameter from GUI
        String minCountVar = horizontalMinCount.getText();
        String warnCountVar = horizontalWarnCount.getText();
        String timeWindowVar = horizontalTime.getText();
        String timeIntervalVar = horizontalTimeInterval.getText();

        //Check if integer and set
        if(isInteger(minCountVar)){
            portScansMain.setMinConnectionCountHorizontal(Integer.parseInt(minCountVar));
        }
        if(isInteger(warnCountVar)){
            portScansMain.setCountHorizontal(Integer.parseInt(warnCountVar));
        }
        if(isInteger(timeWindowVar)){
            portScansMain.setTimeWindowHorizontal(Integer.parseInt(timeWindowVar));
        }
        if(isInteger(timeIntervalVar)){
            portScansMain.setIntervalHorizontal(Integer.parseInt(timeIntervalVar));
        }
    }

        runtime = EPRuntimeProvider.getRuntime("PortScansAlertPlugin", config);
    }
}


