package vn.edu.vgu.jupiter.Pcap4JIntroduction;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import java.net.InetAddress;

public class IntroductionMain {
    public static void main(String[] args){
        //FIND A NETWORK INTERFACE TO CAPTURE PACKET ON
        try {
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            PcapNetworkInterface networkInterface = Pcaps.getDevByAddress(inetAddress);

            //OPEN PCAP HANDLE FROM NETWORK INTERFACE
            int snapLen = 65536;
            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
            int timeout = 10;
            PcapHandle handle = networkInterface.openLive(snapLen, mode, timeout);

            //NOW WE CAN START CAPTURE PACKETS
            Packet packet = handle.getNextPacketEx();
            handle.close();

            //HANDLE INFORMATION FROM PACKET
            TcpPacket tcpPacket = packet.get(TcpPacket.class);
            IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);

            String sourceAddress = ipV4Packet.getHeader().getSrcAddr().toString().substring(1);
            String destinationAddress = ipV4Packet.getHeader().getDstAddr().toString().substring(1);

            String[] tempArray = tcpPacket.getHeader().getSrcPort().toString().split(" ");
            int sourcePort = Integer.parseInt(tempArray[0]);
            tempArray = tcpPacket.getHeader().getDstPort().toString().split(" ");
            int destinationPort = Integer.parseInt(tempArray[0]);

            //PUT INTO CLASS
            NetworkPacketEvent networkPacketEvent = new NetworkPacketEvent(sourceAddress, sourcePort, destinationAddress, destinationPort);

            //TEST
            System.out.println("Create Network Packet object!");
            System.out.println("From: " + networkPacketEvent.getSourceAddress() + ", port " + networkPacketEvent.getSourcePort());
            System.out.println("To: " + networkPacketEvent.getDestinationAddress() + ", port " + networkPacketEvent.getDestinationPort());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
