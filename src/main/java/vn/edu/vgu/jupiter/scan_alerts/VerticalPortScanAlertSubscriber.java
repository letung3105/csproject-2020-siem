package vn.edu.vgu.jupiter.scan_alerts;

import java.util.logging.Logger;

public class VerticalPortScanAlertSubscriber {
    private final Logger logger = Logger.getLogger(VerticalPortScanAlertSubscriber.class.getName());

    public void update(VerticalPortScanAlert alert) {
        logger.info(alert.getHostAddr() + " IS UNDER ATTACKS");
    }
}
