# Vertical Port Scan Event

The following diagram describes that event hierarchy that is used to alert to user about a potential vertical port scan that is happening in the network.

```
TCPConnectionEvent
          |
          |
          v
TCPConnectionToClosedPortEvent
          |
          |
          v
VerticalPortScanEvent
```

`PacketTransmissionEvent` will be raised whenever there is network packet passing around between the host address and some external address.

`TCPConnectionToClosedPortEvent` will be raised whenever 
