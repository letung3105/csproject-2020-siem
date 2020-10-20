public class httpConsecutiveFailedLoginAlertEvent {
    String time;
    String timeZone;

    public httpConsecutiveFailedLoginAlertEvent(String time, String timeZone) {
        this.time = time;
        this.timeZone = timeZone;
    }

    public String getTime() {
        return time;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
