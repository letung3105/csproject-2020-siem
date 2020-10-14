package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.vgu.jupiter.eventbean.TcpPacketEvent;

import java.time.Instant;

/**
 * Setup the Esper's runtime and packet capture, captured network packets are passed to the Esper's runtime
 *
 * @author Tung Le Vo
 */
public class PortScansAlertMain implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PortScansAlertMain.class);

    private static final int COUNT = -1;
    private static final int READ_TIMEOUT = 100; // [ms]
    private static final int SNAPLEN = 65536; // [bytes]
    private static final String FILTER = "tcp";

    private String netDevName;

    public static void main(String[] args) {
        String netDevName = args.length > 0 ? args[0] : "";
        new PortScansAlertMain(netDevName).run();
    }

    public PortScansAlertMain(String netDevName) {
        this.netDevName = netDevName;
    }

    /**
     * Setup the runtime, deploys the necessary statements and starts capturing packets
     */
    public void run() {
        try {
            Configuration configuration = PortScansAlertUtil.getConfiguration();
            EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

            // compile and deploy epl statements
            log.info("Setting up EPL");
            new TcpPacketWithClosedPortStatement(runtime);
            new VerticalPortScanAlertStatement(runtime, 100, 60, 5);
            new ClosedPortsCountStatement(runtime);
            new BlockPortScanAlertStatement(runtime, 50, 2, 60, 5);

            // getting the network interface
            PcapNetworkInterface nif = Pcaps.getDevByName(netDevName);
            log.info(nif.getName() + "(" + nif.getDescription() + ")");

            final PcapHandle handle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
            handle.setFilter(FILTER, BpfProgram.BpfCompileMode.OPTIMIZE);
            try {
                // capturing packet and send the Esper engine
                handle.loop(COUNT, (PacketListener) packet -> {
                    IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
                    TcpPacket tcpPacket = ipV4Packet.get(TcpPacket.class);
                    TcpPacketEvent evt = new TcpPacketEvent(
                            Instant.ofEpochMilli(handle.getTimestamp().getTime()),
                            tcpPacket.getHeader(),
                            ipV4Packet.getHeader()
                    );
                    runtime.getEventService().sendEventBean(evt, TcpPacketEvent.class.getSimpleName());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handle.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
