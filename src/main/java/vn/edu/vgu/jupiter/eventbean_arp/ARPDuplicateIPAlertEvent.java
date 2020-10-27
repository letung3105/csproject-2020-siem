package vn.edu.vgu.jupiter.eventbean_arp;

public class ARPDuplicateIPAlertEvent {
    String IP;
    String time;

    public ARPDuplicateIPAlertEvent(String IP, String time) {
        this.IP = IP;
        this.time = time;
    }

    public String getIP() {
        return IP;
    }

    public String getTime() {
        return time;
    }
}
