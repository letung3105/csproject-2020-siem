package vn.edu.vgu.jupiter.eventbean_http;

public class httpConsecutiveFileTooLargeAlertEvent {
    String time;
    String timeZone;
    String returnObjSize;
    long failuresCount;

    public httpConsecutiveFileTooLargeAlertEvent(String time, String timeZone, String returnObjSize, long failuresCount) {
        this.time = time;
        this.timeZone = timeZone;
        this.returnObjSize = returnObjSize;
        this.failuresCount = failuresCount;
    }

    public String getTime() {
        return time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getReturnObjSize() {
        return returnObjSize;
    }

    public long getFailuresCount() {
        return failuresCount;
    }
}
