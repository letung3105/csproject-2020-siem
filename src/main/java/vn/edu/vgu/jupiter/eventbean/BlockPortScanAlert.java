package vn.edu.vgu.jupiter.eventbean;

/**
 * Data structure that represents alerts for potential block port scan events
 *
 * @author Vo Le Tung
 */
public class BlockPortScanAlert {
    private Long timestamp;
    private Long count;

    public BlockPortScanAlert(Long timestamp, Long count) {
        this.timestamp = timestamp;
        this.count = count;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getCount() {
        return count;
    }
}
