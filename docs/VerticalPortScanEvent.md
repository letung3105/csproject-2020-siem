# Vertical Port Scan Event

The following diagram describes the event hierarchy that is used to alert to user about a potential vertical port scan that is happening in the network.

```
      TcpPacketEvent
            |
            |
            v
TcpPacketWithClosedPortEvent
            |
            |
            v
   VerticalPortScanAlert
```

+ `TcpPacketEvent` will be produced whenever a network packet that uses TCP protocol is captured.
+ `TcpConnectionToClosedPortEvent` will be produced whenever a host is trying to send TCP packets to a closed port on another host.
	+ A TCP packet is determined to be sent to a closed port by detecting a pattern of packet transmission. The initial TCP packet (SYN) will be replied with a RST-packet, if it is sent to a closed port.
+ `VerticalPortScanEvent` will be raised whenever the number of `TcpConnectionToClosedPortEvent` reaches a certain threshold in a set time window.

Pattern used for identifying TCP packets that are sent to a closed port.

```
|       SYN       |
|---------------->|
|       RST       |
|<----------------|
|                 |
|                 |
v                 v
```

The current implementation is able to detect simple port scans that are performed using nmap with the following commands:
+ `nmap -sS <host_addr>`
+ `nmap -sS <host_addr>`