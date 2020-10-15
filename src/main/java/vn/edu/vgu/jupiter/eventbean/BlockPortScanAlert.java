package vn.edu.vgu.jupiter.eventbean;

public class BlockPortScanAlert {
    private Long timestamp;

    public BlockPortScanAlert(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
