package vn.edu.vgu.jupiter.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PortScansAlertController {
    /**
     * FXML fields id
     */
    @FXML private TextField verticalMinCount;
    @FXML private TextField verticalTime;
    @FXML private TextField verticalTimeInterval;
    @FXML private TextField verticalWarnCount;

    @FXML private TextField horizontalMinCount;
    @FXML private TextField horizontalTime;
    @FXML private TextField horizontalTimeInterval;
    @FXML private TextField horizontalWarnCount;

    @FXML private TextField blockMinPortCount;
    @FXML private TextField blockMinAddressCount;
    @FXML private TextField blockTime;
    @FXML private TextField blockTimeInterval;
    @FXML private TextField blockWarnAddressCount;
}
