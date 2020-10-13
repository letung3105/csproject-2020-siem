package vn.edu.vgu.jupiter.scan_alerts;

import java.net.InetAddress;

public class VerticalPortScanAlert {
    InetAddress hostAddr;

    public VerticalPortScanAlert(InetAddress hostAddr) {
        this.hostAddr = hostAddr;
    }

    public InetAddress getHostAddr() {
        return hostAddr;
    }
}
