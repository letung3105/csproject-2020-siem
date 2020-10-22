package vn.edu.vgu.jupiter.dashboard;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vn.edu.vgu.jupiter.scan_alerts.PortScansAlertMain;

import java.io.IOException;

public class Dashboard extends Application {

    public static final double HEIGHT = 300;
    public static final double WIDTH = 400;

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

    @FXML
    private TextField blockMinAddressCount;
    @FXML
    private TextField blockMinPortCount;
    @FXML
    private TextField blockWarnAddressCount;
    @FXML
    private TextField blockTime;
    @FXML
    private TextField blockTimeInterval;

    private void setParametersBlock(){
        //Get parameter from GUI
        String minAddressCountVar = blockMinAddressCount.getText();
        String minPortCountVar = blockMinPortCount.getText();
        String warnCountVar = blockWarnAddressCount.getText();
        String timeWindowVar = blockTime.getText();
        String timeIntervalVar = blockTimeInterval.getText();

        //Check if integer and set
        if(isInteger(minAddressCountVar)){
            portScansMain.setMinAddressCount(Integer.parseInt(minAddressCountVar));
        }
        if(isInteger(minPortCountVar)){
            portScansMain.setMinPortsCount(Integer.parseInt(minPortCountVar));
        }
        if(isInteger(warnCountVar)){
            portScansMain.setCountBlock(Integer.parseInt(warnCountVar));
        }
        if(isInteger(timeWindowVar)){
            portScansMain.setTimeWindowBlock(Integer.parseInt(timeWindowVar));
        }
        if(isInteger(timeIntervalVar)){
            portScansMain.setIntervalBlock(Integer.parseInt(timeIntervalVar));
        }
    }

    private boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

}


