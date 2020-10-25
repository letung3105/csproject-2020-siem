package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.runtime.client.EPRuntime;
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

    private boolean isNetDeviceCorrect = false;
    private boolean isApacheLogExist = false;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        dashboardController = loader.getController();
        scene.setRoot(loader.load());
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
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
