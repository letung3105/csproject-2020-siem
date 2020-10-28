package vn.edu.vgu.jupiter.scan_alerts.eventbean;

import java.net.InetAddress;

/**
 * Data structure that represents alerts for potential vertical port
 * scan events.
 *
 * @author Vo Le Tung
 */
public class VerticalPortScanAlert {
    private Long timestamp;
    private InetAddress hostAddr;
    private Long count;

    public VerticalPortScanAlert(Long timestamp, InetAddress hostAddr, Long count) {
        this.timestamp = timestamp;
        this.hostAddr = hostAddr;
        this.count = count;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public InetAddress getHostAddr() {
        return hostAddr;
    }

    public Long getCount() {
        return count;
    }
}
