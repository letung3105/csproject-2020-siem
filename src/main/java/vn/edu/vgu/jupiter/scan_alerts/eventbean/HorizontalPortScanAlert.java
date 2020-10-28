package vn.edu.vgu.jupiter.scan_alerts.eventbean;

import org.pcap4j.packet.namednumber.Port;

/**
 * A class that represent HorizontalPortScan events
 *
 * @author Pham Nguyen Thanh An
 */
public class HorizontalPortScanAlert {
    private Long timestamp;
    private Port hostPort;
    private Long count;

    public HorizontalPortScanAlert(Long timestamp, Port hostPort, Long count) {
        this.timestamp = timestamp;
        this.hostPort = hostPort;
        this.count = count;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Port getHostPort() {
        return hostPort;
    }

    public Long getCount() {
        return count;
    }
}
