package vn.edu.vgu.jupiter.eventbean;

import org.pcap4j.packet.namednumber.Port;

/**
 * A class that represent HorizontalPortScan events
 *
 * @author Pham Nguyen Thanh An
 */
public class HorizontalPortScanAlert {
    private Long timestamp;
    private Port hostPort;

    public HorizontalPortScanAlert(Long timestamp, Port hostPort) {
        this.timestamp = timestamp;
        this.hostPort = hostPort;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Port getHostPort() {
        return hostPort;
    }
}
