package vn.edu.vgu.jupiter.eventbean;

import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TcpPacket;

/**
 * Data structure that represents the raw event that contains the
 * necessary information for detecting port scan using TCP protocol
 *
 * @author Pham Nguyen Thanh An
 */
public class TcpPacketEvent {
    private Long timestamp;
    private TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;

    public TcpPacketEvent(Long timestamp, TcpPacket.TcpHeader tcpHeader, IpPacket.IpHeader ipHeader) {
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
