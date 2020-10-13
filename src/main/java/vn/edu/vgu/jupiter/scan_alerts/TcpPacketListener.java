package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EventSender;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

public class TcpPacketListener implements PacketListener {
    private EventSender evtSender;

    public TcpPacketListener(EventSender evtSender) {
        this.evtSender = evtSender;
    }

    @Override
    public void gotPacket(Packet packet) {
        IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
        TcpPacket tcpPacket = ipV4Packet.get(TcpPacket.class);
        TcpPacketEvent evt = new TcpPacketEvent(
                ipV4Packet.getHeader(),
                tcpPacket.getHeader()
        );
        this.evtSender.sendEvent(evt);
    }
}
