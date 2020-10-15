package vn.edu.vgu.jupiter.eventbean;

import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TcpPacket;

/**
 * Data structures that represents the event that is raised when
 * a TCP connection is made to a closed port.
 *
 * @author Vo Le Tung
 */
public class TcpPacketWithClosedPortEvent {
    private Long timestamp;
    private TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;

    public TcpPacketWithClosedPortEvent(Long timestamp, TcpPacket.TcpHeader tcpHeader, IpPacket.IpHeader ipHeader) {
        this.timestamp = timestamp;
        this.tcpHeader = tcpHeader;
        this.ipHeader = ipHeader;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public IpPacket.IpHeader getIpHeader() {
        return ipHeader;
    }

    public TcpPacket.TcpHeader getTcpHeader() {
        return tcpHeader;
    }
}
