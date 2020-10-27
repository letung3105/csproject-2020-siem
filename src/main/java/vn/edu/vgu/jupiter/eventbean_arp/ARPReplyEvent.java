package vn.edu.vgu.jupiter.eventbean_arp;

public class ARPReplyEvent {
    String srcIP;
    String destIP;
    String srcMAC;
    String destMAC;
    String time;

    public ARPReplyEvent(String srcIP, String destIP, String srcMAC, String destMAC, String time) {
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
