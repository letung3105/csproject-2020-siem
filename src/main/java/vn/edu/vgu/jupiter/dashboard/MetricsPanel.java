package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.metric.StatementMetric;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MetricsPanel implements UpdateListener, Initializable {
    public Label numberOfTCPPacketEventsLabel;
    public Label numberOfTCPPacketWithClosedPortEventsLabel;

    public Label numberOfVerticalPortScanAlertsLabel;
    public Label numberOfHorizontalPortScanAlertsLabel;
    public Label numberOfBlockPortScanAlertsLabel;

    public Label numberOfHTTPFailedLoginEventsLabel;
    public Label numberOfConsecutiveFailedLoginsAlertsLabel;
    public Label numberOfConsecutiveFailedLoginsSameUserIDAlertsLabel;
    public Label numberOfConsecutiveFailedLoginsFromSameIPAlertsLabel;

    private boolean isControllerInitialized = false;
    private Map<String, Label> enabledMetricLogStatementNamesToLabel = new HashMap<>();
    private Map<String, Long> statementNamesToEventsCount = new HashMap<>();

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }

        if (isControllerInitialized) {
            StatementMetric metric = (StatementMetric) newEvents[0].getUnderlying();
            if (enabledMetricLogStatementNamesToLabel.containsKey(metric.getStatementName())) {
                Platform.runLater(() -> {
                    Label label = enabledMetricLogStatementNamesToLabel.get(metric.getStatementName());
                    Long prevCount = statementNamesToEventsCount.get(metric.getStatementName());
                    Long newCount = prevCount + metric.getNumOutputIStream();
                    statementNamesToEventsCount.put(metric.getStatementName(), newCount);
                    label.setText("#events: " + newCount);
                });
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapStatementNameToLabelAndCount("TCPPacket", numberOfTCPPacketEventsLabel);
        mapStatementNameToLabelAndCount("TcpPacketWithClosedPort", numberOfTCPPacketWithClosedPortEventsLabel);
        mapStatementNameToLabelAndCount("VerticalPortScanAlert", numberOfVerticalPortScanAlertsLabel);
        mapStatementNameToLabelAndCount("HorizontalPortScanAlert", numberOfHorizontalPortScanAlertsLabel);
        mapStatementNameToLabelAndCount("BlockPortScanAlert", numberOfBlockPortScanAlertsLabel);

        mapStatementNameToLabelAndCount("HTTPFailedLogin", numberOfHTTPFailedLoginEventsLabel);
        mapStatementNameToLabelAndCount("ConsecutiveFailedLoginsAlert", numberOfConsecutiveFailedLoginsAlertsLabel);
        mapStatementNameToLabelAndCount("ConsecutiveFailedLoginsFromSameIPAlert", numberOfConsecutiveFailedLoginsFromSameIPAlertsLabel);
        mapStatementNameToLabelAndCount("ConsecutiveFailedLoginsSameUserIDAlert", numberOfConsecutiveFailedLoginsSameUserIDAlertsLabel);

        this.isControllerInitialized = true;
    }



    private void mapStatementNameToLabelAndCount(String statementName, Label label) {
        enabledMetricLogStatementNamesToLabel.put(statementName, label);
        statementNamesToEventsCount.put(statementName, 0L);
    }
}
