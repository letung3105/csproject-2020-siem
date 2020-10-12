package vn.edu.vgu.jupiter.scan_alerts;

import java.util.logging.Logger;

public class TcpPacketEventSubscriber {
    private final Logger logger = Logger.getLogger(TcpPacketEventSubscriber.class.getName());

    public void update(TcpPacketEvent evt) {
        logger.info(evt.getIpHeader().getSrcAddr() + " --> " + evt.getIpHeader().getDstAddr());
    }
}
