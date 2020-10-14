package vn.edu.vgu.jupiter.eventbean;

import java.net.InetAddress;
import java.time.Instant;

public class VerticalPortScanAlert {
    private Instant timestamp;
    private InetAddress hostAddr;

    public VerticalPortScanAlert(Instant timestamp, InetAddress hostAddr) {
        this.timestamp = timestamp;
        this.hostAddr = hostAddr;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public InetAddress getHostAddr() {
        return hostAddr;
    }
}
