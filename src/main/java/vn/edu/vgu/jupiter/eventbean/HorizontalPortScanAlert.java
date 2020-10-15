package vn.edu.vgu.jupiter.eventbean;

import org.pcap4j.packet.namednumber.Port;

/**
 * A class that represent HorizontalPortScan events
 *
 * @author Pham Nguyen Thanh An
 */
public class HorizontalPortScanAlert {
    private Port hostPort;

    public HorizontalPortScanAlert(Port hostPort) {
        this.hostPort = hostPort;
    }

    public Port getHostPort() { return hostPort; }
}
