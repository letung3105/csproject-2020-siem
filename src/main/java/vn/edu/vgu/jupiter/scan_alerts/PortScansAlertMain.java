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
public class PortScansAlertMain implements Runnable {
    /**
     * The predefined variables by program
     */
    private static final Logger log = LoggerFactory.getLogger(PortScansAlertMain.class);
    private static final int READ_TIMEOUT = 100; // [ms]
    private static final int SNAPLEN = 65536; // [bytes]
    private static final String FILTER = "tcp";
    private static int COUNT = -1;
    private String netDevName;

    /**
     * Default values for port scan
     */
    private int minConnectionCountVertical = 60; // [connections] [info threshold]
    private int timeWindowVertical = 60; // [seconds]
    private int countVertical = 100; // [warning threshold]
    private int intervalVertical = 10; // [seconds delay]

    private int minConnectionCountHorizontal = 60; // [connections] [info threshold]
    private int timeWindowHorizontal = 60; // [seconds]
    private int countHorizontal = 100; // [warning threshold]
    private int intervalHorizontal = 10; // [seconds delay]

    private int minPortsCount = 50; // [ports]
    private int minAddressCount = 2; // [addresses] [info threshold]
    private int timeWindowBlock = 60; // [seconds]
    private int countBlock = 5; // [warning threshold]
    private int intervalBlock = 10; // [seconds delay]
    //Some condition variable
    private boolean isVariableChange = true;

    public PortScansAlertMain(String netDevName) {
        this.netDevName = netDevName;
    }

    /**
     * Setup the runtime, deploys the necessary statements and starts capturing packets
     */
    public void run() {
        Configuration configuration = PortScansAlertUtil.getConfiguration();
        EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

        while (true) {
            if (isVariableChange) {
                isVariableChange = false;
                COUNT = -1;
                //rerun the thing
                try {
                    // TODO: statement's parameters should be modifiable from external classes
                    // compile and deploy epl statements
                    log.info("Setting up EPL");
                    new TcpPacketWithClosedPortStatement(runtime);
                    new VerticalPortScanAlertStatement(runtime, minConnectionCountVertical, timeWindowVertical, intervalVertical, countVertical);
                    new HorizontalPortScanAlertStatement(runtime, minConnectionCountHorizontal, timeWindowHorizontal, intervalHorizontal, countHorizontal);
                    new BlockPortScanAlertStatement(runtime, minPortsCount, minAddressCount, timeWindowBlock, intervalBlock, countBlock);

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
                            if (isVariableChange) {
                                COUNT = 1;
                                log.info("Variable change detect, rerun the loop.");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handle.close();
                    //undeploy
                    runtime.getDeploymentService().undeployAll();
                    log.info("Undeploy all.");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setVariableChange(boolean variableChange) {
        isVariableChange = variableChange;
    }

    public int getMinConnectionCountVertical() {
        return minConnectionCountVertical;
    }

    /**
     * Setters for modifying variables
     */
    public void setMinConnectionCountVertical(int minConnectionCountVertical) {
        this.minConnectionCountVertical = minConnectionCountVertical;
    }

    public int getTimeWindowVertical() {
        return timeWindowVertical;
    }

    public void setTimeWindowVertical(int timeWindowVertical) {
        this.timeWindowVertical = timeWindowVertical;
    }

    public int getMinConnectionCountHorizontal() {
        return minConnectionCountHorizontal;
    }

    public void setMinConnectionCountHorizontal(int minConnectionCountHorizontal) {
        this.minConnectionCountHorizontal = minConnectionCountHorizontal;
    }

    public int getTimeWindowHorizontal() {
        return timeWindowHorizontal;
    }

    public void setTimeWindowHorizontal(int timeWindowHorizontal) {
        this.timeWindowHorizontal = timeWindowHorizontal;
    }

    public int getMinPortsCount() {
        return minPortsCount;
    }

    public void setMinPortsCount(int minPortsCount) {
        this.minPortsCount = minPortsCount;
    }

    public int getMinAddressCount() {
        return minAddressCount;
    }

    public void setMinAddressCount(int minAddressCount) {
        this.minAddressCount = minAddressCount;
    }

    public int getTimeWindowBlock() {
        return timeWindowBlock;
    }

    public void setTimeWindowBlock(int timeWindowBlock) {
        this.timeWindowBlock = timeWindowBlock;
    }

    public int getCountVertical() {
        return countVertical;
    }

    public void setCountVertical(int countVertical) {
        this.countVertical = countVertical;
    }

    public int getCountHorizontal() {
        return countHorizontal;
    }

    public void setCountHorizontal(int countHorizontal) {
        this.countHorizontal = countHorizontal;
    }

    public int getCountBlock() {
        return countBlock;
    }

    public void setCountBlock(int countBlock) {
        this.countBlock = countBlock;
    }

    public void setIntervalVertical(int intervalVertical) {
        this.intervalVertical = intervalVertical;
    }

    public void setIntervalHorizontal(int intervalHorizontal) {
        this.intervalHorizontal = intervalHorizontal;
    }

    public void setIntervalBlock(int intervalBlock) {
        this.intervalBlock = intervalBlock;
    }
}
