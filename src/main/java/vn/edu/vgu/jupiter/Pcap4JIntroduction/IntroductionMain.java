package vn.edu.vgu.jupiter.Pcap4JIntroduction;

import org.pcap4j.core.*;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import java.net.InetAddress;

public class IntroductionMain {
    public static void main(String[] args){
        final int MAX_CAPTURE = 1;

        //FIND A NETWORK INTERFACE TO CAPTURE PACKET ON
        try {
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            PcapNetworkInterface networkInterface = Pcaps.getDevByAddress(inetAddress);

            //OPEN PCAP HANDLE FROM NETWORK INTERFACE
            int snapLen = 65536; //bytes
            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
            int timeout = 10; //milliseconds
            PcapHandle handle = networkInterface.openLive(snapLen, mode, timeout);

            PacketListener listener = new PacketListener() {
                @Override
                public void gotPacket(Packet packet) {
                    //HANDLE INFORMATION FROM PACKET
                    TcpPacket tcpPacket = packet.get(TcpPacket.class);
                    IpPacket ipPacket = packet.get(IpPacket.class);

                    //PUT INTO CLASS
                    NetworkPacketEvent networkPacketEvent = new NetworkPacketEvent(tcpPacket.getHeader(), ipPacket.getHeader(), handle.getTimestamp());

                    //TEST BY PRINT OUT
                    System.out.println("Create Network Packet object!");
                    System.out.println(networkPacketEvent.getDate());
                    System.out.println("From: " + networkPacketEvent.getSourceAddress() + ", port " + networkPacketEvent.getSourcePort());
                    System.out.println("To: " + networkPacketEvent.getDestinationAddress() + ", port " + networkPacketEvent.getDestinationPort());
                }
            };

            //USE PACKET LISTENER TO LOOP
            try{
                handle.loop(MAX_CAPTURE, listener);
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
