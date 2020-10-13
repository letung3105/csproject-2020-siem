package vn.edu.vgu.jupiter.eventbean;

import java.net.InetAddress;

public class VerticalPortScanAlert {
    private InetAddress hostAddr;

    public VerticalPortScanAlert(InetAddress hostAddr) {
        this.hostAddr = hostAddr;
    }

    public InetAddress getHostAddr() {
        return hostAddr;
    }
}
