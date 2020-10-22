package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import org.pcap4j.core.*;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpOperation;
import vn.edu.vgu.jupiter.eventbean_arp.ARPPacketEvent;
import vn.edu.vgu.jupiter.scan_alerts.PortScansAlertUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main implements Runnable {
    public static void main(String[] args) {
        new vn.edu.vgu.jupiter.http_alerts.Main().run();
    }


    public void run() {
        Configuration configuration = ARPAlertUtils.getConfiguration();
        EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("192.168.1.200");
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
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }

        PacketListener listener = new PacketListener() {
            @Override
            public void gotPacket(Packet packet) {
                if (packet.contains(ArpPacket.class)) {
                    ArpPacket arp = packet.get(ArpPacket.class);
                    ARPPacketEvent arpPacketEvent = new ARPPacketEvent(arp.getHeader());
                    runtime.getEventService().sendEventBean(arpPacketEvent, arpPacketEvent.getClass().getName());
                }
            }
        };
    }
}
