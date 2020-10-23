package vn.edu.vgu.jupiter.http_alerts;

/**
 * Sets of parameters for raising http alerts.
 *
 * @author Vo Le Tung
 */
public class HTTPAlertsConfigurations {

    private FailedLogin failedLogin;
    private FailedLoginFromSameIP failedLoginFromSameIP;
    private FailedLoginSameUserID failedLoginSameUserID;

    public HTTPAlertsConfigurations(FailedLogin failedLogin, FailedLoginFromSameIP failedLoginFromSameIP, FailedLoginSameUserID failedLoginSameUserID) {
        this.failedLogin = failedLogin;
        this.failedLoginFromSameIP = failedLoginFromSameIP;
        this.failedLoginSameUserID = failedLoginSameUserID;
    }

    public FailedLogin getFailedLogin() {
        return failedLogin;
    }

    public FailedLoginFromSameIP getFailedLoginFromSameIP() {
        return failedLoginFromSameIP;
    }

    public FailedLoginSameUserID getFailedLoginSameUserID() {
        return failedLoginSameUserID;
    }

    /**
     * Set of parameters that are common across all alerts
     *
     * @author Vo Le Tung
     */
    public static abstract class GeneralHTTPAlert {
        private int consecutiveAttemptsThreshold;
        private int timeWindow;
        private int alertInterval;
        private long highPriorityThreshold;

        /**
         * Set of parameters for HTTP alerts.
         *
         * @param consecutiveAttemptsThreshold Threshold for the number of events that can happen
         * @param timeWindow                   In seconds, the sliding time window of events to be considered
         * @param alertInterval                In seconds, the interval of alert messages raising to the user
         * @param highPriorityThreshold        Threshold for classifying a high priority event
         */
        public GeneralHTTPAlert(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            this.consecutiveAttemptsThreshold = consecutiveAttemptsThreshold;
            this.timeWindow = timeWindow;
            this.alertInterval = alertInterval;
            this.highPriorityThreshold = highPriorityThreshold;
        }

        public int getConsecutiveAttemptsThreshold() {
            return consecutiveAttemptsThreshold;
        }

        public int getTimeWindow() {
            return timeWindow;
        }

        public int getAlertInterval() {
            return alertInterval;
        }

        public long getHighPriorityThreshold() {
            return highPriorityThreshold;
        }
    }

    /**
     * Set of parameters for raising failed login alerts.
     *
     * @author Vo Le Tung
     */
    public static class FailedLogin extends GeneralHTTPAlert {
        public FailedLogin(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    /**
     * Set of parameters for raising failed login from one same ip address alerts.
     *
     * @author Vo Le Tung
     */
    public static class FailedLoginFromSameIP extends GeneralHTTPAlert {
        public FailedLoginFromSameIP(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    /**
     * Set of parameters for raising failed login using one same user id alerts.
     *
     * @author Vo Le Tung
     */
    public static class FailedLoginSameUserID extends GeneralHTTPAlert {
        public FailedLoginSameUserID(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }
}
