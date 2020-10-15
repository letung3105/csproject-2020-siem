# Block Port Scan Event

The following diagram describes the event hierarchy that is used to alert to user about a potential block port scan that is happening in the network.

```
      TcpPacketEvent
            |
            |
            v
TcpPacketWithClosedPortEvent
            |
            |
            v
 ClosedPortsCountPerAddress
            |
            |
            v
    BlockPortScanAlert
```

+ `TcpPacketEvent` and `TcpPacketWithClosedPortEvent` are structured and raised in the same way as they are when detecting vertical port scans.
+ `ClosedPortsCount` events are raised by pairing a destination address with the number of connections to a closed port on that address.
+ `BlockPortScanAlert` events are raised when the number of hosts, whose closed ports were accessed for more times than a given threshold, exceeds its limit.