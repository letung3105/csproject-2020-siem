package vn.edu.vgu.jupiter.http_alerts;

public class HTTPAlertConfiguration {

    private FailedLogin failedLogin;
    private FailedLoginFromSameIP failedLoginFromSameIP;
    private FailedLoginSameUserID failedLoginSameUserID;

    public HTTPAlertConfiguration(FailedLogin failedLogin, FailedLoginFromSameIP failedLoginFromSameIP, FailedLoginSameUserID failedLoginSameUserID) {
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

    public static abstract class GeneralHTTPAlert {
        private int consecutiveAttemptsThreshold;
        private int timeWindow;
        private int alertInterval;
        private long highPriorityThreshold;

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

    public static class FailedLogin extends GeneralHTTPAlert {
        public FailedLogin(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    public static class FailedLoginFromSameIP extends GeneralHTTPAlert {
        public FailedLoginFromSameIP(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    public static class FailedLoginSameUserID extends GeneralHTTPAlert {
        public FailedLoginSameUserID(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }
}
