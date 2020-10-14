package vn.edu.vgu.jupiter.eventbean;

import org.pcap4j.packet.namednumber.Port;

public class HorizontalPortScanAlert {
    private Port hostPort;

    public HorizontalPortScanAlert(Port hostPort) {
        this.hostPort = hostPort;
    }

    public Port getHostPort() { return hostPort; }
}
