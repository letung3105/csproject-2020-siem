package vn.edu.vgu.jupiter.eventbean;

/**
 * Data structure that represents alerts for potential block port scan events
 *
 * @author Vo Le Tung
 */
public class BlockPortScanAlert {
    private Long timestamp;

    public BlockPortScanAlert(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
