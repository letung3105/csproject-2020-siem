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
 * This class is the new PortScansMain that have setters and getters to modify variables for all PortScan types
 */
public class UserDefinedPortScanMain {
    /**
     * The predefined variables by program
     */
    private static final Logger log = LoggerFactory.getLogger(PortScansAlertMain.class);
    private static final int COUNT = -1;
    private static final int READ_TIMEOUT = 100; // [ms]
    private static final int SNAPLEN = 65536; // [bytes]
    private static final String FILTER = "tcp";

    /**
     * Default values for port scan
     */
    private int minConnectionCountVertical = 100; // [connections]
    private int timeWindowVertical = 60; // [seconds]

    private int minConnectionCountHorizontal = 100; // [connections]
    private int timeWindowHorizontal = 60; // [seconds]

    private int minPortsCount = 50; // [ports]
    private int minAddressCount = 2; // [addresses]
    private int timeWindowBlock = 60; // [seconds]

    /**
     * Setup the runtime, deploys the necessary statements and starts capturing packets
     */
    public void run(String netDevName) {
        try {
            Configuration configuration = PortScansAlertUtil.getConfiguration();
            EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

            // TODO: statement's parameters should be modifiable from external classes
            // compile and deploy epl statements
            log.info("Setting up EPL");
            new TcpPacketWithClosedPortStatement(runtime);
            new VerticalPortScanAlertStatement(runtime, minConnectionCountVertical, timeWindowVertical, 10);
            new HorizontalPortScanAlertStatement(runtime, minConnectionCountHorizontal, timeWindowHorizontal, 10);
            new BlockPortScanAlertStatement(runtime, minPortsCount, minAddressCount, timeWindowBlock, 10);

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Setters for modifying variables
     */
    public void setMinConnectionCountVertical(int minConnectionCountVertical) {
        this.minConnectionCountVertical = minConnectionCountVertical;
    }

    public void setTimeWindowVertical(int timeWindowVertical) {
        this.timeWindowVertical = timeWindowVertical;
    }

    public void setMinConnectionCountHorizontal(int minConnectionCountHorizontal) {
        this.minConnectionCountHorizontal = minConnectionCountHorizontal;
    }

    public void setTimeWindowHorizontal(int timeWindowHorizontal) {
        this.timeWindowHorizontal = timeWindowHorizontal;
    }

    public void setMinPortsCount(int minPortsCount) {
        this.minPortsCount = minPortsCount;
    }

    public void setMinAddressCount(int minAddressCount) {
        this.minAddressCount = minAddressCount;
    }

    public void setTimeWindowBlock(int timeWindowBlock) {
        this.timeWindowBlock = timeWindowBlock;
    }

    public int getMinConnectionCountVertical() {
        return minConnectionCountVertical;
    }

    public int getTimeWindowVertical() {
        return timeWindowVertical;
    }

    public int getMinConnectionCountHorizontal() {
        return minConnectionCountHorizontal;
    }

    public int getTimeWindowHorizontal() {
        return timeWindowHorizontal;
    }

    public int getMinPortsCount() {
        return minPortsCount;
    }

    public int getMinAddressCount() {
        return minAddressCount;
    }

    public int getTimeWindowBlock() {
        return timeWindowBlock;
    }
}
