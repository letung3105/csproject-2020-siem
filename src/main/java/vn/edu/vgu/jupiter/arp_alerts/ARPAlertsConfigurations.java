package vn.edu.vgu.jupiter.arp_alerts;

public class ARPAlertsConfigurations {
    ARPDuplicateIP arpDuplicateIP;
    ARPCacheFlood arpCacheFlood;
    ARPGratuitousAnnouncement arpGratuitousAnnouncement;

    public ARPAlertsConfigurations(ARPDuplicateIP arpDuplicateIP, ARPCacheFlood arpCacheFlood, ARPGratuitousAnnouncement arpGratuitousAnnouncement) {
        this.arpCacheFlood = arpCacheFlood;
        this.arpDuplicateIP = arpDuplicateIP;
        this.arpGratuitousAnnouncement = arpGratuitousAnnouncement;
    }
    public static class ARPDuplicateIP {

    }
    public static class ARPCacheFlood {
        int consecutiveAttemptsThreshold;
        int timeWindowSeconds;
        int alertIntervalSeconds;
        long highPriorityThreshold;

        public int getConsecutiveAttemptsThreshold() {
            return consecutiveAttemptsThreshold;
        }

        public int getTimeWindowSeconds() {
            return timeWindowSeconds;
        }

        public int getAlertIntervalSeconds() {
            return alertIntervalSeconds;
        }

        public long getHighPriorityThreshold() {
            return highPriorityThreshold;
        }

        public ARPCacheFlood(int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
            this.consecutiveAttemptsThreshold = consecutiveAttemptsThreshold;
            this.timeWindowSeconds = timeWindowSeconds;
            this.alertIntervalSeconds = alertIntervalSeconds;
            this.highPriorityThreshold = highPriorityThreshold;
        }
    }
    public static class ARPGratuitousAnnouncement {
        int consecutiveAttemptsThreshold;
        int timeWindowSeconds;
        int alertIntervalSeconds;
        long highPriorityThreshold;

        public ARPGratuitousAnnouncement(int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
            this.consecutiveAttemptsThreshold = consecutiveAttemptsThreshold;
            this.timeWindowSeconds = timeWindowSeconds;
            this.alertIntervalSeconds = alertIntervalSeconds;
            this.highPriorityThreshold = highPriorityThreshold;
        }

        public int getConsecutiveAttemptsThreshold() {
            return consecutiveAttemptsThreshold;
        }

        public int getTimeWindowSeconds() {
            return timeWindowSeconds;
        }

        public int getAlertIntervalSeconds() {
            return alertIntervalSeconds;
        }

        public long getHighPriorityThreshold() {
            return highPriorityThreshold;
        }
    }
}
