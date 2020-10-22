package vn.edu.vgu.jupiter.Pcap4JIntroduction;

import org.pcap4j.core.*;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.ArpOperation;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class IntroductionMain {
    public static void main(String[] args){
        final int MAX_CAPTURE = 1;

        //FIND A NETWORK INTERFACE TO CAPTURE PACKET ON
        try {
            InetAddress inetAddress = InetAddress.getByName("192.168.1.200");
            PcapNetworkInterface networkInterface = Pcaps.getDevByAddress(inetAddress);

            //OPEN PCAP HANDLE FROM NETWORK INTERFACE
            int snapLen = 65536; //bytes
            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
            int timeout = 10; //milliseconds
            PcapHandle handle = networkInterface.openLive(snapLen, mode, timeout);

            PacketListener listener = new PacketListener() {
                @Override
                public void gotPacket(Packet packet) {
                    if (packet.contains(ArpPacket.class)) {
                        ArpPacket arp = packet.get(ArpPacket.class);
                        if (arp.getHeader().getOperation().equals(ArpOperation.REPLY)) {
//                            SendArpRequest.resolvedAddr = arp.getHeader().getSrcHardwareAddr();
                        }
                    System.out.println(arp.getHeader());
                    System.out.println("------------");
                    }
                }
            };

            //USE PACKET LISTENER TO LOOP
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
