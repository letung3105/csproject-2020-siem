package vn.edu.vgu.jupiter.dashboard;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public StartupConfig(){
        //Set the values for net device
        //Check why port scan main have null pointer exception, though it run normally
        try {
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
        //For now, let it move onto the next panel
        var dashboardRoot = (Parent) FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        scene.setRoot(dashboardRoot);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> tempList = new ArrayList<>();
        for(NetworkInterface networkInterface: Collections.list(nets)){
            String netName = networkInterface.getName(); //need to check why it only output option1 and option2
            tempList.add(netName);
        }
        netNameList = FXCollections.observableList(tempList);
        netDeviceComboBox = new ComboBox<>(netNameList);
    }

    @Override
    public void stop() throws Exception {
        //should share runtime to dashboard from here, then destroy the runtime
        super.stop();
    }
}
