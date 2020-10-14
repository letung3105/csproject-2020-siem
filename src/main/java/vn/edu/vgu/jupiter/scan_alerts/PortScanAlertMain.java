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

/**
 * Setup the Esper's runtime and packet capture, captured network packets are passed to the Esper's runtime
 *
 * @author Tung Le Vo
 */
public class PortScanAlertMain {

    private static final Logger log = LoggerFactory.getLogger(PortScanAlertMain.class);

    private static final int COUNT = -1;
    private static final int READ_TIMEOUT = 100; // [ms]
    private static final int SNAPLEN = 65536; // [bytes]
    private static final String FILTER = "tcp";

    public static void main(String[] args) throws Exception {
        new PortScanAlertMain().run(args);
    }

    /**
     * Setup the runtime, deploys the necessary statements and starts capturing packets
     *
     * @param args program's arguments
     * @throws PcapNativeException occurs while capturing packets
     * @throws NotOpenException    occurs while accessing network interface
     */
    public void run(String[] args) throws PcapNativeException, NotOpenException {
        Configuration configuration = PortScanAlertUtil.getConfiguration();
        EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

        // compile and deploy epl statements
        log.info("Setting up EPL");
        new PortScanAlertStatement(runtime, 100, 5);
        new TcpPacketWithClosedPortStatement(runtime);

        // getting the network interface
        String devName = args.length > 0 ? args[0] : "";
        PcapNetworkInterface nif = Pcaps.getDevByName(devName);
        log.info(nif.getName() + "(" + nif.getDescription() + ")");

        final PcapHandle handle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
        handle.setFilter(FILTER, BpfProgram.BpfCompileMode.OPTIMIZE);

        try {
            // capturing packet and send the Esper engine
            handle.loop(COUNT, (PacketListener) packet -> {
                IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
                TcpPacket tcpPacket = ipV4Packet.get(TcpPacket.class);
                TcpPacketEvent evt = new TcpPacketEvent(
                        ipV4Packet.getHeader(),
                        tcpPacket.getHeader()
                );
                runtime.getEventService().sendEventBean(evt, TcpPacketEvent.class.getSimpleName());
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handle.close();
    }
}
