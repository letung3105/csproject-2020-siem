package vn.edu.vgu.jupiter.eventbean_arp;

public class ARPBroadcastEvent {
    String srcIP;
    String destIP;
    String srcMAC;
    String time;

    public ARPBroadcastEvent(String srcIP, String destIP, String srcMAC, String time) {
        this.srcIP = srcIP;
        this.destIP = destIP;
        this.srcMAC = srcMAC;
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

    public String getTime() {
        return time;
    }
}
