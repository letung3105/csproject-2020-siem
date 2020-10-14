package vn.edu.vgu.jupiter.eventbean;

import java.net.InetAddress;
import java.time.Instant;

public class ClosedPortsCount {
    private Instant timestamp;
    private InetAddress addr;
    private Long portsCount;

    public ClosedPortsCount(Instant timestamp, InetAddress addr, Long portsCount) {
        this.timestamp = timestamp;
        this.addr = addr;
        this.portsCount = portsCount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public Long getPortsCount() {
        return portsCount;
    }
}
