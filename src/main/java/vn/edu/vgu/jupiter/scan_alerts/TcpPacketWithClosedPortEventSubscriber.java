package vn.edu.vgu.jupiter.scan_alerts;

import java.util.logging.Logger;

public class TcpPacketWithClosedPortEventSubscriber {
    private final Logger logger = Logger.getLogger(TcpPacketWithClosedPortEventSubscriber.class.getName());

    public void update(TcpPacketWithClosedPortEvent evt) {
        logger.info(evt.getIpHeader().getSrcAddr() + " accessed to closed port");
    }
}
