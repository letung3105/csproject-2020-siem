package vn.edu.vgu.jupiter.Pcap4JIntroduction;

public class NetworkPacketEvent {
    private String sourceAddress;
    private int sourcePort;

    private String destinationAddress;
    private int destinationPort;

    public NetworkPacketEvent(String sourceAddress, int sourcePort, String destinationAddress, int destinationPort){
        this.sourceAddress = sourceAddress;
        this.sourcePort = sourcePort;
        this.destinationAddress = destinationAddress;
        this.destinationPort = destinationPort;
    }

    public String getSourceAddress() { return sourceAddress; }
    public String getDestinationAddress() { return destinationAddress; }

    public int getDestinationPort() { return destinationPort; }
    public int getSourcePort() { return sourcePort; }
}
