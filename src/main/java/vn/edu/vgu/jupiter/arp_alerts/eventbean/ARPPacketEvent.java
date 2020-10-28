package vn.edu.vgu.jupiter.arp_alerts.eventbean;

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
        this.isReply = header.getOperation().toString().equals("2 (REPLY)");
        this.isAnnouncement = header.getOperation().toString().equals("1 (REQUEST)") && srcIP.equals(destIP);
        this.time = LocalDateTime.now().toString();
    }

    public ARPPacketEvent(String srcIP, String destIP, String srcMAC, String destMAC, boolean isReply, boolean isAnnouncement) {
        System.out.println(destMAC);
        this.srcIP = srcIP;
        this.destIP = destIP;
        this.srcMAC = srcMAC;
        this.destMAC = destMAC;
        this.isReply = isReply;
        this.isAnnouncement = isAnnouncement;
        this.time = LocalDateTime.now().toString();
    }

    public String getSrcIP() {
        return srcIP;
    }

    public String getDestIP() {
        return destIP;
    }

    public String getSrcMAC() {
        return srcMAC;
    }

    public String getDestMAC() {
        return destMAC;
    }

    public boolean isReply() {
        return isReply;
    }

    public boolean isAnnouncement() {
        return isAnnouncement;
    }

    public String getTime() {
        return time;
    }
}
