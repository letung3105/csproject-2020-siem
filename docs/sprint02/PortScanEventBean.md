# PORT SCAN EVENT BEAN VARIABLES DOCUMENTATION

This document is about the variables that were chosen to represent the events in Port scan alert.

---
### The hierarchy

```text
                                 |--> VerticalScanAlert
                                 |
TcpPacket --> TcpToClosedPort -->|--> HorizontalScanAlert
                                 |
                                 |--> ClosedPortsPerAddr --> BlockScanAlert
```
---

### Raw event class TcpPacketEvent

This class is a data structure that represents the raw events that contains the necessary information for detecting port scan using TCP protocol. 3 fields are used to keep track of necessary information for later events.
```java
public class TcpPacketEvent {
    private Long timestamp;
    private TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;
	
	}
```
+ timestamp keeps the time when the event is raise.
+ tcpHeader keeps TcpHeader of the TcpPacket so that when needed, source port and destination port values can be taken and used for determining whether this packet is part of a port scan.
+ ipHeader keeps IpHeader of the IpPacket so that when needed, source address and destination address values can be taken and used for determining whether this packet is part of a port scan.

### Event class TcpPacketWithClosedPortEvent

This class is a data structure that represents the events that is raised when a TCP connection is made to a closed port. It has 3 fields , like its raw event class, for keep track of the same things.
```java
public class TcpPacketWithClosedPortEvent {
    private Long timestamp;
    private TcpPacket.TcpHeader tcpHeader;
    private IpPacket.IpHeader ipHeader;
	
	}
```

### Event class VerticalPortScanAlert

This class is a data structure that represents alerts for potential vertical port scans events. 3 fields are used to keep track of necessary information that needed to be output.
```java
public class VerticalPortScanAlert {
    private Long timestamp;
    private InetAddress hostAddr;
    private Long count;
	
	}
```
+ timestamp keeps the time when the event is raise.
+ hostAddr keeps the host address that is extracted from IpHeader. In vertical port scans, since the attack aims at one single address and keep sending packets to its several ports.
+ count keeps track of the number of ports that has been scanned of this address. This variable is used in listener class to classify low or high priority alert.

### Event class HorizontalPortScanAlert

This class is a data structure that represents alerts for potential horizontal port scans events. 3 fields are used to keep track of necessary information that needed to be output.
```java
public class HorizontalPortScanAlert {
    private Long timestamp;
    private Port hostPort;
    private Long count;
	
	}
```
+ timestamp keeps the time when the event is raise.
+ hostPort keeps the host port that is extracted from TcpHeader. In horizontal port scans, since the attack aims at one single port and keep sending packets to several addresses targeting that port.
+ count keeps track of the number of addresses that has been scanned of this port. This variable is used in listener class to classify low or high priority alert.

### Event class ClosedPortsCountPerAddress

This class is a data structure represents the aggregation of an address and the number of connections that have been made to a closed port on that address. 3 fields are used to keep track of necessary information that needed to be output.
```java
public class ClosedPortsCountPerAddress {
    private Long timestamp;
    private InetAddress addr;
    private Long portsCount;
	
	}
```
+ timestamp keeps the time when the event is raise.
+ addr is the host address extracted from IpHeader.
+ count keeps track of the number of ports that has been scanned of this address.
### Event class BlockPortScanAlert

This class is a data structure that represents alerts for potential block port scans events. 2 fields are used to keep track of necessary information that needed to be output.
```java
public class BlockPortScanAlert {
    private Long timestamp;
    private Long count;
	
	}
```
+ timestamp keeps the time when the event is raise.
+ count keeps track of the number of addresses that has been scanned of predefined number of ports. This variable is used in listener class to classify low or high priority alert.