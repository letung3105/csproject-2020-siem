public class httpFileTooLargeSameFileEvent {
    String time;
    String timeZone;
    String returnObjSize;

    public httpFileTooLargeSameFileEvent(String time, String timeZone, String returnObjSize) {
        this.time = time;
        this.timeZone = timeZone;
        this.returnObjSize = returnObjSize;
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
}
