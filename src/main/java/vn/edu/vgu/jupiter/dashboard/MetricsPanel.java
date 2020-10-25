package vn.edu.vgu.jupiter.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A controller
 */
public class MetricsPanel implements PropertyChangeListener, Initializable {
    @FXML
    public Label numberOfTCPPacketWithClosedPortEventsLabel;
    @FXML
    public Label numberOfVerticalPortScanAlertsLabel;
    @FXML
    public Label numberOfHorizontalPortScanAlertsLabel;
    @FXML
    public Label numberOfBlockPortScanAlertsLabel;

    @FXML
    public Label numberOfHTTPFailedLoginEventsLabel;
    @FXML
    public Label numberOfConsecutiveFailedLoginsAlertsLabel;
    @FXML
    public Label numberOfConsecutiveFailedLoginsSameUserIDAlertsLabel;
    @FXML
    public Label numberOfConsecutiveFailedLoginsFromSameIPAlertsLabel;

    private boolean isControllerInitialized = false;
    private Map<String, Label> enabledMetricLogStatementNamesToLabel = new HashMap<>();
    private Map<String, Long> statementNamesToEventsCount = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enabledMetricLogStatementNamesToLabel.put("TcpPacketWithClosedPort", numberOfTCPPacketWithClosedPortEventsLabel);
        enabledMetricLogStatementNamesToLabel.put("VerticalPortScanAlert", numberOfVerticalPortScanAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("HorizontalPortScanAlert", numberOfHorizontalPortScanAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("BlockPortScanAlert", numberOfBlockPortScanAlertsLabel);

        enabledMetricLogStatementNamesToLabel.put("HTTPFailedLogin", numberOfHTTPFailedLoginEventsLabel);
        enabledMetricLogStatementNamesToLabel.put("ConsecutiveFailedLoginsAlert", numberOfConsecutiveFailedLoginsAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("ConsecutiveFailedLoginsFromSameIPAlert", numberOfConsecutiveFailedLoginsFromSameIPAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("ConsecutiveFailedLoginsSameUserIDAlert", numberOfConsecutiveFailedLoginsSameUserIDAlertsLabel);

        this.isControllerInitialized = true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (isControllerInitialized) {
            Platform.runLater(() -> {
                String propertyName = evt.getPropertyName();
                if (enabledMetricLogStatementNamesToLabel.containsKey(propertyName)) {
                    Label label = enabledMetricLogStatementNamesToLabel.get(propertyName);
                    Long count = (Long) evt.getNewValue();
                    label.setText("#events: " + count);
                }
            });
        }
    }
}
