package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ClosedPortsCountStatement {
    public ClosedPortsCountStatement(EPRuntime runtime) {
        PortScansAlertUtil.compileDeploy(
                "insert into ClosedPortsCount\n" +
                        "select timestamp, ipHeader.dstAddr, count(distinct tcpHeader.dstPort)\n" +
                        "from TcpPacketWithClosedPortEvent\n" +
                        "group by ipHeader.dstAddr\n",
                runtime);
    }
}
