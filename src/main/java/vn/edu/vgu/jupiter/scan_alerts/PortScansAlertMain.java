package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPUndeployException;
import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.vgu.jupiter.eventbean.TcpPacket;

/**
 * This class is the new PortScansMain that have function to modify variables for all PortScan types
 *
 * @author Vo Le Tung
 * @author Pham Nguyen Thanh An
 */
public class PortScansAlertMain implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PortScansAlertMain.class);
    private static final int READ_TIMEOUT = 100; // [ms]
    private static final int SNAPLEN = 65536; // [bytes]
    private static final String FILTER = "tcp";
    private static int COUNT = -1;

    private String netDevName;
    private PortScansAlertConfigurations portScanAlertConfig;

    private EPRuntime runtime;
    private TcpPacketWithClosedPortStatement tcpClosedStatement;
    private VerticalPortScanAlertStatement verticalStatement;
    private HorizontalPortScanAlertStatement horizontalStatement;
    private BlockPortScanAlertStatement blockStatement;

    public PortScansAlertMain(String netDevName) {
        this.netDevName = netDevName;
        this.runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(),
                PortScansAlertUtil.getConfiguration());
    }

    public static void main(String[] args) {
        PortScansAlertConfigurations portScanAlertConfig = new PortScansAlertConfigurations(
                new PortScansAlertConfigurations.VerticalScan(60, 10, 100, 60),
                new PortScansAlertConfigurations.HorizontalScan(60, 10, 100, 60),
                new PortScansAlertConfigurations.BlockScan(60, 10, 5, 50, 2));

        PortScansAlertMain portScansAlertMain = new PortScansAlertMain(args[0]);
        portScansAlertMain.deploy(portScanAlertConfig);
        portScansAlertMain.run();
    }

    /**
     * Setup the runtime, deploys the necessary statements and starts capturing packets
     */
    public void run() {
        try {
            // getting the network interface
            PcapNetworkInterface nif = Pcaps.getDevByName(netDevName);
            log.info(nif.getName() + "(" + nif.getDescription() + ")");

            final PcapHandle handle = nif.openLive(
                    SNAPLEN,
                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                    READ_TIMEOUT);
            handle.setFilter(FILTER, BpfProgram.BpfCompileMode.OPTIMIZE);

            Thread portScansAlertMainThread = Thread.currentThread();
            try {
                // capturing packet and send the Esper engine
                handle.loop(COUNT, (PacketListener) packet -> {
                    if (portScansAlertMainThread.isInterrupted()) {
                        try {
                            handle.breakLoop();
                        } catch (NotOpenException e) {
                            e.printStackTrace();
                        }
                    }

                    if (packet.contains(IpV4Packet.class)) {
                        IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
                        if (ipV4Packet.contains(org.pcap4j.packet.TcpPacket.class)) {
                            org.pcap4j.packet.TcpPacket tcpPacket = ipV4Packet.get(org.pcap4j.packet.TcpPacket.class);
                            TcpPacket evt = new TcpPacket(
                                    handle.getTimestamp().getTime(),
                                    tcpPacket.getHeader(),
                                    ipV4Packet.getHeader()
                            );
                            runtime.getEventService().sendEventBean(evt, TcpPacket.class.getSimpleName());
                        }
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handle.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deploy all modules related to port scans alert with given configurations
     *
     * @param portScanAlertConfig configurations for port scans alert
     */
    public void deploy(PortScansAlertConfigurations portScanAlertConfig) {
        tcpClosedStatement = new TcpPacketWithClosedPortStatement(runtime);
        verticalStatement = new VerticalPortScanAlertStatement(
                runtime,
                portScanAlertConfig.getVerticalScan().getConnectionsCountThreshold(),
                portScanAlertConfig.getVerticalScan().getTimeWindow(),
                portScanAlertConfig.getVerticalScan().getAlertInterval(),
                portScanAlertConfig.getVerticalScan().getHighPriorityThreshold());
        horizontalStatement = new HorizontalPortScanAlertStatement(
                runtime,
                portScanAlertConfig.getHorizontalScan().getConnectionsCountThreshold(),
                portScanAlertConfig.getHorizontalScan().getTimeWindow(),
                portScanAlertConfig.getHorizontalScan().getAlertInterval(),
                portScanAlertConfig.getHorizontalScan().getHighPriorityThreshold());
        blockStatement = new BlockPortScanAlertStatement(
                runtime,
                portScanAlertConfig.getBlockScan().getPortsCountThreshold(),
                portScanAlertConfig.getBlockScan().getAddressesCountThreshold(),
                portScanAlertConfig.getBlockScan().getTimeWindow(),
                portScanAlertConfig.getHorizontalScan().getAlertInterval(),
                portScanAlertConfig.getHorizontalScan().getHighPriorityThreshold());
    }

    /**
     * Undeploy all the modules related to port scan service alerts
     *
     * @throws EPUndeployException
     */
    public void undeploy() throws EPUndeployException {
        if (tcpClosedStatement != null) {
            tcpClosedStatement.undeploy();
        }
        if (verticalStatement != null) {
            verticalStatement.undeploy();
        }
        if (horizontalStatement != null) {
            horizontalStatement.undeploy();
        }
        if (blockStatement != null) {
            blockStatement.undeploy();
        }
    }
}
