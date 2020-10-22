package vn.edu.vgu.jupiter.eventbean_arp;

public class ARPCacheFloodAlertEvent {
    int count;

    public ARPCacheFloodAlertEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
