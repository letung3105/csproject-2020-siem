package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.runtime.client.EPRuntime;
import com.sun.nio.sctp.Notification;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.*;

/**
 * This controller asks for user inputs in net device for port scans alerts and log location for http alerts
 * Upon receiving the correct values, it will switch to Dashboard
 */
public class StartupConfig extends Application implements Initializable{
    public static final double HEIGHT = 600;
    public static final double WIDTH = 600;

    private static Scene scene;

    @FXML
    private ComboBox<String> netDeviceComboBox;
    @FXML
    private TextField apacheLogLocationField;

    Enumeration<NetworkInterface> nets;
    ObservableList<String> netNameList;

    private boolean isApacheLogExist = false;
    private String chosenLogLocation;

    private Dashboard dashboardController;
    private EPRuntime runtime;

    public StartupConfig(){
        try {
            netDeviceComboBox = new ComboBox<>();
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //set up scene and show
        var root = (Parent) FXMLLoader.load(getClass().getResource("StartupConfig.fxml"));
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("SIEM Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void applyVariables() throws IOException {
        //Checking log location
        checkLogLocation();

        if(isApacheLogExist){
            //Load
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            //TODO: How to set the variables before the plugin get them
            var root = (Parent) loader.load();
            dashboardController = loader.getController();
            dashboardController.setNetDeviceName(netDeviceComboBox.getValue());
            dashboardController.setLogFileLocation(chosenLogLocation);
            dashboardController.setPlugin();
            scene.setRoot(root);
        } else {
            //make an big error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Your apache log file may not exist, or it is not readable.");

            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> tempList = new ArrayList<>();
        for(NetworkInterface networkInterface: Collections.list(nets)){
            String netName = networkInterface.getDisplayName();
            tempList.add(netName);
        }
        netNameList = FXCollections.observableList(tempList);
        netDeviceComboBox.setItems(netNameList);
        netDeviceComboBox.getSelectionModel().selectFirst();
    }

    private void checkLogLocation(){
        chosenLogLocation = apacheLogLocationField.getText();
        //checking for location
        File file = new File(chosenLogLocation);
        if(file.canRead()){
            isApacheLogExist = true;
        }
    }
}
