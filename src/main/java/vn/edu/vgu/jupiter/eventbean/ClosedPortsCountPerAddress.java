package vn.edu.vgu.jupiter.eventbean;

import java.net.InetAddress;

/**
 * Data structure represents the aggregation of an address and the
 * number of connections that have been made to a closed port on that
 * address
 *
 * @author Vo Le Tung
 */
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
