package vn.edu.vgu.jupiter.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<String, Label> enabledMetricLogStatementNamesToLabel = new ConcurrentHashMap<>();
    private Map<String, Long> statementNamesToEventsCount = new ConcurrentHashMap<>();

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
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            String propertyName = evt.getPropertyName();
            Label label = enabledMetricLogStatementNamesToLabel.get(propertyName);
            boolean canUpdate =
                    label != null && enabledMetricLogStatementNamesToLabel.containsKey(propertyName);
            if (canUpdate) {
                Long count = (Long) evt.getNewValue();
                label.setText("#events: " + count);
            }
        });
    }
}
