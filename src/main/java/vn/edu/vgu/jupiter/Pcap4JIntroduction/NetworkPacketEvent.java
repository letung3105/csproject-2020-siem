package vn.edu.vgu.jupiter.Pcap4JIntroduction;

import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TcpPacket;

import java.sql.Timestamp;
import java.util.Date;

public class NetworkPacketEvent {
    private Timestamp timeStamp;
    private Date date;

    private TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;

    private String sourceAddress;
    private int sourcePort;

    private String destinationAddress;
    private int destinationPort;

    public NetworkPacketEvent(TcpPacket.TcpHeader tcpHeader, IpPacket.IpHeader ipHeader, Timestamp timeStamp){
        this.tcpHeader = tcpHeader;
        this.ipHeader = ipHeader;
        this.timeStamp = timeStamp;

        //PARSE DATA
        sourceAddress = ipHeader.getSrcAddr().toString().substring(1);
        destinationAddress = ipHeader.getDstAddr().toString().substring(1);

        String[] tempArray = tcpHeader.getSrcPort().toString().split(" ");
        sourcePort = Integer.parseInt(tempArray[0]);
        tempArray = tcpHeader.getDstPort().toString().split(" ");
        destinationPort = Integer.parseInt(tempArray[0]);

        date = new Date(timeStamp.getTime());
    }

    public String getSourceAddress() { return sourceAddress; }
    public String getDestinationAddress() { return destinationAddress; }

    public int getDestinationPort() { return destinationPort; }
    public int getSourcePort() { return sourcePort; }

    public Date getDate() { return date; }
}
