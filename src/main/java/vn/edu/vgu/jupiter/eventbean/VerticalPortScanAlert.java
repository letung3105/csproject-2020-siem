package vn.edu.vgu.jupiter.eventbean;

import java.net.InetAddress;

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
