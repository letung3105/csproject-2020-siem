package vn.edu.vgu.jupiter.eventbean;

import java.net.InetAddress;

public class ClosedPortsCountPerAddress {
    private Long timestamp;
    private InetAddress addr;
    private Long portsCount;

    public ClosedPortsCountPerAddress(Long timestamp, InetAddress addr, Long portsCount) {
        this.timestamp = timestamp;
        this.addr = addr;
        this.portsCount = portsCount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public Long getPortsCount() {
        return portsCount;
    }
}
