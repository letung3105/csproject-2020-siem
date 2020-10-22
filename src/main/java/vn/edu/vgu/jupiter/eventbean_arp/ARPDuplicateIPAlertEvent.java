package vn.edu.vgu.jupiter.eventbean_arp;

public class ARPDuplicateIPAlertEvent {
    String IP;
    String recordedMAC;
    String acquiredMAC;
    String time;

    public ARPDuplicateIPAlertEvent(String IP, String recordedMAC, String acquiredMAC, String time) {
        this.IP = IP;
        this.recordedMAC = recordedMAC;
        this.acquiredMAC = acquiredMAC;
        this.time = time;
    }

    public String getIP() {
        return IP;
    }

    public String getRecordedMAC() {
        return recordedMAC;
    }

    public String getAcquiredMAC() {
        return acquiredMAC;
    }

    public String getTime() {
        return time;
    }
}
