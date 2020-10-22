package vn.edu.vgu.jupiter.eventbean_arp;

public class ARPMultipleUnaskedForAnnouncementAlertEvent {
    String srcIP;
    String srcMAC;
    String time;

    public ARPMultipleUnaskedForAnnouncementAlertEvent(String srcIP, String srcMAC, String time) {
        this.srcIP = srcIP;
        this.srcMAC = srcMAC;
        this.time = time;
    }

    public String getSrcIP() {
        return srcIP;
    }

    public String getSrcMAC() {
        return srcMAC;
    }

    public String getTime() {
        return time;
    }
}
