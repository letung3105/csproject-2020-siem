package vn.edu.vgu.jupiter.eventbean;

import java.time.Instant;

public class BlockPortScanAlert {
    private Instant timestamp;

    public BlockPortScanAlert(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
