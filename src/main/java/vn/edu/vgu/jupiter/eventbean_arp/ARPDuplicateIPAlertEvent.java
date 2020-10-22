package vn.edu.vgu.jupiter.eventbean_arp;

import java.util.ArrayList;

public class ARPDuplicateIPAlertEvent {
    String IP;
    String MACs;
    String time;

    public ARPDuplicateIPAlertEvent(String IP, String MACs, String time) {
        this.IP = IP;
        this.MACs = MACs;
        this.time = time;
    }

    public String getIP() {
        return IP;
    }

    public String getMACs() {
        return MACs;
    }

    public String getTime() {
        return time;
    }
}
