package vn.edu.vgu.jupiter.scan_alerts.eventbean;

import org.pcap4j.packet.IpPacket;

/**
 * Data structure that represents the raw event that contains the
 * necessary information for detecting port scan using TCP protocol
 *
 * @author Pham Nguyen Thanh An
 */
public class TcpPacket {
    private Long timestamp;
    private org.pcap4j.packet.TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;

    public TcpPacket(Long timestamp, org.pcap4j.packet.TcpPacket.TcpHeader tcpHeader, IpPacket.IpHeader ipHeader) {
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

    public org.pcap4j.packet.TcpPacket.TcpHeader getTcpHeader() {
        return tcpHeader;
    }

}
