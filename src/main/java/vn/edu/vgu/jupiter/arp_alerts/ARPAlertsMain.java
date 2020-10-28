package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPUndeployException;
import org.pcap4j.core.*;
import org.pcap4j.packet.ArpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.vgu.jupiter.eventbean_arp.ARPPacketEvent;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ARPAlertsMain implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ARPAlertsMain.class);

    private EPRuntime runtime;
    private ARPAnnouncementStatement arpAnnouncementStatement;
    private ARPReplyStatement arpReplyStatement;
    private ARPCacheFloodAlertStatement arpCacheFloodAlertStatement;
    private ARPDuplicateIPAlertStatement arpDuplicateIPAlertStatement;
    private ARPCacheUpdateStatement arpCacheUpdateStatement;
    private ARPMultipleUnaskedForAnnouncementAlertStatement arpMultipleUnaskedForAnnouncementAlertStatement;
    private ARPBroadcastStatement arpBroadcastStatement;

    public ARPAlertsMain() {
        this.runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), ARPAlertUtils.getConfiguration());
    }

    public static void main(String[] args) {
        ARPAlertsConfigurations arpAlertsConfigurations = new ARPAlertsConfigurations(
                new ARPAlertsConfigurations.ARPDuplicateIP(),
                new ARPAlertsConfigurations.ARPCacheFlood(40, 3, 10, 30),
                new ARPAlertsConfigurations.ARPGratuitousAnnouncement(4, 10, 10, 3)
        );
        ARPAlertsMain arpAlertsMain = new ARPAlertsMain();
        arpAlertsMain.deploy(arpAlertsConfigurations);
        arpAlertsMain.run();
    }

    private void deploy(ARPAlertsConfigurations arpAlertsConfigurations) {
        arpBroadcastStatement = new ARPBroadcastStatement(runtime);
        arpAnnouncementStatement = new ARPAnnouncementStatement(runtime);
        arpReplyStatement = new ARPReplyStatement(runtime);
        arpCacheFloodAlertStatement = new ARPCacheFloodAlertStatement(runtime,
                arpAlertsConfigurations.arpCacheFlood.consecutiveAttemptsThreshold,
                arpAlertsConfigurations.arpCacheFlood.timeWindowSeconds,
                arpAlertsConfigurations.arpCacheFlood.alertIntervalSeconds,
                arpAlertsConfigurations.arpCacheFlood.highPriorityThreshold);
        arpDuplicateIPAlertStatement = new ARPDuplicateIPAlertStatement(runtime);
        arpCacheUpdateStatement = new ARPCacheUpdateStatement(runtime);
        arpMultipleUnaskedForAnnouncementAlertStatement = new ARPMultipleUnaskedForAnnouncementAlertStatement(runtime,
                arpAlertsConfigurations.arpGratuitousAnnouncement.consecutiveAttemptsThreshold,
                arpAlertsConfigurations.arpGratuitousAnnouncement.timeWindowSeconds,
                arpAlertsConfigurations.arpGratuitousAnnouncement.alertIntervalSeconds,
                arpAlertsConfigurations.arpGratuitousAnnouncement.highPriorityThreshold);
    }

    public void run() {
        String ip = getPreferredOutboundIP();

        InetAddress inetAddress;
        try {
            //Change the InetAddress to your desire interface's address
            inetAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        PcapNetworkInterface networkInterface;
        try {
            networkInterface = Pcaps.getDevByAddress(inetAddress);
        } catch (PcapNativeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //OPEN PCAP HANDLE FROM NETWORK INTERFACE
        int snapLen = 65536; //bytes
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        int timeout = 10; //milliseconds
        try {
            PcapHandle handle = networkInterface.openLive(snapLen, mode, timeout);
            PacketListener listener = packet -> {
                if (packet.contains(ArpPacket.class)) {
                    ArpPacket arp = packet.get(ArpPacket.class);
                    ARPPacketEvent arpPacketEvent = new ARPPacketEvent(arp.getHeader());
                    runtime.getEventService().sendEventBean(arpPacketEvent, "ARPPacketEvent");
                }
            };

            try {
                handle.loop(-1, listener);
            } catch (PcapNativeException | InterruptedException | NotOpenException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                logger.info("Not connected to the internet");
            }
            handle.close();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the preferred outbound IP of the machine's network interfaces
     *
     * @return preferred outbound IP of the machine
     * @author Bui Xuan Phuoc
     */
    public String getPreferredOutboundIP() {
        String ip = null;
        //ip always takes the value of the preferred outbound ip
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        System.out.println(ip);
        return ip;
    }

    public void undeploy() throws EPUndeployException {
        if (arpBroadcastStatement != null) {
            arpBroadcastStatement.undeploy();
            arpBroadcastStatement = null;
        }
        if (arpAnnouncementStatement != null) {
            arpAnnouncementStatement.undeploy();
            arpAnnouncementStatement = null;
        }
        if (arpReplyStatement != null) {
            arpReplyStatement.undeploy();
            arpReplyStatement = null;
        }
        if (arpCacheFloodAlertStatement != null) {
            arpCacheFloodAlertStatement.undeploy();
            arpCacheFloodAlertStatement = null;
        }
        if (arpDuplicateIPAlertStatement != null) {
            arpDuplicateIPAlertStatement.undeploy();
            arpDuplicateIPAlertStatement = null;
        }
        if (arpMultipleUnaskedForAnnouncementAlertStatement != null) {
            arpMultipleUnaskedForAnnouncementAlertStatement.undeploy();
            arpMultipleUnaskedForAnnouncementAlertStatement = null;
        }
        if (arpCacheUpdateStatement != null) {
            arpCacheUpdateStatement.undeploy();
            arpCacheUpdateStatement = null;
        }
    }
}
