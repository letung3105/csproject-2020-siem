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
 * A panel that displays the number of processed events
 *
 * @author Vo Le Tung
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

    @FXML
    public Label numberOfHTTPFileTooLargeEventsLabel;
    @FXML
    public Label numberOfConsecutiveFileTooLargeAlertsLabel;
    @FXML
    public Label numberOfConsecutiveFileTooLargeFromSameIPAlertsLabel;
    @FXML
    public Label numberOfConsecutiveFileTooLargeSameUserIDAlertsLabel;

    @FXML
    public Label numberOfARPReplyEventsLabel;
    @FXML
    public Label numberOfARPBroadcastEventsLabel;
    @FXML
    public Label numberOfARPDuplicateIPAlertsLabel;
    @FXML
    public Label numberOfARPCacheFloodAlertsLabel;
    @FXML
    public Label numberOfARPAnnouncementEventsLabel;
    @FXML
    public Label numberOfCacheUpdateEventsLabel;
    @FXML
    public Label numberOfARPMultipleUnaskedForAnnouncementAlertsLabel;

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

        enabledMetricLogStatementNamesToLabel.put("HTTPFileTooLarge", numberOfHTTPFileTooLargeEventsLabel);
        enabledMetricLogStatementNamesToLabel.put("ConsecutiveFileTooLargeAlert", numberOfConsecutiveFileTooLargeAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("ConsecutiveFileTooLargeFromSameIPAlert", numberOfConsecutiveFileTooLargeFromSameIPAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("ConsecutiveFileTooLargeSameUserIDAlert", numberOfConsecutiveFileTooLargeSameUserIDAlertsLabel);

        enabledMetricLogStatementNamesToLabel.put("ARPAnnouncementEvent", numberOfARPAnnouncementEventsLabel);
        enabledMetricLogStatementNamesToLabel.put("ARPBroadcastEvent", numberOfARPBroadcastEventsLabel);
        enabledMetricLogStatementNamesToLabel.put("ARPCacheFloodAlertEvent", numberOfARPCacheFloodAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("ARPCacheUpdateEvent", numberOfCacheUpdateEventsLabel);
        enabledMetricLogStatementNamesToLabel.put("ARPDuplicateIPAlertEvent", numberOfARPDuplicateIPAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("ARPMultipleUnaskedForAnnouncementAlertEvent", numberOfARPMultipleUnaskedForAnnouncementAlertsLabel);
        enabledMetricLogStatementNamesToLabel.put("ARPReplyEvent", numberOfARPReplyEventsLabel);
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
