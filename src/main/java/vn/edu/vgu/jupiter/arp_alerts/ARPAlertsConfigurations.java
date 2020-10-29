package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.configuration.Configuration;
import vn.edu.vgu.jupiter.arp_alerts.eventbean.*;

/**
 * Sets of parameters for raising arp alerts.
 *
 * @author Tung Le Vo
 */
public class ARPAlertsConfigurations {
    ARPDuplicateIP arpDuplicateIP;
    ARPCacheFlood arpCacheFlood;
    ARPGratuitousAnnouncement arpGratuitousAnnouncement;

    /**
     * Make and returns that default configuration for Esper
     *
     * @return the configuration
     */
    protected static Configuration getEPConfiguration() {
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(ARPPacketEvent.class);
        configuration.getCommon().addEventType(ARPReplyEvent.class);
        configuration.getCommon().addEventType(ARPBroadcastEvent.class);
        configuration.getCommon().addEventType(ARPDuplicateIPAlertEvent.class);
        configuration.getCommon().addEventType(ARPCacheFloodAlertEvent.class);
        configuration.getCommon().addEventType(ARPAnnouncementEvent.class);
        configuration.getCommon().addEventType(ARPCacheUpdateEvent.class);
        configuration.getCommon().addEventType(ARPMultipleUnaskedForAnnouncementAlertEvent.class);
        configuration.getRuntime().getLogging().setEnableExecutionDebug(false);
        configuration.getRuntime().getLogging().setEnableTimerDebug(false);
        configuration.getRuntime().getMetricsReporting().setEnableMetricsReporting(true);
        return configuration;
    }

    public ARPAlertsConfigurations(ARPDuplicateIP arpDuplicateIP, ARPCacheFlood arpCacheFlood, ARPGratuitousAnnouncement arpGratuitousAnnouncement) {
        this.arpCacheFlood = arpCacheFlood;
        this.arpDuplicateIP = arpDuplicateIP;
        this.arpGratuitousAnnouncement = arpGratuitousAnnouncement;
    }

    public ARPDuplicateIP getArpDuplicateIP() {
        return arpDuplicateIP;
    }

    public ARPCacheFlood getArpCacheFlood() {
        return arpCacheFlood;
    }

    public ARPGratuitousAnnouncement getArpGratuitousAnnouncement() {
        return arpGratuitousAnnouncement;
    }

    public static class ARPDuplicateIP {
        int alertIntervalSeconds;

        public ARPDuplicateIP(int alertIntervalSeconds) {
            this.alertIntervalSeconds = alertIntervalSeconds;
        }
    }

    public static class ARPCacheFlood {
        int consecutiveAttemptsThreshold;
        int timeWindowSeconds;
        int alertIntervalSeconds;
        long highPriorityThreshold;

        public ARPCacheFlood(int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
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
