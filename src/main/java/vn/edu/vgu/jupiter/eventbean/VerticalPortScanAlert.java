package vn.edu.vgu.jupiter.eventbean;

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

    public VerticalPortScanAlert(Long timestamp, InetAddress hostAddr) {
        this.timestamp = timestamp;
        this.hostAddr = hostAddr;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public InetAddress getHostAddr() {
        return hostAddr;
    }
}
