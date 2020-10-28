package vn.edu.vgu.jupiter.arp_alerts.eventbean;

public class ARPMultipleUnaskedForAnnouncementAlertEvent {
    int count;
    String srcIP;
    String srcMAC;
    String time;

    public ARPMultipleUnaskedForAnnouncementAlertEvent(int count, String srcIP, String srcMAC, String time) {
        this.count = count;
        this.srcIP = srcIP;
        this.srcMAC = srcMAC;
        this.time = time;
    }

    public int getCount() {
        return count;
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
