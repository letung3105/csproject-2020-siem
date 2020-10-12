package vn.edu.vgu.jupiter.scan_alerts;

import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TcpPacket;

public class TcpPacketWithClosedPortEvent {
    public final static String statementName = "TcpPacketWithClosedPortEvent";
    private TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;

    public TcpPacketWithClosedPortEvent(IpPacket.IpHeader ipHeader, TcpPacket.TcpHeader tcpHeader) {
        this.ipHeader = ipHeader;
        this.tcpHeader = tcpHeader;
    }

    public IpPacket.IpHeader getIpHeader() {
        return ipHeader;
    }

    public TcpPacket.TcpHeader getTcpHeader() {
        return tcpHeader;
    }
}
