package vn.edu.vgu.jupiter.Pcap4JIntroduction;

import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TcpPacket;

import java.sql.Timestamp;

public class NetworkPacketEvent {
    private Timestamp timeStamp;
    private TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;
    private String sourceAddress;
    private int sourcePort;
    private String destinationAddress;
    private int destinationPort;

    public NetworkPacketEvent(TcpPacket.TcpHeader tcpHeader, IpPacket.IpHeader ipHeader, Timestamp timeStamp) {
        this.tcpHeader = tcpHeader;
        this.ipHeader = ipHeader;
        this.timeStamp = timeStamp;

        //PARSE DATA
        sourceAddress = ipHeader.getSrcAddr().getHostAddress();
        sourcePort = tcpHeader.getSrcPort().valueAsInt();
        destinationAddress = ipHeader.getDstAddr().getHostAddress();
        destinationPort = tcpHeader.getDstPort().valueAsInt();
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public TcpPacket.TcpHeader getTcpHeader() {
        return tcpHeader;
    }

    public IpPacket.IpHeader getIpHeader() {
        return ipHeader;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public int getSourcePort() {
        return sourcePort;
    }
}
