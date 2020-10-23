package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPUndeployException;
import org.pcap4j.core.*;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.Packet;
import vn.edu.vgu.jupiter.eventbean_arp.ARPPacketEvent;

import java.net.*;

public class ARPAlertsMain implements Runnable {
    private EPRuntime runtime;
    private ARPAnnouncementStatement arpAnnouncementStatement;
    private ARPReplyStatement arpReplyStatement;
    private ARPCacheFloodAlertStatement arpCacheFloodAlertStatement;
    private ARPDuplicateIPAlertStatement arpDuplicateIPAlertStatement;
    private ARPCacheUpdateStatement arpCacheUpdateStatement;
    private ARPMultipleUnaskedForAnnouncementAlertStatement arpMultipleUnaskedForAnnouncementAlertStatement;

    public ARPAlertsMain() {
        this.runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), ARPAlertUtils.getConfiguration());
    }

    public static void main(String[] args) throws SocketException {
        ARPAlertsConfigurations arpAlertsConfigurations = new ARPAlertsConfigurations(
                new ARPAlertsConfigurations.ARPDuplicateIP(),
                new ARPAlertsConfigurations.ARPCacheFlood(10, 120),
                new ARPAlertsConfigurations.ARPGratuitousAnnouncement(6, 10, 10, 5)
        );
        ARPAlertsMain arpAlertsMain= new ARPAlertsMain();
        arpAlertsMain.deploy(arpAlertsConfigurations);
        arpAlertsMain.run();
    }

    private void deploy(ARPAlertsConfigurations arpAlertsConfigurations) {
        arpAnnouncementStatement = new ARPAnnouncementStatement(runtime);
        arpReplyStatement = new ARPReplyStatement(runtime);
        arpCacheFloodAlertStatement =  new ARPCacheFloodAlertStatement(runtime,
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
//        Configuration configuration = ARPAlertUtils.getConfiguration();
//        EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);
//        new ARPAnnouncementStatement(runtime);
//        new ARPReplyStatement(runtime);
//        new ARPCacheFloodAlertStatement(runtime, 10, 120);
//        new ARPDuplicateIPAlertStatement(runtime);
//        new ARPCacheUpdateStatement(runtime);
//        new ARPMultipleUnaskedForAnnouncementAlertStatement(runtime, 6, 10, 10, 4);
        String ip = getPreferredOutboundIP();

        InetAddress inetAddress = null;
        try {
            //Change the InetAddress to your desire interface's address
            inetAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        PcapNetworkInterface networkInterface = null;
        try {
            networkInterface = Pcaps.getDevByAddress(inetAddress);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }

        //OPEN PCAP HANDLE FROM NETWORK INTERFACE
        int snapLen = 65536; //bytes
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        int timeout = 10; //milliseconds
        try {
            PcapHandle handle = networkInterface.openLive(snapLen, mode, timeout);
            PacketListener listener = new PacketListener() {
                @Override
                public void gotPacket(Packet packet) {
                    if (packet.contains(ArpPacket.class)) {
                        ArpPacket arp = packet.get(ArpPacket.class);
                        ARPPacketEvent arpPacketEvent = new ARPPacketEvent(arp.getHeader());
                        runtime.getEventService().sendEventBean(arpPacketEvent, "ARPPacketEvent");
                    }
                }
            };

            try{
                handle.loop(-1, listener);
            } catch (PcapNativeException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NotOpenException e) {
                e.printStackTrace();
            }
            handle.close();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the preferred outbound IP of the machine's network interfaces
     *
     * @author Bui Xuan Phuoc
     * @return preferred outbound IP of the machine
     */
    public String getPreferredOutboundIP() {
        String ip = null;
        //ip always takes the value of the preferred outbound ip
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public void undeploy() throws EPUndeployException {
        if (arpAnnouncementStatement != null) {
            arpAnnouncementStatement.undeploy();
        }
        if (arpReplyStatement != null) {
            arpReplyStatement.undeploy();
        }
        if (arpCacheFloodAlertStatement != null) {
            arpCacheFloodAlertStatement.undeploy();
        }
        if (arpDuplicateIPAlertStatement != null) {
            arpDuplicateIPAlertStatement.undeploy();
        }
        if (arpMultipleUnaskedForAnnouncementAlertStatement != null) {
            arpMultipleUnaskedForAnnouncementAlertStatement.undeploy();
        }
        if (arpCacheUpdateStatement != null) {
            arpCacheUpdateStatement.undeploy();
        }
    }
}
