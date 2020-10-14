package vn.edu.vgu.jupiter.eventbean;

import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TcpPacket;

import java.time.Instant;

public class TcpPacketEvent {
    private Instant timestamp;
    private TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;

    public TcpPacketEvent(Instant timestamp, TcpPacket.TcpHeader tcpHeader, IpPacket.IpHeader ipHeader) {
        this.timestamp = timestamp;
        this.tcpHeader = tcpHeader;
        this.ipHeader = ipHeader;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public IpPacket.IpHeader getIpHeader() {
        return ipHeader;
    }

    public TcpPacket.TcpHeader getTcpHeader() {
        return tcpHeader;
    }

}
