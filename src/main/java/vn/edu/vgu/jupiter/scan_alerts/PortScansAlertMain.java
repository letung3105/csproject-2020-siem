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
 * This class is the new PortScansMain that have function to modify variables for all PortScan types
 */
public class PortScansAlertMain implements Runnable{
    /**
     * The predefined variables by program
     */
    private static final Logger log = LoggerFactory.getLogger(PortScansAlertMain.class);
    private static int COUNT = -1;
    private static final int READ_TIMEOUT = 100; // [ms]
    private static final int SNAPLEN = 65536; // [bytes]
    private static final String FILTER = "tcp";

    private String netDevName;

    public static void main(String[] args){
        PortScansAlertConfigurations portScanAlertConfig = new PortScansAlertConfigurations(
                new PortScansAlertConfigurations.VerticalScan(60, 10, 100, 60),
                new PortScansAlertConfigurations.HorizontalScan(60, 10, 100, 60),
                new PortScansAlertConfigurations.BlockScan(60, 10, 5, 50, 2));
        new PortScansAlertMain(portScanAlertConfig).run();
    }
    
    private PortScansAlertConfigurations portScanAlertConfig;
    
    public PortScansAlertMain(PortScansAlertConfigurations portScanAlertConfig){ this.portScanAlertConfig = portScanAlertConfig; }
    
    public PortScansAlertMain(String netDevName){
        this.netDevName = netDevName;
    }
    
    /**
     * Setup the runtime, deploys the necessary statements and starts capturing packets
     */
    public void run() {
        Configuration configuration = PortScansAlertUtil.getConfiguration();
        EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

        try{
            // TODO: statement's parameters should be modifiable from external classes
            // compile and deploy epl statements
            log.info("Setting up EPL");
            new VerticalPortScanAlertStatement(
                    runtime,
                    portScanAlertConfig.getVerticalScan().getConnectionsCountThreshold(),
                    portScanAlertConfig.getVerticalScan().getTimeWindow(),
                    portScanAlertConfig.getVerticalScan().getAlertInterval(),
                    portScanAlertConfig.getVerticalScan().getHighPriorityThreshold());
            new HorizontalPortScanAlertStatement(
                    runtime,
                    portScanAlertConfig.getHorizontalScan().getConnectionsCountThreshold(),
                    portScanAlertConfig.getHorizontalScan().getTimeWindow(),
                    portScanAlertConfig.getHorizontalScan().getAlertInterval(),
                    portScanAlertConfig.getHorizontalScan().getHighPriorityThreshold());
            new BlockPortScanAlertStatement(
                    runtime,
                    portScanAlertConfig.getBlockScan().getPortsCountThreshold(),
                    portScanAlertConfig.getBlockScan().getAddressesCountThreshold(),
                    portScanAlertConfig.getBlockScan().getTimeWindow(),
                    portScanAlertConfig.getHorizontalScan().getAlertInterval(),
                    portScanAlertConfig.getHorizontalScan().getHighPriorityThreshold());

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
                            handle.getTimestamp().getTime(),
                            tcpPacket.getHeader(),
                            ipV4Packet.getHeader()
                    );
                    runtime.getEventService().sendEventBean(evt, TcpPacketEvent.class.getSimpleName());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handle.close();
            //undeploy
            runtime.getDeploymentService().undeployAll();
            log.info("Undeploy all.");
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
