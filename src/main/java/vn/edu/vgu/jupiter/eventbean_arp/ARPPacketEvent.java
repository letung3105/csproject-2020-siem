package vn.edu.vgu.jupiter.eventbean_arp;

import org.pcap4j.packet.ArpPacket;

import java.time.LocalDateTime;

public class ARPPacketEvent {
    String srcIP;
    String destIP;
    String srcMAC;
    String destMAC;
    boolean isReply;
    boolean isAnnouncement;
    String time;

    public ARPPacketEvent(ArpPacket.ArpHeader header) {
        this.srcIP = header.getSrcProtocolAddr().toString();
        this.destIP = header.getDstProtocolAddr().toString();
        this.srcMAC = header.getSrcHardwareAddr().toString();
        this.destMAC = header.getDstHardwareAddr().toString();
        this.isReply = (header.getOperation().equals(2)) ? true:false;
        this.isAnnouncement = (header.getOperation().equals(1) && srcIP == destIP) ? true:false;
        this.time = LocalDateTime.now().toString();
    }
    public ARPPacketEvent(String srcIP, String destIP, String srcMAC, String destMAC, boolean isReply, boolean isAnnouncement) {
        this.srcIP = srcIP;
        this.destIP = destIP;
        this.srcMAC = srcMAC;
        this.destMAC = destMAC;
        this.isReply = isReply;
        this.isAnnouncement = isAnnouncement;
        this.time = LocalDateTime.now().toString();
    }


}
