package vn.edu.vgu.jupiter.eventbean_arp;

public class ARPCacheUpdateEvent {
    String srcIP;
    String destIP;
    String srcMAC;
    String destMAC;
    String time;
    public ARPCacheUpdateEvent(ARPAnnouncementEvent event) {
        this.srcIP = event.srcIP;
        this.destIP = event.destIP;
        this.srcMAC = event.srcMAC;
        this.destMAC = event.destMAC;
        this.time = event.time;
    }

    public ARPCacheUpdateEvent(ARPReplyEvent event) {
        this.srcIP = event.srcIP;
        this.destIP = event.destIP;
        this.srcMAC = event.srcMAC;
        this.destMAC = event.destMAC;
        this.time = event.time;
    }

    public ARPCacheUpdateEvent(String srcIP, String destIP, String srcMAC, String destMAC, String time) {
        this.srcIP = srcIP;
        this.destIP = destIP;
        this.srcMAC = srcMAC;
        this.destMAC = destMAC;
        this.time = time;
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

    public String getTime() {
        return time;
    }
}
