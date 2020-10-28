package vn.edu.vgu.jupiter.arp_alerts.eventbean;

public class ARPCacheFloodAlertEvent {
    int count;

    public ARPCacheFloodAlertEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
