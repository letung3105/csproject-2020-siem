package vn.edu.vgu.jupiter.arp_alerts.eventbean;

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
